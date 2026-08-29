# EcoGuardianes — Informe de compilación

**Estado: COMPILACIÓN VERIFICADA.**
Los cuatro comandos se ejecutaron realmente en el equipo de desarrollo y los
resultados de esta página proceden de los informes generados por Gradle, no de
estimaciones.

---

## 1. Entorno

| Elemento | Valor |
|---|---|
| Fecha de la ejecución | 2026-08-29, 01:56–01:59 UTC |
| Sistema operativo | Windows 11 Pro (10.0.26200), x64 |
| JDK | Temurin OpenJDK **17.0.20.1+1** |
| Gradle | **8.9** (Gradle Wrapper del proyecto) |
| Android Gradle Plugin | **8.7.3** |
| Kotlin | **2.0.21** · KSP 2.0.21-1.0.28 |
| SDK de Android | `compileSdk 35`, `build-tools 35.0.0` |
| Variante | `debug` |

## 2. Comandos ejecutados

```bash
./gradlew clean
./gradlew testDebugUnitTest
./gradlew lintDebug
./gradlew assembleDebug
```

| Paso | Resultado | Duración |
|---|---|---|
| `clean` | **BUILD SUCCESSFUL** | 22 s |
| `testDebugUnitTest` | **BUILD SUCCESSFUL** | 51 s |
| `lintDebug` | **BUILD SUCCESSFUL** | 1 min 45 s |
| `assembleDebug` | **BUILD SUCCESSFUL** | 7 s |

La secuencia partió de un `clean` real: no hay resultados heredados de
compilaciones anteriores.

## 3. Pruebas unitarias

**102 pruebas ejecutadas · 102 aprobadas · 0 fallidas · 0 omitidas · 10,8 s**

| Clase de prueba | Pruebas | Fallos |
|---|---|---|
| `domain.MotorAuditoriaTest` | 16 | 0 |
| `domain.CasosLimiteTest` | 15 | 0 |
| `domain.RecompensasTest` | 12 | 0 |
| `domain.ProgresionTest` | 6 | 0 |
| `data.CatalogoTest` | 23 | 0 |
| `data.RepositorioRoomTest` | 17 | 0 |
| `data.FlujoAuditoriaTest` | 13 | 0 |
| **Total** | **102** | **0** |

Informes generados:

- `app/build/reports/tests/testDebugUnitTest/index.html`
- `app/build/test-results/testDebugUnitTest/*.xml`

Cobertura funcional: motor de auditoría y fórmula del puntaje, progresión de
niveles y rangos, insignias y coleccionables, apertura de zonas, integridad y
codificación del catálogo educativo, persistencia real con Room en memoria
(Robolectric) y el ciclo completo de una misión a través del ViewModel.

## 4. Lint

**BUILD SUCCESSFUL · 0 errores · 48 advertencias**

| Regla | Severidad | Nº |
|---|---|---|
| `GradleDependency` | Warning | 45 |
| `AndroidGradlePluginVersion` | Warning | 3 |

Las 48 advertencias son avisos de que existen versiones más recientes de las
dependencias y del plugin de Android. Las versiones están fijadas
deliberadamente en `gradle/libs.versions.toml` para que la compilación sea
reproducible. **No hay advertencias de corrección, accesibilidad, rendimiento,
seguridad ni usabilidad.**

Informes: `app/build/reports/lint-results-debug.html` y `lint-results-debug.xml`.

## 5. APK generado

| Dato | Valor |
|---|---|
| Ruta | `app/build/outputs/apk/debug/app-debug.apk` |
| Tamaño | **10 770 362 bytes** (10,27 MB) |
| SHA-256 | `1854d5eb8ec4d2d4f48bac2fdb433d8dc8c6bfd8c942a3666970bc4b6bfeed59` |
| Generado | 2026-08-29 01:58:50 UTC |
| `applicationId` | `pe.ecoguardianes` |
| `versionName` / `versionCode` | 1.0.0 / 1 |
| `minSdk` / `targetSdk` | 24 / 35 |
| Firma | Clave de depuración de Android |

El tamaño bajó de 16,83 MB a 10,27 MB al retirar `material-icons-extended`, del
que solo se usaba un icono ya incluido en `material-icons-core`.

Verificación del hash:

```bash
sha256sum app/build/outputs/apk/debug/app-debug.apk
```

## 6. Base de datos

El esquema de Room se exporta a
`app/schemas/pe.ecoguardianes.data.local.EcoDatabase/1.json`
(versión 1, *identity hash* `0a367d4bf90f6b8be2bfc11f032454fa`).

`database/schema.sql` y `database/sample_data.sql` se comprobaron ejecutándolos
contra SQLite:

```
tablas creadas ......... auditoria, coleccionable, hallazgo, insignia,
                         perfil, progreso_zona
hallazgos por categoría  AGUA 2 · CONTAMINACION 1 · ENERGIA 3 ·
                         RESIDUOS 2 · RUIDO 1   (9 válidos)
SUM(xpGanado) .......... 514  (coincide con perfil.xp)
MAX(puntaje) ........... 100
acciones correctas ..... 8
insignias desbloqueadas  2 de 12
colección .............. 6 de 26
borrado en cascada ..... al eliminar la auditoría 2 desaparecen sus 5 hallazgos
```

## 7. Documentos generados

| Documento | Markdown | PDF |
|---|---|---|
| Memoria descriptiva | `docs/MEMORIA_DESCRIPTIVA.md` | `docs/pdf/MEMORIA_DESCRIPTIVA.pdf` |
| Manual de usuario | `docs/MANUAL_USUARIO.md` | `docs/pdf/MANUAL_USUARIO.pdf` |
| Manual técnico | `docs/MANUAL_TECNICO.md` | `docs/pdf/MANUAL_TECNICO.pdf` |
| Base de datos | `docs/BASE_DE_DATOS.md` | — |
| Informe de compilación | `docs/BUILD_REPORT.md` | — |

Los PDF se generaron con ReportLab a partir del Markdown, conservando la
codificación UTF-8.

## 8. Integración continua

`.github/workflows/build-apk.yml` repite en Ubuntu la misma secuencia con JDK 17
y el Gradle Wrapper: pruebas → lint → APK, calcula el SHA-256 y publica el APK y
los informes como *artifacts*.

**Ejecutado y verificado.** Primera ejecución disparada por el `push` inicial:

| Dato | Valor |
|---|---|
| Repositorio | `juliobermudo16-source/EcoGuardianes` |
| Ejecución | `33228100318`, 2026-08-29 02:06 UTC |
| Resultado | **Correcto** — los 10 pasos en verde |
| Duración total | 4 min 43 s |
| Runner | `ubuntu-latest`, JDK 17 Temurin |

| Paso en CI | Resultado | Duración |
|---|---|---|
| `./gradlew testDebugUnitTest` | BUILD SUCCESSFUL | 2 min 22 s |
| `./gradlew lintDebug` | BUILD SUCCESSFUL | 42 s |
| `./gradlew assembleDebug` | BUILD SUCCESSFUL | 41 s |

APK producido en CI:

```
2f6440fbbecb1c591db6f22c14ebf71b13c95c18e0df713db26f996faa851d9d  app-debug.apk
```

*Artifacts* publicados: `EcoGuardianes-apk-debug` (APK y su SHA-256) y
`EcoGuardianes-informes` (informes de pruebas y de lint).

El SHA-256 del APK de CI no coincide con el local porque el APK de depuración se
firma con la clave de depuración de cada equipo, que es distinta en cada máquina.
El código compilado es el mismo.

La ejecución registra dos avisos informativos de GitHub, ajenos al proyecto:
`actions/setup-java@v4` está en desuso y varias acciones apuntan a Node.js 20.
No afectan al resultado.

## 9. Incidencias resueltas

### 9.1 El JDK no podía abrir sus selectores (Windows con tilde en la ruta)

**Síntoma:** cualquier invocación de Gradle terminaba con
`java.io.IOException: Unable to establish loopback connection`.

**Causa:** en Windows, el JDK crea el *pipe* interno de `Selector.open()` con
sockets AF_UNIX bajo el directorio temporal. La carpeta del usuario
(`C:\Users\Julio Andrés\…`) contiene una tilde, y Windows rechaza esa ruta al
crear el socket.

**Solución:** apuntar el directorio temporal a una ruta ASCII antes de compilar.

```bash
export TMP="C:\\ecoguardianes-tmp"
export TEMP="C:\\ecoguardianes-tmp"
```

### 9.2 Los procesos de prueba de Gradle no arrancaban

**Síntoma:** `Error: no se ha encontrado o cargado la clase principal
worker.org.gradle.process.internal.worker.GradleWorkerMain`, con el proceso de
pruebas terminando en código 1 sin ejecutar ni un test.

**Causa:** Gradle pasa el *classpath* del proceso de pruebas en un *argfile*
(`@archivo`). Ese archivo vive bajo `GRADLE_USER_HOME` y apunta a los `.jar` de
la caché, cuya ruta también contiene la tilde. El lanzador de Java no resuelve
esas rutas y el *classpath* queda vacío.

**Solución:** exponer el directorio de Gradle bajo una ruta ASCII mediante una
unión de directorios de Windows, sin duplicar los 5,3 GB de caché:

```bat
mklink /J "C:\gradle-eco" "%USERPROFILE%\.gradle"
```

```bash
export GRADLE_USER_HOME="C:\\gradle-eco"
```

Ambas incidencias son **propias de este equipo**, no del proyecto: el código, el
`gradle.properties` y el flujo de GitHub Actions no contienen nada específico de
esta máquina, y en Linux, macOS o Windows con una ruta de usuario sin acentos la
compilación funciona sin ajustes.

### 9.3 `clean` fallaba con el demonio de Gradle en marcha

`./gradlew clean` no podía borrar `app/build/intermediates/lint-cache` porque el
demonio mantenía abiertos los `.jar` de las reglas de lint. Se resuelve
ejecutando `./gradlew --stop` antes del `clean`, o encadenando
`./gradlew clean testDebugUnitTest lintDebug assembleDebug` en una sola
invocación.

### 9.4 Pruebas del ViewModel con Room

Room resuelve sus consultas en su propio *executor*, así que el reloj virtual de
`runTest` no basta para saber cuándo terminó una corrutina lanzada en
`viewModelScope`. `FlujoAuditoriaTest` incorpora el ayudante `esperarA { }`, que
alterna `advanceUntilIdle()` con esperas cortas hasta que se cumple la condición.

## 10. Reproducir esta verificación

```bash
git clone <repositorio>
cd EcoGuardianes
echo "sdk.dir=/ruta/al/Android/Sdk" > local.properties   # Linux o macOS
./gradlew --stop
./gradlew clean testDebugUnitTest lintDebug assembleDebug
sha256sum app/build/outputs/apk/debug/app-debug.apk
```

Resultado esperado: `BUILD SUCCESSFUL`, 102 pruebas aprobadas, lint sin errores y
el APK en `app/build/outputs/apk/debug/`. El SHA-256 variará entre equipos: el
APK de depuración se firma con la clave de depuración local, que es distinta en
cada máquina.
