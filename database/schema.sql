-- ===========================================================================
-- EcoGuardianes - Esquema de la base de datos local (SQLite / Room)
--
-- Este archivo reproduce el esquema que Room genera realmente en el
-- dispositivo. La fuente de verdad es:
--     app/schemas/pe.ecoguardianes.data.local.EcoDatabase/1.json
--
-- Base de datos : ecoguardianes.db
-- Versión       : 1
-- Identity hash : 0a367d4bf90f6b8be2bfc11f032454fa
-- Codificación  : UTF-8
--
-- Todo el contenido educativo (escenarios, reglas, acciones, retos, insignias
-- y coleccionables) vive en el código como catálogo inmutable. En la base de
-- datos se guarda únicamente lo que el jugador consigue, que es lo que cambia.
-- ===========================================================================

PRAGMA foreign_keys = ON;

-- ---------------------------------------------------------------------------
-- perfil
-- Fila única (id = 1) con la identidad local del jugador y sus preferencias.
-- No contiene ningún dato personal: solo un apodo inventado y un avatar.
-- ---------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `perfil` (
    `id`                   INTEGER NOT NULL,
    `alias`                TEXT    NOT NULL,
    `avatarId`             TEXT    NOT NULL,
    `xp`                   INTEGER NOT NULL,
    `onboardingCompletado` INTEGER NOT NULL,
    `sonidoActivado`       INTEGER NOT NULL,
    `hapticaActivada`      INTEGER NOT NULL,
    `textoGrande`          INTEGER NOT NULL,
    `pistasAutomaticas`    INTEGER NOT NULL,
    `creadoEn`             INTEGER NOT NULL,
    PRIMARY KEY(`id`)
);

-- ---------------------------------------------------------------------------
-- progreso_zona
-- Mejor resultado del jugador en cada una de las seis zonas del mapa.
-- zonaId toma los valores del enumerado ZonaId:
--   CASA, ESCUELA, PARQUE, RIO, CIUDAD, ZONA_INDUSTRIAL
-- ---------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `progreso_zona` (
    `zonaId`          TEXT    NOT NULL,
    `estrellas`       INTEGER NOT NULL,
    `mejorPuntaje`    INTEGER NOT NULL,
    `vecesCompletada` INTEGER NOT NULL,
    `iniciada`        INTEGER NOT NULL,
    `actualizadoEn`   INTEGER NOT NULL,
    PRIMARY KEY(`zonaId`)
);

-- ---------------------------------------------------------------------------
-- auditoria
-- Una fila por cada ficha de auditoría cerrada. Todos los contadores los
-- calcula el motor de auditoría (MotorAuditoria) a partir de las decisiones
-- reales del jugador; ninguno se escribe a mano.
-- ---------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `auditoria` (
    `id`                      INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    `misionId`                TEXT    NOT NULL,
    `zonaId`                  TEXT    NOT NULL,
    `iniciadaEn`              INTEGER NOT NULL,
    `finalizadaEn`            INTEGER NOT NULL,
    `totalSituaciones`        INTEGER NOT NULL,
    `problemasTotales`        INTEGER NOT NULL,
    `detectadosCorrectos`     INTEGER NOT NULL,
    `falsosPositivos`         INTEGER NOT NULL,
    `omitidos`                INTEGER NOT NULL,
    `conformes`               INTEGER NOT NULL,
    `observaciones`           INTEGER NOT NULL,
    `noConformidades`         INTEGER NOT NULL,
    `clasificacionesCorrectas` INTEGER NOT NULL,
    `accionesCorrectas`       INTEGER NOT NULL,
    `accionesPropuestas`      INTEGER NOT NULL,
    `puntaje`                 INTEGER NOT NULL,
    `estrellas`               INTEGER NOT NULL,
    `xpGanado`                INTEGER NOT NULL,
    `aprobada`                INTEGER NOT NULL,
    `deteccionPerfecta`       INTEGER NOT NULL,
    `completada`              INTEGER NOT NULL
);

CREATE INDEX IF NOT EXISTS `index_auditoria_zonaId`   ON `auditoria` (`zonaId`);
CREATE INDEX IF NOT EXISTS `index_auditoria_misionId` ON `auditoria` (`misionId`);

-- ---------------------------------------------------------------------------
-- hallazgo
-- Cada situación que el jugador registró en su acta. Se guardan también los
-- avisos equivocados (valido = 0), porque forman parte de la evidencia de la
-- auditoría y explican el puntaje final.
--
--   categoria / categoriaElegida -> RESIDUOS, AGUA, AIRE, RUIDO,
--                                   AREAS_VERDES, BIODIVERSIDAD, ENERGIA,
--                                   CONTAMINACION
--   gravedad  / gravedadElegida  -> CONFORME, OBSERVACION, NO_CONFORMIDAD
--   estado                       -> DETECTADO, CLASIFICADO, RESUELTO,
--                                   DESCARTADO
-- ---------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `hallazgo` (
    `id`               INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    `auditoriaId`      INTEGER NOT NULL,
    `situacionId`      TEXT    NOT NULL,
    `zonaId`           TEXT    NOT NULL,
    `nombre`           TEXT    NOT NULL,
    `categoria`        TEXT    NOT NULL,
    `gravedad`         TEXT    NOT NULL,
    `categoriaElegida` TEXT,
    `gravedadElegida`  TEXT,
    `descripcion`      TEXT    NOT NULL,
    `reglaId`          TEXT    NOT NULL,
    `accionId`         TEXT,
    `accionCorrecta`   INTEGER NOT NULL,
    `retoSuperado`     INTEGER NOT NULL,
    `intentos`         INTEGER NOT NULL,
    `valido`           INTEGER NOT NULL,
    `estado`           TEXT    NOT NULL,
    `registradoEn`     INTEGER NOT NULL,
    FOREIGN KEY(`auditoriaId`) REFERENCES `auditoria`(`id`)
        ON UPDATE NO ACTION ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS `index_hallazgo_auditoriaId` ON `hallazgo` (`auditoriaId`);
CREATE INDEX IF NOT EXISTS `index_hallazgo_categoria`   ON `hallazgo` (`categoria`);
CREATE INDEX IF NOT EXISTS `index_hallazgo_situacionId` ON `hallazgo` (`situacionId`);

-- ---------------------------------------------------------------------------
-- insignia
-- Estado de desbloqueo de cada insignia del catálogo. progresoActual y meta
-- se recalculan tras cada auditoría, de modo que la barra que ve el niño
-- refleja siempre datos reales.
-- ---------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `insignia` (
    `id`             TEXT    NOT NULL,
    `desbloqueada`   INTEGER NOT NULL,
    `desbloqueadaEn` INTEGER,
    `progresoActual` INTEGER NOT NULL,
    `meta`           INTEGER NOT NULL,
    PRIMARY KEY(`id`)
);

-- ---------------------------------------------------------------------------
-- coleccionable
-- Igual que la tabla anterior, pero para la colección ambiental.
-- ---------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `coleccionable` (
    `id`             TEXT    NOT NULL,
    `desbloqueado`   INTEGER NOT NULL,
    `desbloqueadoEn` INTEGER,
    `progresoActual` INTEGER NOT NULL,
    `meta`           INTEGER NOT NULL,
    PRIMARY KEY(`id`)
);

-- ---------------------------------------------------------------------------
-- Consultas de referencia que usa la aplicación
-- ---------------------------------------------------------------------------

-- Hallazgos válidos agrupados por categoría (alimenta las insignias):
--   SELECT categoria AS categoria, COUNT(*) AS total
--   FROM hallazgo WHERE valido = 1 GROUP BY categoria;

-- Mejor puntaje conseguido:
--   SELECT COALESCE(MAX(puntaje), 0) FROM auditoria WHERE completada = 1;

-- Auditorías con detección perfecta (insignia Súper Detective):
--   SELECT COUNT(*) FROM auditoria
--   WHERE completada = 1 AND deteccionPerfecta = 1;
