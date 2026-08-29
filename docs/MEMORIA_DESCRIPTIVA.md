# EcoGuardianes — Memoria descriptiva

**Simulador educativo de auditoría ambiental para niños de 8 a 12 años**
Versión 1.0.0 · Aplicación Android nativa · Funcionamiento sin conexión

---

## 1. Presentación

EcoGuardianes convierte al niño en un pequeño auditor ambiental. Recibe misiones
para investigar seis lugares distintos, encuentra situaciones que no cumplen una
regla ambiental, aprende por qué importan y propone —y aplica— la acción que las
corrige.

El lema **«Detecta, aprende y protege»** resume las tres capas del producto:

- **Detecta:** una mecánica de observación e investigación sobre escenarios
  ilustrados e interactivos.
- **Aprende:** una biblioteca ambiental con reglas explicadas en lenguaje
  sencillo y referencias a normas peruanas reales.
- **Protege:** acciones correctivas que modifican de verdad el estado de la
  misión y el progreso del jugador.

## 2. Problema que resuelve

La educación ambiental infantil suele quedarse en dos extremos: la charla teórica
o el cuestionario. Ninguno enseña el paso más difícil, que es **mirar un lugar
real y reconocer qué está mal**.

EcoGuardianes traslada al lenguaje infantil el método de una auditoría ambiental:
observar, registrar el hallazgo, clasificarlo, relacionarlo con una regla,
proponer una acción correctiva y medir el resultado. El niño no memoriza: decide,
se equivoca, recibe una explicación y vuelve a intentarlo.

## 3. Público objetivo

Niños de 8 a 12 años. El diseño evita deliberadamente la estética preescolar y el
lenguaje infantilizado: colores saturados pero maduros, tipografía grande y
contrastada, frases cortas, iconografía de «equipo de campo» (lupa, portapapeles,
casco, mochila). El niño debe sentirse **guardián del planeta**, no alumno
rellenando una ficha.

## 4. Objetivos

### 4.1 Objetivo general

Desarrollar hábitos de cuidado ambiental mediante la práctica repetida de un
ciclo completo de auditoría, en una experiencia interactiva y sin conexión.

### 4.2 Objetivos específicos

1. Enseñar a distinguir una situación correcta de una no conformidad.
2. Introducir ocho categorías ambientales y sus consecuencias cotidianas.
3. Relacionar cada problema con una regla ambiental comprensible.
4. Ejercitar la propuesta y la aplicación de acciones correctivas.
5. Presentar, con rigor y sin exagerar, la existencia de normas ambientales
   peruanas reales.
6. Sostener la motivación con progresión, recompensas y coleccionables ligados a
   logros verificables.

## 5. Fundamento pedagógico

La dificultad crece en tres etapas, alineadas con los rangos del juego:

| Etapa | Rango | Qué se pide | Zonas |
|---|---|---|---|
| Inicial | 🌱 Explorador | Observar y encontrar | Casa, Escuela |
| Media | 🔍 Detective | Clasificar y decidir | Parque, Río |
| Avanzada | 🛡️ EcoGuardián | Analizar, relacionar y solucionar | Ciudad, Zona industrial |

Cada zona mezcla problemas y situaciones bien resueltas. Esto es clave: obliga a
**mirar antes de acusar**. Marcar como problema algo que estaba bien resta
precisión en la ficha final, igual que en una auditoría de verdad.

El concepto técnico de *no conformidad* se traduce así para el niño:

> «Una situación que no cumple una regla ambiental o que puede causar un problema
> para el ambiente.»

### 5.1 Feedback

Ninguna respuesta se contesta con un simple «correcto» o «incorrecto»:

- **Acierto:** animación breve, explicación del porqué, recompensa y avance.
- **Error:** ECO explica en qué falla el razonamiento, ofrece una pista y deja
  reintentar. Al segundo fallo lo resuelve con el niño y la partida continúa,
  para que nunca se atasque.
- **Aviso equivocado:** ECO explica por qué esa situación sí cumplía la regla y
  ofrece la **goma del auditor**: una única corrección por misión para retirar el
  hallazgo del acta sin penalización.

## 6. Identidad

### 6.1 ECO

ECO es un pequeño guardián con brotes en la cabeza y capa azul, dibujado
íntegramente con Compose Canvas. Tiene cinco estados de ánimo (normal, feliz,
pensativo, alerta y celebración) que cambian según lo que ocurre. Sus diálogos
son siempre de una o dos frases y aparece solo cuando aporta algo.

### 6.2 Paleta

Inspirada en vegetación, agua, tierra y energía limpia:

| Familia | Uso |
|---|---|
| Verdes (`#14573C`, `#2E9E5B`, `#6FCF7F`) | Naturaleza, aciertos, primario |
| Azules (`#0F4C75`, `#1E88C7`, `#6FB3D9`) | Agua, cielo, secundario |
| Tierra y arena (`#6B4A2F`, `#E9D5AC`) | Suelo, madera, superficies |
| Sol y fuego (`#F2B705`, `#E8722B`) | Energía, XP, recompensas |
| Coral y ámbar (`#D1495B`, `#E8A020`) | No conformidad y observación |

Material 3 se usa como base técnica, pero el esquema de color, la tipografía, las
formas y todo el arte son propios.

### 6.3 Arte

47 iconos ambientales, 6 escenarios completos, la mascota, el logotipo y el icono
adaptativo de la aplicación están dibujados con primitivas de Canvas en
coordenadas normalizadas. No hay ningún archivo de imagen en el proyecto: el arte
escala sin pérdida y el APK se mantiene ligero.

## 7. Mecánica

### 7.1 Ciclo principal

```
OBSERVAR → INVESTIGAR → DETECTAR → IDENTIFICAR LA NO CONFORMIDAD
        → RELACIONAR CON UNA REGLA → PROPONER UNA ACCIÓN CORRECTIVA
        → RESOLVER EL MINI-RETO → FEEDBACK → RECOMPENSA → DESBLOQUEO
```

### 7.2 Dentro del escenario

El escenario es una ilustración viva con objetos marcados. Cada objeto muestra su
estado con un símbolo, no solo con color: `?` sin revisar, `·` revisado, `!`
registrado como hallazgo, `✓` resuelto.

Al tocar un objeto se abre la inspección: qué se ve, la posibilidad de pedir una
pista a ECO y dos decisiones: **«Está correcto»** o **«Registrar hallazgo»**.

### 7.3 Los cinco pasos de un hallazgo

1. **Observar** — descripción de lo que se ve y pista opcional.
2. **Clasificar** — elegir categoría (entre 8) y gravedad (entre 3).
3. **La regla** — ficha de la regla ambiental relacionada, con ejemplo, acción
   correcta y, cuando corresponde, la referencia normativa peruana.
4. **Acción correctiva** — tres opciones plausibles; cada una explica por qué
   funciona o por qué no.
5. **Mini-reto** — aplicar la acción de verdad.

### 7.4 Mini-retos

21 retos de siete tipos. Los de clasificación y colocación se resuelven
**arrastrando y soltando**; los de selección, conexión y orden, tocando la pieza y
después su destino (más preciso en pantallas pequeñas y más accesible).

| Tipo | Ejemplo |
|---|---|
| Clasificar | Separar la bolsa de la cocina en tres recipientes |
| Arrastrar | Retirar del río lo que no pertenece al agua |
| Interruptor | Apagar lo que sobra sin apagar la luz de emergencia |
| Conectar | Unir cada fuente de humo con su solución |
| Ordenar | Ordenar los pasos para recuperar el césped pisoteado |
| Selección | Elegir todo lo que de verdad baja el ruido |
| Verificación | Completar la lista de comprobación |

## 8. Contenido

### 8.1 Escenarios

| Zona | Situaciones | Problemas | Correctas | XP para abrirla |
|---|---|---|---|---|
| 🏠 Casa | 7 | 5 | 2 | 0 |
| 🏫 Escuela | 7 | 5 | 2 | 120 |
| 🌳 Parque | 7 | 5 | 2 | 320 |
| 🌊 Río | 7 | 5 | 2 | 600 |
| 🏙️ Ciudad | 7 | 5 | 2 | 950 |
| 🏭 Zona industrial | 7 | 5 | 2 | 1400 |

Una zona se abre cuando el jugador alcanza la experiencia mínima **y** ha
completado la zona anterior con al menos una estrella.

### 8.2 Biblioteca ambiental

22 reglas repartidas entre las ocho categorías (al menos dos por categoría). Cada
ficha tiene: regla sencilla, por qué importa, ejemplo cotidiano, qué hacer y, en
14 casos, una referencia normativa peruana real.

### 8.3 Tratamiento de la normativa

La aplicación distingue de forma explícita dos planos, con estilos visuales
distintos:

- **Regla EcoGuardián** — redactada para el niño.
- **Referencia normativa del Perú** — nombre oficial de la norma, descripción
  general de su finalidad y entidad que la emite.

Normas citadas: Ley N.° 28611 (General del Ambiente), Decreto Legislativo
N.° 1278 (Gestión Integral de Residuos Sólidos), Ley N.° 27972 (Orgánica de
Municipalidades), Ley N.° 29419 (recicladores), Ley N.° 30884 (plástico de un
solo uso), Ley N.° 29338 (Recursos Hídricos), D.S. N.° 004-2017-MINAM (ECA Agua),
D.S. N.° 003-2017-MINAM (ECA Aire), D.S. N.° 085-2003-PCM (ECA Ruido), Ley
N.° 26834 (Áreas Naturales Protegidas), Ley N.° 29763 (Forestal y de Fauna
Silvestre), Ley N.° 27345 (uso eficiente de la energía), Ley N.° 30754 (Marco
sobre Cambio Climático) y Ley N.° 27446 (SEIA).

**No se citan artículos, no se inventan normas y no se atribuyen obligaciones
legales concretas.** Cada ficha lleva el aviso «Esto es material educativo, no
asesoría legal».

## 9. Ficha de auditoría

Al cerrar la misión, el motor calcula la ficha con las decisiones registradas:

```
puntaje = 40 · detección + 25 · clasificación + 30 · corrección + 5 · precisión
          − 5 por cada aviso equivocado (máximo −15)
```

- **Detección:** problemas encontrados sobre problemas reales.
- **Clasificación:** categoría y gravedad correctas sobre lo detectado.
- **Corrección:** acciones acertadas sobre problemas reales.
- **Precisión:** situaciones correctas que el niño respetó.

Estrellas: ⭐ desde 50, ⭐⭐ desde 70, ⭐⭐⭐ desde 90.

XP = experiencia de cada hallazgo + 5 por categoría acertada + 5 por gravedad
acertada + 15 por acción correcta + 25 por estrella.

El resultado se muestra con un medidor circular animado, una barra segmentada de
conformes / observaciones / no conformidades, cifras destacadas y el acta
detallada, que se **relee desde la base de datos** para demostrar que se guardó.

## 10. Recompensas

12 insignias, todas con requisito medible y verificable:

| Insignia | Requisito |
|---|---|
| 🌱 Guardián de la Naturaleza | 6 hallazgos de áreas verdes |
| 💧 Protector del Agua | 6 hallazgos de agua |
| ♻️ Maestro de los Residuos | 8 hallazgos de residuos |
| 🌳 Defensor de los Bosques | 2 estrellas en el Parque |
| 🐾 Amigo de la Biodiversidad | 5 hallazgos de biodiversidad |
| ⚡ Guardián de la Energía | 5 hallazgos de energía |
| 🔍 Súper Detective | Una auditoría con detección perfecta |
| 🌎 EcoGuardián Supremo | Las 6 zonas completadas |
| 🏅 Auditoría Impecable | 95 % o más en una auditoría |
| 🛠️ Manos a la Obra | 20 acciones correctivas acertadas |
| 📋 Guardián Constante | 8 auditorías completadas |
| 🌬️ Centinela del Aire | 4 hallazgos de aire |

La colección ambiental tiene 26 piezas (fauna, flora, reciclables, herramientas y
descubrimientos), cada una con su dato curioso y su condición real de desbloqueo.
Mientras está bloqueada, el niño ve **qué le falta exactamente** y una barra de
progreso honesta.

No hay rankings online, ni presión social, ni compras, ni anuncios, ni vidas
limitadas, ni castigos.

## 11. Accesibilidad

- Tipografía grande de base y opción de **texto grande** (escala 1,18).
- Contraste alto en todas las superficies; tema claro y oscuro.
- Zonas táctiles amplias (objetos de 54 dp, botones de 46 dp o más).
- Ningún estado se comunica solo con color: siempre hay icono, símbolo o texto.
- Descripciones de contenido en iconos, objetos del escenario, insignias, mapa y
  barras de resultados.
- Sonido y vibración desactivables.

## 12. Privacidad infantil

- No se solicita nombre real, correo, teléfono, dirección, ubicación ni contactos.
- El perfil es un apodo inventado (máximo 16 caracteres) y un avatar local.
- **El permiso `INTERNET` no está declarado en el manifiesto**, así que la
  aplicación no puede enviar nada aunque quisiera.
- Sin publicidad, analítica ni seguimiento.
- La pantalla de configuración explica esto con palabras del niño y permite
  borrar todo el progreso.

## 13. Alcance y simplificaciones

Todo lo especificado está implementado y es funcional. Se documentan tres
decisiones de diseño deliberadas:

1. **Contenido educativo como catálogo en código.** Escenarios, reglas, acciones,
   retos, insignias y coleccionables son objetos Kotlin inmutables; la base de
   datos guarda todo lo que cambia (perfil, progreso, auditorías, hallazgos,
   insignias y colección). El catálogo es contenido, no estado, y así queda
   cubierto por los tests sin necesidad de sembrar la base en cada arranque.
2. **Arrastrar y soltar en dos de los siete tipos de reto.** Se usa donde aporta
   (clasificar y colocar). En orden, conexión y selección, tocar es más preciso
   con dedos pequeños y funciona mejor con lectores de pantalla.
3. **Sonido.** La interfaz de configuración y las preferencias de sonido están
   implementadas y persistidas; no se incluyen archivos de audio, de modo que la
   aplicación nunca reproduce sonidos fuertes automáticamente y el APK no carga
   recursos multimedia.

## 14. Estado de verificación

Comprobado en el equipo de desarrollo con evidencia real (ver
`docs/BUILD_REPORT.md`): 102 pruebas unitarias en verde, lint sin errores y APK
de depuración generado. El flujo de GitHub Actions repite los tres pasos en
Ubuntu con JDK 17 y publica el APK como *artifact*.
