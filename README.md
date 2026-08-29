# EcoGuardianes

**Detecta, aprende y protege.**

Simulador educativo de auditoría ambiental para niños de 8 a 12 años.
Aplicación Android nativa, **100 % offline**, escrita en Kotlin con Jetpack Compose.

El niño no responde un cuestionario: entra en un escenario ilustrado, **observa**,
**investiga**, **detecta** situaciones que no cumplen una regla ambiental, las
**clasifica**, aprende la **regla** que las explica, **propone** una acción
correctiva y la **aplica** en un mini-reto. Al final recibe una ficha de auditoría
calculada con sus decisiones reales.

---

## El ciclo de juego

```
ENTRAR AL MAPA → RECIBIR LA MISIÓN → EXPLORAR EL ESCENARIO
        ↓
ENCONTRAR HALLAZGOS → IDENTIFICAR LA NO CONFORMIDAD
        ↓
APRENDER LA REGLA AMBIENTAL → PROPONER LA ACCIÓN CORRECTIVA
        ↓
RESOLVER EL MINI-RETO → FICHA DE AUDITORÍA → XP, ESTRELLAS E INSIGNIAS
        ↓
DESBLOQUEAR LA SIGUIENTE ZONA
```

## Qué trae instalado

| Contenido | Cantidad |
|---|---|
| Zonas del mapa | 6 (Casa, Escuela, Parque, Río, Ciudad, Zona industrial) |
| Misiones de auditoría | 6 |
| Situaciones interactivas | 42 (30 problemas + 12 situaciones correctas) |
| Acciones correctivas | 90 (3 por problema, con explicación cada una) |
| Mini-retos interactivos | 21 |
| Reglas de la biblioteca ambiental | 22 |
| Referencias a normas peruanas reales | 14 |
| Insignias | 12 |
| Elementos de colección | 26 |
| Avatares | 8 |
| Ilustraciones vectoriales propias | 47 iconos + 6 escenarios + mascota + logotipo |

Toda la ilustración está **dibujada con Compose Canvas**: no hay imágenes
externas, el arte escala a cualquier pantalla y el APK pesa poco.

## Categorías de no conformidad

♻️ Residuos · 💧 Agua · 🌬️ Aire · 🔊 Ruido · 🌳 Áreas verdes ·
🐾 Biodiversidad · ⚡ Energía · 🌎 Contaminación

Cada categoría tiene icono, color y explicación propios. El estado nunca se
comunica solo con color: siempre hay icono y texto.

## Progresión

| Rango | XP | Qué aprende |
|---|---|---|
| 🌱 Explorador | 0 – 599 | Observar con atención |
| 🔍 Detective | 600 – 1599 | Encontrar y clasificar problemas |
| 🛡️ EcoGuardián | 1600+ | Analizar, relacionar reglas y solucionar |

## Cómo se calcula el puntaje

El motor de auditoría (`MotorAuditoria`) es Kotlin puro y está cubierto por
tests. La ficha nunca se simula:

```
puntaje = 40 · (problemas detectados / problemas reales)
        + 25 · (clasificaciones correctas / detectados)
        + 30 · (acciones acertadas / problemas reales)
        +  5 · (situaciones correctas respetadas / situaciones correctas)
        − 5 por cada aviso equivocado (hasta un máximo de 15)
```

⭐ ≥ 50 · ⭐⭐ ≥ 70 · ⭐⭐⭐ ≥ 90

## Privacidad infantil

- No se pide nombre real, correo, teléfono, dirección, ubicación ni contactos.
- Solo un apodo inventado y un avatar, guardados en el dispositivo.
- **El permiso `INTERNET` no se declara en el manifiesto.**
- Sin publicidad, sin analítica, sin rankings, sin compras, sin vidas limitadas.

## Compilar

Requisitos: **JDK 17**, Android SDK con **API 35** y build-tools **35.0.0**.

```bash
./gradlew clean
./gradlew testDebugUnitTest
./gradlew lintDebug
./gradlew assembleDebug
```

El APK queda en `app/build/outputs/apk/debug/app-debug.apk`.

En Windows, si tu carpeta de usuario tiene tildes o eñes, revisa la nota del
final de [docs/BUILD_REPORT.md](docs/BUILD_REPORT.md): hace falta apuntar
`GRADLE_USER_HOME`, `TMP` y `TEMP` a rutas sin acentos.

## Compilación automática

`.github/workflows/build-apk.yml` ejecuta en Ubuntu, con JDK 17 y el Gradle
Wrapper: pruebas → lint → APK, y publica el APK y los informes como *artifacts*.

## Estructura

```
app/
  src/main/java/pe/ecoguardianes/
    domain/     modelo y motor de auditoría (Kotlin puro, sin Android)
    data/       catálogo educativo + Room + repositorio
    ui/         tema, arte en Canvas, componentes, pantallas y navegación
  src/test/     102 pruebas unitarias
database/       schema.sql y sample_data.sql
docs/           memoria descriptiva y manuales (Markdown y PDF)
deliverables/   APK, código fuente comprimido y PDF
.github/        flujo de trabajo de GitHub Actions
```

## Arquitectura

Kotlin · Jetpack Compose · Material 3 (solo como base técnica; la identidad
visual es propia) · Navigation Compose · MVVM + Repository · Room/SQLite ·
Coroutines y StateFlow · Gradle Kotlin DSL · JDK 17 · `minSdk 24` /
`targetSdk 35`.

## Documentación

- [Memoria descriptiva](docs/MEMORIA_DESCRIPTIVA.md)
- [Manual de usuario](docs/MANUAL_USUARIO.md)
- [Manual técnico](docs/MANUAL_TECNICO.md)
- [Base de datos](docs/BASE_DE_DATOS.md)
- [Informe de compilación](docs/BUILD_REPORT.md)

## Aviso

La información legal que aparece en la aplicación es **apoyo educativo, no
asesoría jurídica**. Las normas peruanas se citan por su nombre oficial y su
finalidad general; no se reproducen artículos ni se atribuyen obligaciones
concretas a personas o empresas.
