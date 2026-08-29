# EcoGuardianes — Manual técnico

Versión 1.0.0 · `applicationId` `pe.ecoguardianes`

---

## 1. Stack

| Elemento | Versión |
|---|---|
| Lenguaje | Kotlin 2.0.21 |
| Interfaz | Jetpack Compose (BOM 2024.12.01) + Material 3 1.3.1 |
| Navegación | Navigation Compose 2.8.5 |
| Persistencia | Room 2.6.1 sobre SQLite (KSP 2.0.21-1.0.28) |
| Asincronía | Coroutines 1.9.0, Flow y StateFlow |
| Ciclo de vida | Lifecycle 2.8.7 (`collectAsStateWithLifecycle`) |
| Compilación | Android Gradle Plugin 8.7.3, Gradle 8.9, Gradle Kotlin DSL |
| JDK | 17 |
| SDK | `minSdk 24`, `targetSdk 35`, `compileSdk 35` |
| Pruebas | JUnit 4.13.2, Robolectric 4.14.1, `kotlinx-coroutines-test`, `room-testing` |

Las versiones están centralizadas en `gradle/libs.versions.toml`.

## 2. Estructura

```
pe.ecoguardianes
├── EcoGuardianesApp.kt          Application + contenedor de dependencias
├── MainActivity.kt              Activity única
├── domain/                      Kotlin puro, sin dependencias de Android
│   ├── model/                   Categorias, Zonas, Iconos, Contenido
│   └── audit/                   MotorAuditoria, Progresion, EstadoJuego,
│                                EvaluadorRecompensas, DesbloqueoZonas
├── data/
│   ├── catalogo/                Contenido educativo (inmutable)
│   ├── local/                   Entidades Room, DAOs y EcoDatabase
│   └── repo/                    EcoRepositorio
└── ui/
    ├── theme/                   Colores, tipografía, formas, preferencias
    ├── art/                     Primitivas de Canvas, iconos, escenarios, ECO
    ├── componentes/             Piezas reutilizables
    ├── nav/                     Rutas y grafo de navegación
    └── pantallas/               13 pantallas y sus ViewModels
```

Regla de dependencias: `ui → data → domain`. `domain` no conoce a nadie, por eso
todas las reglas de auditoría se prueban sin emulador ni Robolectric.

## 3. Capa de dominio

### 3.1 Modelo

- `Categoria` — 8 categorías con etiqueta, símbolo, color y explicación.
- `Gravedad` — `CONFORME`, `OBSERVACION`, `NO_CONFORMIDAD`, cada una con símbolo
  textual para no depender del color.
- `ZonaId` — las 6 zonas, con orden y XP requerido.
- `EstadoZona` — `BLOQUEADA`, `DISPONIBLE`, `EN_PROGRESO`, `COMPLETADA`,
  `DOMINADA`.
- `Rango` — Explorador, Detective y EcoGuardián con sus umbrales.
- `Situacion`, `Mision`, `ReglaAmbiental`, `ReferenciaNormativa`,
  `AccionCorrectiva`, `Reto`, `Insignia`, `Coleccionable`, `Avatar`.
- `Requisito` + `Medida` — describen de forma uniforme qué hace falta para
  desbloquear cualquier recompensa.
- `IconoAmb` — identificador de dominio de las 47 ilustraciones, para que el
  contenido no dependa de recursos de Android.

### 3.2 MotorAuditoria

Objeto sin estado con dos entradas:

```kotlin
fun evaluarHallazgo(situacion: Situacion, marca: MarcaJugador?): EvaluacionHallazgo
fun calcular(mision: Mision, situaciones: List<Situacion>, marcas: List<MarcaJugador>): ResultadoAuditoria
```

Fórmula del puntaje (constantes públicas y comprobadas por tests):

```
base = 40 · ratioDetección
     + 25 · ratioClasificación
     + 30 · ratioCorrección
     +  5 · ratioPrecisión
penalización = min(15, 5 · falsosPositivos)
puntaje = clamp(round(base − penalización), 0, 100)
```

Casos límite resueltos de forma explícita: sin problemas reales la detección y la
clasificación valen 1; sin situaciones correctas la precisión vale 1; el XP nunca
es negativo; las marcas de situaciones inexistentes se ignoran y las duplicadas no
cuentan dos veces.

### 3.3 Progresion

`nivel(xp) = xp / 250 + 1` con tope en 30. Expone `xpEnNivel`,
`xpParaSiguienteNivel`, `progresoNivel`, `subioDeNivel` y `cambioDeRango`.

### 3.4 EstadoJuego y Medidor

`EstadoJuego` es la fotografía del progreso construida siempre desde la base de
datos. `Medidor` traduce cualquier `Requisito` a un número sobre ese estado, de
modo que insignias y coleccionables comparten la misma maquinaria y ninguna barra
de progreso está escrita a mano.

### 3.5 EvaluadorRecompensas y DesbloqueoZonas

`evaluarColeccionables` hace dos pasadas: primero las piezas que dependen del
juego y luego las que dependen del tamaño de la propia colección, usando el
recuento real de la primera pasada.

`DesbloqueoZonas.estaAbierta` exige XP mínimo **y** al menos una estrella en la
zona anterior; `requisitoPendiente` devuelve el texto que se le muestra al niño.

## 4. Capa de datos

### 4.1 Catálogo

Contenido inmutable en objetos Kotlin:

| Objeto | Contenido |
|---|---|
| `CatalogoEscenarios` | 42 situaciones y 6 misiones |
| `CatalogoReglas` | 22 reglas y 14 referencias normativas |
| `CatalogoAcciones` | 90 acciones (3 por problema) |
| `CatalogoRetos` | 21 mini-retos |
| `CatalogoInsignias` | 12 insignias |
| `CatalogoColeccion` | 26 coleccionables |
| `CatalogoAvatares` | 8 avatares |

Las situaciones toman sus `accionesIds` de `CatalogoAcciones`, así que un desfase
entre catálogos rompe un test en lugar de llegar al usuario.

### 4.2 Room

Seis entidades: `perfil`, `progreso_zona`, `auditoria`, `hallazgo`, `insignia` y
`coleccionable`. `hallazgo` tiene clave foránea a `auditoria` con borrado en
cascada e índices por auditoría, categoría y situación.

El esquema se exporta a `app/schemas/` (`exportSchema = true`) y se reproduce en
`database/schema.sql`. Detalle completo en [BASE_DE_DATOS.md](BASE_DE_DATOS.md).

### 4.3 EcoRepositorio

Punto único de acceso. Expone `perfil`, `estadoJuego`, `insignias`, `coleccion`,
`auditorias` y `progresoZonas` como `Flow`, y operaciones suspendidas para
escribir.

`estadoJuego` combina ocho consultas de Room (perfil, zonas, conteo por
categoría, acciones acertadas, auditorías completadas, detecciones perfectas,
puntaje máximo y coleccionables) en un único `EstadoJuego`.

`guardarAuditoria` ejecuta dentro de una transacción: inserta la ficha, inserta
los hallazgos —incluidos los avisos equivocados, marcados con `valido = 0`—,
actualiza el progreso de la zona conservando el mejor resultado y suma el XP.
Después recalcula las recompensas y devuelve un `ResumenGuardado` con lo que
acaba de desbloquearse.

## 5. Capa de interfaz

### 5.1 Tema

`EcoGuardianesTema` define esquemas claro y oscuro con la paleta propia,
tipografía con escala configurable (1,0 o 1,18 con «texto grande») y formas
redondeadas. Las preferencias viajan por `LocalPreferenciasEco`.

### 5.2 Arte

`ui/art/Lienzo.kt` aporta primitivas en coordenadas normalizadas (`circuloN`,
`cajaN`, `lineaN`, `figuraN`, `arcoN`, `ovaloN`, `anilloN`). Sobre ellas se
dibujan los 47 iconos (`IconosEco.kt`), los 6 escenarios (`Escenarios.kt`) y ECO
con el logotipo (`EcoMascota.kt`). El icono de la aplicación es un vector
adaptativo en `res/drawable/ic_launcher_foreground.xml`.

**No hay ningún archivo de imagen en el proyecto.**

### 5.3 Pantallas

| Pantalla | Archivo |
|---|---|
| Splash | `SplashPantalla.kt` |
| Onboarding y creación de perfil | `OnboardingPantalla.kt` |
| Mapa principal | `MapaPantalla.kt` |
| Selección de misión | `MisionPantalla.kt` |
| Escenario de auditoría | `EscenarioPantalla.kt` |
| Detalle, clasificación, regla, acción, reto, cierre | `PanelesAuditoria.kt` |
| Mini-retos | `RetoInteractivo.kt` |
| Resultado de auditoría | `ResultadoPantalla.kt` |
| Biblioteca ambiental | `BibliotecaPantalla.kt` |
| Colección e insignias | `ColeccionPantalla.kt` |
| Perfil y configuración | `PerfilPantalla.kt` |

### 5.4 ViewModels

- `AppViewModel` — perfil, preferencias y `EstadoJuego` global.
- `AuditoriaViewModel` — máquina de estados de la partida.
- `ResultadoViewModel` — relee la ficha guardada desde la base de datos.

`AuditoriaViewModel` recorre las fases `EXPLORAR → INSPECCION →
[FALSO_POSITIVO] → CLASIFICAR → REGLA → ACCION → [RETO] → RESUELTO`, más `CIERRE`
para revisar el acta. Todo el estado vive en un único `StateFlow<EstadoAuditoria>`,
así que la rotación y la recomposición no pierden nada.

### 5.5 Navegación

`EcoApp` monta el tema y el `NavHost`. Rutas en `Rutas.kt`; las de misión,
escenario y resultado llevan argumento. `AuditoriaViewModel` y
`ResultadoViewModel` se crean en el nivel de `EcoApp` para que la pantalla de
resultado pueda mostrar las recompensas recién obtenidas.

Inyección de dependencias manual: `Contenedor` en `EcoGuardianesApp` y una
fábrica genérica `factoria { ... }`.

## 6. Compilación

```bash
./gradlew clean
./gradlew testDebugUnitTest
./gradlew lintDebug
./gradlew assembleDebug
```

Salida: `app/build/outputs/apk/debug/app-debug.apk`.

`local.properties` (no versionado) debe apuntar al SDK:

```properties
sdk.dir=C:/ruta/al/Android/Sdk
```

En Linux y macOS basta con `ANDROID_HOME`.

### Nota para Windows con tildes en la ruta de usuario

Si la carpeta del usuario contiene acentos o eñes, hay que apuntar
`GRADLE_USER_HOME`, `TMP` y `TEMP` a rutas ASCII. El motivo y la solución exacta
están en [BUILD_REPORT.md](BUILD_REPORT.md), sección «Incidencias resueltas».

## 7. Pruebas

102 pruebas unitarias:

| Archivo | Qué cubre | Nº |
|---|---|---|
| `MotorAuditoriaTest` | Detección, clasificación, penalizaciones, estrellas, XP | 16 |
| `ProgresionTest` | Niveles, rangos, topes y valores negativos | 6 |
| `RecompensasTest` | Medidor, insignias, coleccionables y apertura de zonas | 12 |
| `CasosLimiteTest` | Texto largo y vacío, valores inválidos, listas vacías | 15 |
| `CatalogoTest` | Integridad del contenido, mínimos y codificación UTF-8 | 23 |
| `RepositorioRoomTest` | Persistencia real con Room en memoria | 17 |
| `FlujoAuditoriaTest` | Ciclo completo a través del ViewModel | 13 |

Los dos últimos usan Robolectric con una base Room en memoria. Como Room resuelve
sus consultas en su propio *executor*, el reloj virtual de `runTest` no basta para
saber cuándo terminó una corrutina lanzada en `viewModelScope`: por eso
`FlujoAuditoriaTest` incluye el ayudante `esperarA { }`.

```bash
./gradlew testDebugUnitTest
```

Informe HTML en `app/build/reports/tests/testDebugUnitTest/index.html`.

## 8. Integración continua

`.github/workflows/build-apk.yml`:

1. Ubuntu más reciente.
2. `actions/checkout@v4`.
3. JDK 17 (Temurin).
4. SDK de Android + `platforms;android-35` y `build-tools;35.0.0`.
5. Caché de Gradle.
6. `./gradlew testDebugUnitTest`
7. `./gradlew lintDebug`
8. `./gradlew assembleDebug`
9. SHA-256 del APK.
10. APK e informes publicados como *artifacts*.

## 9. Codificación

Todo el proyecto es UTF-8. `gradle.properties` fija
`-Dfile.encoding=UTF-8`. `CatalogoTest` incluye una prueba que falla si
desaparecen las tildes, la eñe o los signos de apertura, o si aparecen caracteres
corruptos (`Ã`, `�`).

## 10. Rendimiento y tamaño

- Sin imágenes: todo el arte es Canvas.
- `resourceConfigurations += ["es"]` recorta las traducciones sobrantes.
- `release` activa R8 con `isMinifyEnabled` e `isShrinkResources`.
- Las animaciones son cortas (600–1400 ms) y se apoyan en
  `rememberInfiniteTransition` y `animateFloatAsState`.

## 11. Extender el proyecto

**Añadir una situación:** crear la entrada en `CatalogoEscenarios`, su grupo de
tres acciones en `CatalogoAcciones` y, si procede, un reto en `CatalogoRetos`.
`CatalogoTest` avisará si falta algo.

**Añadir una regla:** entrada en `CatalogoReglas`, con `ReferenciaNormativa` solo
si la norma es real y verificable.

**Añadir una recompensa:** entrada en `CatalogoInsignias` o `CatalogoColeccion`
con su `Requisito`. Si hace falta una métrica nueva, se añade a `Medida` y a
`Medidor.valor`.

**Añadir un icono:** valor nuevo en `IconoAmb`, dibujo en `dibujarIcono` y texto
en `descripcionDe`. Un test comprueba que ningún icono se quede sin descripción
accesible.
