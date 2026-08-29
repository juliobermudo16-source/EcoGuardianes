package pe.ecoguardianes.ui.art

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import pe.ecoguardianes.domain.model.ZonaId

private val CieloDia = Color(0xFFBFE6F7)
private val CieloAlto = Color(0xFF8ECDEC)
private val CieloTarde = Color(0xFFFAD9A8)
private val CieloGris = Color(0xFFCBD5D8)
private val Sol = Color(0xFFF9D34E)
private val Nube = Color(0xFFFFFFFF)
private val Pasto = Color(0xFF4FB06A)
private val PastoOsc = Color(0xFF2F7A3E)
private val Tierra = Color(0xFF8A6039)
private val TierraOsc = Color(0xFF6B4A2F)
private val Muro = Color(0xFFF6E7C1)
private val MuroSombra = Color(0xFFE3CFA2)
private val Techo = Color(0xFFC0553F)
private val TechoOsc = Color(0xFF9B4231)
private val Ventana = Color(0xFF9BD4F0)
private val Agua = Color(0xFF3EA0D6)
private val AguaHonda = Color(0xFF1E6E9E)
private val Asfalto = Color(0xFF6C7A80)
private val AsfaltoOsc = Color(0xFF515E63)
private val Concreto = Color(0xFFB9C2C6)
private val ConcretoOsc = Color(0xFF8C979C)
private val Metal = Color(0xFF98A6AD)

/** Ilustración de fondo de cada zona, dibujada con Canvas. */
@Composable
fun FondoEscenario(zona: ZonaId, modifier: Modifier = Modifier) {
    val descripcion = "Escenario de " + zona.titulo
    Canvas(modifier.semantics { contentDescription = descripcion }) {
        dibujarEscenario(zona)
    }
}

fun DrawScope.dibujarEscenario(zona: ZonaId) {
    when (zona) {
        ZonaId.CASA -> escenaCasa()
        ZonaId.ESCUELA -> escenaEscuela()
        ZonaId.PARQUE -> escenaParque()
        ZonaId.RIO -> escenaRio()
        ZonaId.CIUDAD -> escenaCiudad()
        ZonaId.ZONA_INDUSTRIAL -> escenaIndustrial()
    }
}

private fun DrawScope.cielo(arriba: Color, abajo: Color, alto: Float = 1f) {
    drawRect(
        brush = Brush.verticalGradient(listOf(arriba, abajo)),
        topLeft = Offset.Zero,
        size = Size(size.width, size.height * alto)
    )
}

private fun DrawScope.sol(cx: Float, cy: Float, r: Float = 0.07f) {
    circuloN(cx, cy, r * 1.5f, Sol.copy(alpha = 0.25f))
    circuloN(cx, cy, r, Sol)
}

private fun DrawScope.nube(cx: Float, cy: Float, escala: Float, alpha: Float = 1f) {
    val c = Nube.copy(alpha = alpha)
    circuloN(cx, cy, 0.045f * escala, c)
    circuloN(cx + 0.05f * escala, cy - 0.015f * escala, 0.055f * escala, c)
    circuloN(cx + 0.11f * escala, cy, 0.04f * escala, c)
    cajaN(cx - 0.05f * escala, cy, 0.17f * escala, 0.045f * escala, c, 0.03f)
}

private fun DrawScope.arbolito(cx: Float, base: Float, escala: Float, oscuro: Boolean = false) {
    val copa = if (oscuro) PastoOsc else Pasto
    cajaN(cx - 0.012f * escala, base - 0.10f * escala, 0.024f * escala, 0.10f * escala, TierraOsc, 0.01f)
    circuloN(cx, base - 0.14f * escala, 0.055f * escala, copa)
    circuloN(cx - 0.04f * escala, base - 0.10f * escala, 0.04f * escala, copa)
    circuloN(cx + 0.04f * escala, base - 0.10f * escala, 0.04f * escala, copa)
}

// ------------------------------------------------------------------- CASA

private fun DrawScope.escenaCasa() {
    cielo(CieloAlto, Color(0xFFE8F4E4))
    sol(0.88f, 0.10f)
    nube(0.12f, 0.10f, 1f, 0.9f)
    nube(0.55f, 0.07f, 0.8f, 0.75f)

    // Jardín
    cajaN(0f, 0.88f, 1f, 0.12f, Pasto)
    arbolito(0.06f, 0.90f, 1.1f)
    arbolito(0.94f, 0.92f, 0.9f, oscuro = true)

    // Casa en corte (vista de casa de muñecas)
    figuraN(Techo, 0.50f, 0.05f, 0.98f, 0.24f, 0.02f, 0.24f)
    figuraN(TechoOsc, 0.50f, 0.05f, 0.98f, 0.24f, 0.50f, 0.24f)
    cajaN(0.05f, 0.24f, 0.90f, 0.66f, Muro)

    // Separaciones de habitaciones
    cajaN(0.05f, 0.47f, 0.90f, 0.025f, MuroSombra)
    cajaN(0.49f, 0.24f, 0.02f, 0.23f, MuroSombra)
    cajaN(0.28f, 0.495f, 0.02f, 0.405f, MuroSombra)
    cajaN(0.72f, 0.495f, 0.02f, 0.405f, MuroSombra)

    // Planta alta: dormitorio y sala
    cajaN(0.09f, 0.30f, 0.16f, 0.13f, Ventana, 0.02f)
    cajaN(0.55f, 0.29f, 0.22f, 0.15f, Ventana, 0.02f)
    lineaN(0.66f, 0.29f, 0.66f, 0.44f, Muro, 0.015f)
    cajaN(0.30f, 0.33f, 0.14f, 0.10f, Color(0xFFD8B98C), 0.02f) // cama

    // Planta baja: cocina (izq), lavadero (centro), servicio (der)
    cajaN(0.07f, 0.72f, 0.19f, 0.06f, Color(0xFFC9A87C), 0.01f) // mesada cocina
    cajaN(0.31f, 0.66f, 0.14f, 0.06f, Color(0xFFDCE3E6), 0.01f) // lavadero
    cajaN(0.76f, 0.60f, 0.16f, 0.30f, Color(0xFFEFE6D2), 0.02f) // zona de tachos
    cajaN(0.55f, 0.80f, 0.13f, 0.10f, Color(0xFFD8B98C), 0.02f) // mueble
    cajaN(0.06f, 0.88f, 0.88f, 0.02f, MuroSombra)

    // Puerta
    cajaN(0.46f, 0.74f, 0.09f, 0.16f, TierraOsc, 0.02f)
    circuloN(0.535f, 0.82f, 0.008f, Sol)
}

// ---------------------------------------------------------------- ESCUELA

private fun DrawScope.escenaEscuela() {
    cielo(CieloAlto, CieloDia)
    sol(0.10f, 0.09f)
    nube(0.60f, 0.09f, 1f, 0.9f)
    nube(0.86f, 0.16f, 0.7f, 0.7f)

    // Patio
    cajaN(0f, 0.80f, 1f, 0.20f, Concreto)
    cajaN(0f, 0.80f, 1f, 0.012f, ConcretoOsc)

    // Bloque escolar de dos pisos
    cajaN(0.24f, 0.22f, 0.62f, 0.58f, Muro)
    cajaN(0.22f, 0.18f, 0.66f, 0.05f, Techo, 0.01f)
    cajaN(0.24f, 0.49f, 0.62f, 0.02f, MuroSombra)

    // Ventanas del aula (fila superior)
    for (i in 0 until 4) {
        val x = 0.29f + i * 0.14f
        cajaN(x, 0.28f, 0.10f, 0.14f, Ventana, 0.015f)
        lineaN(x + 0.05f, 0.28f, x + 0.05f, 0.42f, Muro, 0.012f)
    }
    // Ventanas planta baja
    for (i in 0 until 3) {
        val x = 0.30f + i * 0.16f
        cajaN(x, 0.56f, 0.11f, 0.13f, Ventana, 0.015f)
    }
    // Puerta principal
    cajaN(0.76f, 0.58f, 0.08f, 0.22f, TierraOsc, 0.015f)

    // Asta de bandera
    lineaN(0.14f, 0.80f, 0.14f, 0.34f, Metal, 0.012f)
    figuraN(Color(0xFFD1495B), 0.15f, 0.35f, 0.24f, 0.39f, 0.15f, 0.43f)

    // Quiosco
    cajaN(0.60f, 0.70f, 0.16f, 0.10f, Color(0xFFF2B705), 0.02f)
    cajaN(0.59f, 0.67f, 0.18f, 0.04f, Techo, 0.01f)

    // Áreas verdes y arbolitos
    cajaN(0.02f, 0.72f, 0.18f, 0.08f, Pasto, 0.02f)
    arbolito(0.06f, 0.80f, 1f)
    arbolito(0.94f, 0.82f, 1.1f, oscuro = true)

    // Tacho del patio
    cajaN(0.17f, 0.72f, 0.05f, 0.08f, PastoOsc, 0.01f)
}

// ----------------------------------------------------------------- PARQUE

private fun DrawScope.escenaParque() {
    cielo(CieloAlto, Color(0xFFDDF3DC))
    sol(0.50f, 0.08f, 0.06f)
    nube(0.16f, 0.12f, 0.9f, 0.85f)
    nube(0.78f, 0.10f, 1.1f, 0.8f)

    // Colinas de fondo
    ovaloN(-0.20f, 0.34f, 0.75f, 0.36f, PastoOsc.copy(alpha = 0.55f))
    ovaloN(0.45f, 0.30f, 0.85f, 0.40f, PastoOsc.copy(alpha = 0.45f))

    // Pasto
    cajaN(0f, 0.46f, 1f, 0.54f, Pasto)
    ovaloN(0.02f, 0.62f, 0.42f, 0.10f, PastoOsc.copy(alpha = 0.35f))

    // Sendero curvo
    figuraN(Color(0xFFE0CFA6), 0.00f, 0.98f, 0.16f, 0.62f, 0.30f, 0.62f, 0.20f, 0.98f)
    figuraN(Color(0xFFE0CFA6), 0.16f, 0.66f, 0.86f, 0.58f, 0.86f, 0.66f, 0.16f, 0.74f)

    // Zona pisoteada
    ovaloN(0.24f, 0.74f, 0.22f, 0.08f, Tierra.copy(alpha = 0.75f))

    // Laguna
    ovaloN(0.62f, 0.72f, 0.36f, 0.20f, Agua)
    ovaloN(0.66f, 0.75f, 0.20f, 0.08f, AguaHonda.copy(alpha = 0.35f))

    // Árboles
    arbolito(0.10f, 0.60f, 1.5f, oscuro = true)
    arbolito(0.62f, 0.56f, 1.9f)
    arbolito(0.86f, 0.52f, 1.2f, oscuro = true)
    arbolito(0.40f, 0.54f, 1.1f)

    // Banca
    cajaN(0.30f, 0.84f, 0.14f, 0.02f, TierraOsc, 0.01f)
    cajaN(0.31f, 0.86f, 0.012f, 0.05f, TierraOsc)
    cajaN(0.425f, 0.86f, 0.012f, 0.05f, TierraOsc)

    // Estación de reciclaje
    cajaN(0.83f, 0.82f, 0.04f, 0.09f, Color(0xFFF2B705), 0.01f)
    cajaN(0.88f, 0.82f, 0.04f, 0.09f, Color(0xFF6B4A2F), 0.01f)
    cajaN(0.93f, 0.82f, 0.04f, 0.09f, Color(0xFF6E7A83), 0.01f)
}

// -------------------------------------------------------------------- RÍO

private fun DrawScope.escenaRio() {
    cielo(Color(0xFFA7DCF2), Color(0xFFD8F0E2))
    sol(0.16f, 0.10f)
    nube(0.60f, 0.08f, 1f, 0.85f)
    nube(0.86f, 0.18f, 0.7f, 0.7f)

    // Montañas
    figuraN(Color(0xFF7E9AA8), 0.00f, 0.44f, 0.22f, 0.20f, 0.44f, 0.44f)
    figuraN(Color(0xFF9CB4BF), 0.34f, 0.44f, 0.60f, 0.16f, 0.88f, 0.44f)
    figuraN(Color(0xFFE8F2F5), 0.52f, 0.26f, 0.60f, 0.16f, 0.68f, 0.26f)

    // Ribera lejana
    cajaN(0f, 0.42f, 1f, 0.14f, PastoOsc)
    // Río
    figuraN(Agua, 0.00f, 0.56f, 1.00f, 0.50f, 1.00f, 0.82f, 0.00f, 0.90f)
    figuraN(AguaHonda.copy(alpha = 0.35f), 0.00f, 0.66f, 1.00f, 0.60f, 1.00f, 0.70f, 0.00f, 0.76f)
    // Reflejos
    lineaN(0.18f, 0.62f, 0.32f, 0.61f, Nube.copy(alpha = 0.5f), 0.012f)
    lineaN(0.50f, 0.70f, 0.66f, 0.68f, Nube.copy(alpha = 0.4f), 0.012f)
    lineaN(0.74f, 0.58f, 0.88f, 0.57f, Nube.copy(alpha = 0.45f), 0.012f)

    // Ribera cercana
    figuraN(Tierra, 0.00f, 0.90f, 1.00f, 0.82f, 1.00f, 1.00f, 0.00f, 1.00f)
    cajaN(0f, 0.88f, 1f, 0.03f, PastoOsc.copy(alpha = 0.6f))

    // Piedras
    ovaloN(0.30f, 0.88f, 0.07f, 0.04f, Concreto)
    ovaloN(0.68f, 0.90f, 0.06f, 0.035f, ConcretoOsc)

    // Vegetación de ribera
    arbolito(0.90f, 0.56f, 1.4f, oscuro = true)
    arbolito(0.80f, 0.54f, 1.0f)
    arbolito(0.06f, 0.54f, 1.2f)

    // Tubería de descarga
    cajaN(0.50f, 0.44f, 0.14f, 0.035f, Metal, 0.01f)
    cajaN(0.62f, 0.44f, 0.04f, 0.10f, ConcretoOsc, 0.01f)
}

// ----------------------------------------------------------------- CIUDAD

private fun DrawScope.escenaCiudad() {
    cielo(Color(0xFFAFC9D8), CieloTarde)
    sol(0.80f, 0.12f, 0.055f)

    // Edificios de fondo
    val alturas = floatArrayOf(0.34f, 0.26f, 0.42f, 0.30f, 0.38f, 0.24f, 0.36f)
    for (i in alturas.indices) {
        val x = 0.02f + i * 0.14f
        val h = alturas[i]
        cajaN(x, 0.66f - h, 0.12f, h, if (i % 2 == 0) Concreto else ConcretoOsc, 0.005f)
        for (f in 0 until 4) {
            for (c in 0 until 2) {
                val vy = 0.70f - h + f * 0.07f
                if (vy < 0.62f) {
                    cajaN(
                        x + 0.025f + c * 0.05f, vy, 0.03f, 0.035f,
                        if ((i + f + c) % 3 == 0) Sol.copy(alpha = 0.85f) else Ventana, 0.004f
                    )
                }
            }
        }
    }

    // Vereda y avenida
    cajaN(0f, 0.66f, 1f, 0.06f, Color(0xFFD5DBDD))
    cajaN(0f, 0.72f, 1f, 0.20f, Asfalto)
    cajaN(0f, 0.92f, 1f, 0.08f, Color(0xFFD5DBDD))
    for (i in 0 until 7) {
        cajaN(0.04f + i * 0.14f, 0.815f, 0.07f, 0.012f, Color(0xFFF3EFDF), 0.005f)
    }
    cajaN(0f, 0.715f, 1f, 0.008f, AsfaltoOsc)

    // Paradero
    cajaN(0.10f, 0.56f, 0.16f, 0.02f, Metal, 0.005f)
    lineaN(0.11f, 0.58f, 0.11f, 0.66f, Metal, 0.012f)
    lineaN(0.25f, 0.58f, 0.25f, 0.66f, Metal, 0.012f)

    // Ciclovía
    cajaN(0.60f, 0.66f, 0.38f, 0.055f, Color(0xFF3E8C58))
    lineaN(0.62f, 0.688f, 0.96f, 0.688f, Color(0xFFF3EFDF), 0.008f, punteada = true)

    // Árboles de vereda
    arbolito(0.42f, 0.70f, 0.8f, oscuro = true)
    arbolito(0.90f, 0.66f, 0.7f)

    // Poste con luminaria
    lineaN(0.96f, 0.92f, 0.96f, 0.44f, Metal, 0.012f)
    cajaN(0.90f, 0.42f, 0.08f, 0.025f, Metal, 0.008f)
}

// --------------------------------------------------------- ZONA INDUSTRIAL

private fun DrawScope.escenaIndustrial() {
    cielo(CieloGris, Color(0xFFE6E0D2))
    sol(0.72f, 0.12f, 0.05f)

    // Cerros lejanos
    figuraN(Color(0xFFA9B4B8), 0.00f, 0.46f, 0.20f, 0.30f, 0.42f, 0.46f)
    figuraN(Color(0xFFBAC4C7), 0.55f, 0.46f, 0.78f, 0.28f, 1.00f, 0.46f)

    // Suelo
    cajaN(0f, 0.46f, 1f, 0.54f, Color(0xFFCBBF9F))
    cajaN(0f, 0.74f, 1f, 0.26f, Color(0xFFB6A987))

    // Nave principal
    cajaN(0.16f, 0.42f, 0.46f, 0.34f, Concreto)
    figuraN(ConcretoOsc, 0.14f, 0.42f, 0.39f, 0.32f, 0.64f, 0.42f)
    for (i in 0 until 4) {
        cajaN(0.20f + i * 0.11f, 0.52f, 0.07f, 0.08f, Ventana.copy(alpha = 0.7f), 0.006f)
    }
    cajaN(0.30f, 0.64f, 0.14f, 0.12f, ConcretoOsc, 0.008f)

    // Chimenea
    cajaN(0.22f, 0.14f, 0.07f, 0.30f, Color(0xFFD7B7A5), 0.006f)
    cajaN(0.22f, 0.20f, 0.07f, 0.03f, Color(0xFFB6796A))
    circuloN(0.26f, 0.11f, 0.05f, Color(0x99808E95))
    circuloN(0.33f, 0.07f, 0.04f, Color(0x77808E95))
    circuloN(0.40f, 0.04f, 0.03f, Color(0x55808E95))

    // Almacén con paneles solares
    cajaN(0.68f, 0.46f, 0.30f, 0.24f, Muro)
    figuraN(Color(0xFF2A4A63), 0.66f, 0.46f, 0.76f, 0.38f, 1.00f, 0.38f, 1.00f, 0.46f)
    for (i in 0 until 4) {
        lineaN(0.78f + i * 0.055f, 0.385f, 0.74f + i * 0.055f, 0.455f, Ventana.copy(alpha = 0.5f), 0.008f)
    }
    cajaN(0.80f, 0.56f, 0.10f, 0.14f, ConcretoOsc, 0.008f)

    // Tanques con área de contención
    cajaN(0.03f, 0.60f, 0.11f, 0.16f, Metal, 0.02f)
    cajaN(0.02f, 0.755f, 0.14f, 0.02f, Color(0xFF8FA0A8), 0.006f)
    circuloN(0.085f, 0.60f, 0.055f, Color(0xFFB4C0C6))

    // Bidones
    cajaN(0.56f, 0.72f, 0.05f, 0.08f, Color(0xFFB26A2E), 0.008f)
    cajaN(0.62f, 0.73f, 0.05f, 0.07f, Color(0xFF9C5A25), 0.008f)
    ovaloN(0.55f, 0.79f, 0.10f, 0.03f, Color(0x66362013))

    // Cerco y viviendas vecinas
    for (i in 0 until 14) {
        lineaN(0.02f + i * 0.075f, 0.80f, 0.02f + i * 0.075f, 0.88f, Metal, 0.006f)
    }
    lineaN(0f, 0.82f, 1f, 0.82f, Metal, 0.006f)
    lineaN(0f, 0.87f, 1f, 0.87f, Metal, 0.006f)
    cajaN(0.70f, 0.86f, 0.12f, 0.10f, Muro, 0.008f)
    figuraN(Techo, 0.68f, 0.86f, 0.76f, 0.80f, 0.84f, 0.86f)
    cajaN(0.86f, 0.88f, 0.11f, 0.08f, Muro, 0.008f)
    figuraN(Techo, 0.84f, 0.88f, 0.915f, 0.83f, 0.99f, 0.88f)
}
