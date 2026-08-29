# EcoGuardianes — Base de datos

Room 2.6.1 sobre SQLite · archivo `ecoguardianes.db` · versión de esquema **1**
· *identity hash* `0a367d4bf90f6b8be2bfc11f032454fa`

Esquema exportado por Room en
`app/schemas/pe.ecoguardianes.data.local.EcoDatabase/1.json` y reproducido en
`database/schema.sql`.

---

## 1. Qué se guarda y qué no

| Vive en la base de datos | Vive en el código |
|---|---|
| Perfil y preferencias | Escenarios y situaciones |
| Progreso por zona | Reglas ambientales |
| Fichas de auditoría | Acciones correctivas |
| Hallazgos registrados | Mini-retos |
| Estado de insignias | Definición de insignias |
| Estado de la colección | Definición de coleccionables |

El contenido educativo es inmutable: es **contenido**, no estado. La base de
datos guarda exactamente lo que cambia con el juego. Ninguna estadística de la
interfaz está escrita a mano: todas salen de estas tablas.

## 2. Diagrama

```
              ┌───────────────┐
              │    perfil     │   fila única (id = 1)
              │  alias, xp,   │
              │ preferencias  │
              └───────────────┘

┌────────────────────┐        ┌──────────────────────┐
│   progreso_zona    │        │      auditoria       │
│ zonaId (PK)        │        │ id (PK, autoincr.)   │
│ estrellas          │        │ misionId, zonaId     │
│ mejorPuntaje       │        │ contadores y puntaje │
│ vecesCompletada    │        └──────────┬───────────┘
└────────────────────┘                   │ 1
                                         │
                                         │ N
                              ┌──────────┴───────────┐
                              │      hallazgo        │
                              │ id (PK, autoincr.)   │
                              │ auditoriaId (FK)     │
                              │ ON DELETE CASCADE    │
                              └──────────────────────┘

┌────────────────────┐        ┌──────────────────────┐
│     insignia       │        │    coleccionable     │
│ id (PK, texto)     │        │ id (PK, texto)       │
│ desbloqueada       │        │ desbloqueado         │
│ progresoActual/meta│        │ progresoActual/meta  │
└────────────────────┘        └──────────────────────┘
```

## 3. Tablas

### 3.1 `perfil`

Fila única con `id = 1`. Identidad local del jugador.

| Columna | Tipo | Notas |
|---|---|---|
| `id` | INTEGER PK | Siempre 1 |
| `alias` | TEXT | Apodo inventado, máximo 16 caracteres |
| `avatarId` | TEXT | Id del catálogo de avatares (`AV_…`) |
| `xp` | INTEGER | Experiencia acumulada |
| `onboardingCompletado` | INTEGER | 0 / 1 |
| `sonidoActivado` | INTEGER | 0 / 1 |
| `hapticaActivada` | INTEGER | 0 / 1 |
| `textoGrande` | INTEGER | 0 / 1 |
| `pistasAutomaticas` | INTEGER | 0 / 1 |
| `creadoEn` | INTEGER | Milisegundos desde la época |

**No contiene ningún dato personal.** No hay columnas para nombre real, correo,
teléfono, dirección ni ubicación, ni existe forma de introducirlos.

### 3.2 `progreso_zona`

Una fila por zona visitada. `zonaId` toma los valores `CASA`, `ESCUELA`,
`PARQUE`, `RIO`, `CIUDAD` y `ZONA_INDUSTRIAL`.

| Columna | Tipo | Notas |
|---|---|---|
| `zonaId` | TEXT PK | Enumerado `ZonaId` |
| `estrellas` | INTEGER | 0–3, se conserva el mejor resultado |
| `mejorPuntaje` | INTEGER | 0–100, se conserva el mejor resultado |
| `vecesCompletada` | INTEGER | Se incrementa en cada cierre |
| `iniciada` | INTEGER | 0 / 1, marca la zona como «en progreso» |
| `actualizadoEn` | INTEGER | Milisegundos |

Repetir una misión **nunca empeora** lo conseguido: `estrellas` y `mejorPuntaje`
se guardan con `maxOf`.

### 3.3 `auditoria`

Una fila por ficha cerrada. Todos los contadores los produce `MotorAuditoria`.

| Columna | Significado |
|---|---|
| `misionId`, `zonaId` | Misión y zona auditadas |
| `iniciadaEn`, `finalizadaEn` | Marcas de tiempo locales |
| `totalSituaciones` | Puntos del escenario |
| `problemasTotales` | Cuántos eran problemas reales |
| `detectadosCorrectos` | Problemas encontrados |
| `falsosPositivos` | Avisos sobre situaciones correctas |
| `omitidos` | Problemas que se le escaparon |
| `conformes` | Situaciones correctas respetadas |
| `observaciones` | Aspectos por mejorar detectados |
| `noConformidades` | No conformidades detectadas |
| `clasificacionesCorrectas` | Categoría **y** gravedad acertadas |
| `accionesCorrectas` | Acciones correctivas acertadas |
| `accionesPropuestas` | Acciones propuestas en total |
| `puntaje` | 0–100 |
| `estrellas` | 0–3 |
| `xpGanado` | Experiencia otorgada |
| `aprobada` | Alcanzó el mínimo de hallazgos y 50 puntos |
| `deteccionPerfecta` | Todos los problemas y ningún falso positivo |
| `completada` | 0 / 1 |

Índices: `index_auditoria_zonaId`, `index_auditoria_misionId`.

### 3.4 `hallazgo`

Una fila por situación que el jugador registró en su acta, **incluidos los avisos
equivocados**: son parte de la evidencia y explican el puntaje.

| Columna | Significado |
|---|---|
| `auditoriaId` | FK a `auditoria(id)`, `ON DELETE CASCADE` |
| `situacionId`, `zonaId`, `nombre` | Situación auditada |
| `categoria`, `gravedad` | Valores reales del catálogo |
| `categoriaElegida`, `gravedadElegida` | Lo que respondió el niño (pueden ser NULL) |
| `descripcion` | Lo que se observaba |
| `reglaId` | Regla ambiental relacionada |
| `accionId`, `accionCorrecta` | Acción propuesta y si acertó |
| `retoSuperado` | Si completó el mini-reto |
| `intentos` | Intentos usados en la clasificación |
| `valido` | 1 si era un problema real; 0 si fue un falso positivo |
| `estado` | `DETECTADO`, `CLASIFICADO`, `RESUELTO`, `DESCARTADO` |
| `registradoEn` | Milisegundos |

Índices: `index_hallazgo_auditoriaId`, `index_hallazgo_categoria`,
`index_hallazgo_situacionId`.

`valido` es la columna que alimenta las insignias por categoría: solo cuentan los
hallazgos reales.

### 3.5 `insignia` y `coleccionable`

Misma forma en ambas: `id` de texto del catálogo, indicador de desbloqueo,
momento del desbloqueo y el par `progresoActual` / `meta` que dibuja la barra de
progreso. Se recalculan tras cada auditoría, así que lo que ve el niño siempre
corresponde a lo que ha hecho.

## 4. Consultas que usa la aplicación

Hallazgos válidos por categoría (alimenta las insignias temáticas):

```sql
SELECT categoria AS categoria, COUNT(*) AS total
FROM hallazgo
WHERE valido = 1
GROUP BY categoria;
```

Acciones correctivas acertadas:

```sql
SELECT COUNT(*) FROM hallazgo WHERE valido = 1 AND accionCorrecta = 1;
```

Auditorías completadas, detecciones perfectas y mejor puntaje:

```sql
SELECT COUNT(*) FROM auditoria WHERE completada = 1;
SELECT COUNT(*) FROM auditoria WHERE completada = 1 AND deteccionPerfecta = 1;
SELECT COALESCE(MAX(puntaje), 0) FROM auditoria WHERE completada = 1;
```

Todas se exponen como `Flow`, se combinan en `EcoRepositorio.estadoJuego` y llegan
a la interfaz mediante `collectAsStateWithLifecycle`. Cuando se guarda una
auditoría, Room invalida las consultas y el mapa, el perfil, la colección y las
insignias se actualizan solos.

## 5. Transacciones

`guardarAuditoria` se ejecuta dentro de `db.withTransaction { }` y agrupa cuatro
escrituras: insertar la ficha, insertar los hallazgos, actualizar el progreso de
la zona y sumar el XP al perfil. O se guarda todo, o no se guarda nada.

`reiniciarProgreso` hace lo mismo al borrar: vacía hallazgos, auditorías, zonas,
insignias y colección, pone el XP a cero y **conserva el alias y el avatar**,
para que el niño no tenga que crearse otra vez.

## 6. Migraciones

Versión 1, sin migraciones todavía. La base se crea con
`fallbackToDestructiveMigration()`: en una aplicación local sin cuentas ni
respaldo remoto, un cambio de esquema durante el desarrollo se resuelve
recreando la base. Al publicar la versión 2 habrá que sustituirlo por una
`Migration` explícita, y el JSON de la versión 1 exportado en `app/schemas/`
sirve exactamente para escribirla y probarla con `room-testing`.

## 7. Archivos SQL

- `database/schema.sql` — `CREATE TABLE` e índices tal y como los genera Room.
- `database/sample_data.sql` — una partida de ejemplo: dos auditorías (una
  impecable en la Casa y otra con errores en la Escuela), diez hallazgos, dos
  insignias y seis piezas de colección.

Los datos de ejemplo son **coherentes con el motor**: los 514 XP del perfil son
la suma real de los 316 de la primera auditoría y los 198 de la segunda, y cada
puntaje está desglosado en los comentarios del archivo.

```bash
sqlite3 ecoguardianes.db < database/schema.sql
sqlite3 ecoguardianes.db < database/sample_data.sql
```

## 8. Copias de seguridad y privacidad

`backup_rules.xml` y `data_extraction_rules.xml` incluyen únicamente
`ecoguardianes.db`. Como no existe cuenta ni servidor, la copia de seguridad solo
sirve para restaurar el progreso en el mismo dispositivo o al cambiar de
dispositivo mediante los mecanismos del sistema operativo. Desinstalar la
aplicación borra la base de datos.
