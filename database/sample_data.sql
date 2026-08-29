-- ===========================================================================
-- EcoGuardianes - Datos de ejemplo
--
-- Retrato de una partida real: un guardián que ya completó la Casa con una
-- auditoría impecable y la Escuela con algún error. Los números NO están
-- inventados: son los que produce el motor de auditoría con esas decisiones.
--
-- Uso:
--     sqlite3 ecoguardianes.db < schema.sql
--     sqlite3 ecoguardianes.db < sample_data.sql
--
-- Las marcas de tiempo son milisegundos desde la época (hora local guardada
-- por el dispositivo). Los booleanos se guardan como 0 / 1.
-- ===========================================================================

PRAGMA foreign_keys = ON;

BEGIN TRANSACTION;

-- ---------------------------------------------------------------------------
-- Perfil (fila única). 514 XP = 316 de la Casa + 198 de la Escuela.
-- Con 514 XP el jugador está en el nivel 3 y sigue en el rango Explorador.
-- ---------------------------------------------------------------------------
INSERT INTO `perfil`
    (`id`, `alias`, `avatarId`, `xp`, `onboardingCompletado`, `sonidoActivado`,
     `hapticaActivada`, `textoGrande`, `pistasAutomaticas`, `creadoEn`)
VALUES
    (1, 'Guardiana Ari', 'AV_LUPA', 514, 1, 1, 1, 0, 1, 1756400000000);

-- ---------------------------------------------------------------------------
-- Progreso por zona
-- ---------------------------------------------------------------------------
INSERT INTO `progreso_zona`
    (`zonaId`, `estrellas`, `mejorPuntaje`, `vecesCompletada`, `iniciada`, `actualizadoEn`)
VALUES
    ('CASA',    3, 100, 1, 1, 1756400760000),
    ('ESCUELA', 1,  66, 1, 1, 1756487520000),
    ('PARQUE',  0,   0, 0, 1, 1756490000000);

-- ---------------------------------------------------------------------------
-- Auditoría 1: Casa. Detección perfecta, 100% y tres estrellas.
--   detección   40 * 5/5 = 40
--   clasificación 25 * 5/5 = 25
--   corrección  30 * 5/5 = 30
--   precisión    5 * 2/2 =  5   (no acusó a ninguna situación correcta)
--   ------------------------------ total 100, sin penalizaciones
--   XP: 116 (hallazgos) + 50 (clasificación) + 75 (acciones) + 75 (estrellas)
-- ---------------------------------------------------------------------------
INSERT INTO `auditoria`
    (`id`, `misionId`, `zonaId`, `iniciadaEn`, `finalizadaEn`, `totalSituaciones`,
     `problemasTotales`, `detectadosCorrectos`, `falsosPositivos`, `omitidos`,
     `conformes`, `observaciones`, `noConformidades`, `clasificacionesCorrectas`,
     `accionesCorrectas`, `accionesPropuestas`, `puntaje`, `estrellas`, `xpGanado`,
     `aprobada`, `deteccionPerfecta`, `completada`)
VALUES
    (1, 'M_CASA', 'CASA', 1756400400000, 1756400760000, 7,
     5, 5, 0, 0,
     2, 2, 3, 5,
     5, 5, 100, 3, 316,
     1, 1, 1);

-- ---------------------------------------------------------------------------
-- Auditoría 2: Escuela. Se le escapó el quiosco de descartables y acusó por
-- error al punto de acopio de papel, que estaba bien montado.
--   detección   40 * 4/5 = 32.0
--   clasificación 25 * 3/4 = 18.75
--   corrección  30 * 3/5 = 18.0
--   precisión    5 * 1/2 =  2.5
--   penalización        = -5.0  (un falso positivo)
--   ------------------------------ total 66, una estrella
--   XP: 93 (hallazgos) + 35 (clasificación) + 45 (acciones) + 25 (estrella)
-- ---------------------------------------------------------------------------
INSERT INTO `auditoria`
    (`id`, `misionId`, `zonaId`, `iniciadaEn`, `finalizadaEn`, `totalSituaciones`,
     `problemasTotales`, `detectadosCorrectos`, `falsosPositivos`, `omitidos`,
     `conformes`, `observaciones`, `noConformidades`, `clasificacionesCorrectas`,
     `accionesCorrectas`, `accionesPropuestas`, `puntaje`, `estrellas`, `xpGanado`,
     `aprobada`, `deteccionPerfecta`, `completada`)
VALUES
    (2, 'M_ESCUELA', 'ESCUELA', 1756487000000, 1756487520000, 7,
     5, 4, 1, 1,
     1, 1, 3, 3,
     3, 4, 66, 1, 198,
     1, 0, 1);

-- ---------------------------------------------------------------------------
-- Hallazgos de la auditoría 1 (todos válidos)
-- ---------------------------------------------------------------------------
INSERT INTO `hallazgo`
    (`auditoriaId`, `situacionId`, `zonaId`, `nombre`, `categoria`, `gravedad`,
     `categoriaElegida`, `gravedadElegida`, `descripcion`, `reglaId`, `accionId`,
     `accionCorrecta`, `retoSuperado`, `intentos`, `valido`, `estado`, `registradoEn`)
VALUES
    (1, 'S_CASA_01', 'CASA', 'Residuos mezclados en la cocina',
     'RESIDUOS', 'NO_CONFORMIDAD', 'RESIDUOS', 'NO_CONFORMIDAD',
     'Una sola bolsa con cáscaras, botellas y periódicos revueltos.',
     'R_RES_01', 'S_CASA_01_A1', 1, 1, 1, 1, 'RESUELTO', 1756400760000),

    (1, 'S_CASA_02', 'CASA', 'Caño abierto en el lavadero',
     'AGUA', 'NO_CONFORMIDAD', 'AGUA', 'NO_CONFORMIDAD',
     'El agua corre sin parar y no hay nadie usándola.',
     'R_AGU_01', 'S_CASA_02_A1', 1, 1, 1, 1, 'RESUELTO', 1756400760000),

    (1, 'S_CASA_03', 'CASA', 'Luces encendidas a plena luz del día',
     'ENERGIA', 'OBSERVACION', 'ENERGIA', 'OBSERVACION',
     'Los focos de la sala están encendidos y entra sol por la ventana.',
     'R_ENE_01', 'S_CASA_03_A1', 1, 1, 1, 1, 'RESUELTO', 1756400760000),

    (1, 'S_CASA_04', 'CASA', 'Pilas usadas en el tacho común',
     'CONTAMINACION', 'NO_CONFORMIDAD', 'CONTAMINACION', 'NO_CONFORMIDAD',
     'Tres pilas gastadas entre los residuos de la cocina.',
     'R_CON_01', 'S_CASA_04_A1', 1, 1, 2, 1, 'RESUELTO', 1756400760000),

    (1, 'S_CASA_05', 'CASA', 'Cargadores enchufados sin uso',
     'ENERGIA', 'OBSERVACION', 'ENERGIA', 'OBSERVACION',
     'Dos cargadores conectados y ningún aparato cargando.',
     'R_ENE_02', 'S_CASA_05_A1', 1, 1, 1, 1, 'RESUELTO', 1756400760000);

-- ---------------------------------------------------------------------------
-- Hallazgos de la auditoría 2
-- El último es el aviso equivocado: se guarda con valido = 0 y estado
-- DESCARTADO para que la ficha pueda explicarle al niño qué pasó.
-- ---------------------------------------------------------------------------
INSERT INTO `hallazgo`
    (`auditoriaId`, `situacionId`, `zonaId`, `nombre`, `categoria`, `gravedad`,
     `categoriaElegida`, `gravedadElegida`, `descripcion`, `reglaId`, `accionId`,
     `accionCorrecta`, `retoSuperado`, `intentos`, `valido`, `estado`, `registradoEn`)
VALUES
    (2, 'S_ESC_01', 'ESCUELA', 'Basura fuera del contenedor',
     'RESIDUOS', 'NO_CONFORMIDAD', 'RESIDUOS', 'NO_CONFORMIDAD',
     'Bolsas y envolturas alrededor del tacho del patio.',
     'R_RES_02', 'S_ESC_01_A1', 1, 1, 1, 1, 'RESUELTO', 1756487520000),

    (2, 'S_ESC_02', 'ESCUELA', 'Aula vacía con todo encendido',
     'ENERGIA', 'NO_CONFORMIDAD', 'ENERGIA', 'NO_CONFORMIDAD',
     'Luces y proyector funcionando en un aula sin nadie dentro.',
     'R_ENE_01', 'S_ESC_02_A1', 1, 1, 1, 1, 'RESUELTO', 1756487520000),

    (2, 'S_ESC_03', 'ESCUELA', 'Caño del baño goteando',
     'AGUA', 'OBSERVACION', 'AGUA', 'OBSERVACION',
     'Una gota cae cada dos segundos, todo el día.',
     'R_AGU_01', 'S_ESC_03_A1', 1, 1, 1, 1, 'RESUELTO', 1756487520000),

    (2, 'S_ESC_04', 'ESCUELA', 'Parlante a todo volumen en horario de clase',
     'RUIDO', 'NO_CONFORMIDAD', 'RUIDO', 'OBSERVACION',
     'La música se escucha desde la calle y desde todas las aulas.',
     'R_RUI_01', 'S_ESC_04_A3', 0, 0, 2, 1, 'CLASIFICADO', 1756487520000),

    (2, 'S_ESC_07', 'ESCUELA', 'Punto de acopio de papel señalizado',
     'RESIDUOS', 'CONFORME', NULL, NULL,
     'Caja rotulada, con papel limpio y seco, bajo techo.',
     'R_RES_04', NULL, 0, 0, 1, 0, 'DESCARTADO', 1756487520000);

-- ---------------------------------------------------------------------------
-- Insignias
-- progresoActual sale de contar los hallazgos válidos y las auditorías
-- guardadas arriba; solo dos cumplen ya su requisito.
-- ---------------------------------------------------------------------------
INSERT INTO `insignia` (`id`, `desbloqueada`, `desbloqueadaEn`, `progresoActual`, `meta`)
VALUES
    ('I_NATURALEZA',    0, NULL,          0,  6),
    ('I_AGUA',          0, NULL,          2,  6),
    ('I_RESIDUOS',      0, NULL,          2,  8),
    ('I_BOSQUES',       0, NULL,          0,  2),
    ('I_BIODIVERSIDAD', 0, NULL,          0,  5),
    ('I_ENERGIA',       0, NULL,          3,  5),
    ('I_DETECTIVE',     1, 1756400760000, 1,  1),
    ('I_SUPREMO',       0, NULL,          2,  6),
    ('I_IMPECABLE',     1, 1756400760000, 100, 95),
    ('I_MANOS_OBRA',    0, NULL,          8, 20),
    ('I_CONSTANTE',     0, NULL,          2,  8),
    ('I_AIRE_LIMPIO',   0, NULL,          0,  4);

-- ---------------------------------------------------------------------------
-- Colección ambiental: 6 piezas conseguidas de 26.
-- ---------------------------------------------------------------------------
INSERT INTO `coleccionable` (`id`, `desbloqueado`, `desbloqueadoEn`, `progresoActual`, `meta`)
VALUES
    ('C_PICAFLOR',     0, NULL,          0,   2),
    ('C_RANA',         0, NULL,          0,   4),
    ('C_PEZ',          0, NULL,          0,   1),
    ('C_MARIPOSA',     0, NULL,          0,   3),
    ('C_ABEJA',        0, NULL,          9,  12),
    ('C_NIDO',         0, NULL,          0,   2),
    ('C_MOLLE',        0, NULL,          0,   2),
    ('C_TOTORA',       0, NULL,          2,   3),
    ('C_CANTUTA',      0, NULL,          2,   3),
    ('C_HUERTO',       1, 1756487520000, 1,   1),
    ('C_BOTELLA_PET',  1, 1756487520000, 2,   2),
    ('C_PAPEL',        0, NULL,          2,   4),
    ('C_LATA',         0, NULL,          2,   6),
    ('C_VIDRIO',       1, 1756487520000, 8,   8),
    ('C_PILA',         0, NULL,          1,   3),
    ('C_LUPA',         1, 1756400760000, 9,   3),
    ('C_PORTAPAPELES', 1, 1756400760000, 2,   1),
    ('C_CASCO',        0, NULL,          0,   1),
    ('C_CUADERNO',     0, NULL,          2,   5),
    ('C_MOCHILA',      0, NULL,          8,  12),
    ('C_CICLO_AGUA',   0, NULL,          2,   5),
    ('C_COMPOST',      1, 1756400760000, 8,   5),
    ('C_SOLAR',        0, NULL,          3,   4),
    ('C_SILENCIO',     0, NULL,          1,   3),
    ('C_HUELLA',       0, NULL,        514, 800),
    ('C_VITRINA',      0, NULL,          6,  15);

COMMIT;

-- ---------------------------------------------------------------------------
-- Comprobaciones rápidas
-- ---------------------------------------------------------------------------
-- SELECT categoria, COUNT(*) FROM hallazgo WHERE valido = 1 GROUP BY categoria;
--   AGUA 2 | ENERGIA 3 | CONTAMINACION 1 | RESIDUOS 2 | RUIDO 1   (9 en total)
--
-- SELECT SUM(xpGanado) FROM auditoria WHERE completada = 1;   -->  514
-- SELECT MAX(puntaje)  FROM auditoria WHERE completada = 1;   -->  100
