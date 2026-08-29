package pe.ecoguardianes.ui.art

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Estados de ánimo de ECO, el guardián que acompaña al jugador. */
enum class AnimoEco { NORMAL, FELIZ, PENSATIVO, ALERTA, CELEBRA }

private val Cuerpo = Color(0xFF2E9E5B)
private val CuerpoOscuro = Color(0xFF14573C)
private val Vientre = Color(0xFFE3F4E7)
private val Hoja = Color(0xFF6FCF7F)
private val Capa = Color(0xFF1E88C7)
private val Ojo = Color(0xFF14312B)
private val Brillo = Color(0xFFFFFFFF)
private val Mejilla = Color(0x55E8722B)

/**
 * ECO: mascota y guía de EcoGuardianes.
 * Dibujada con Canvas para que forme parte de la identidad visual propia.
 */
@Composable
fun EcoMascota(
    modifier: Modifier = Modifier,
    tam: Dp = 96.dp,
    animo: AnimoEco = AnimoEco.NORMAL,
    animada: Boolean = true
) {
    val transicion = rememberInfiniteTransition(label = "eco")
    val flotar by transicion.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2200),
            repeatMode = RepeatMode.Reverse
        ),
        label = "flotar"
    )
    val desplazamiento = if (animada) (flotar - 0.5f) * 0.05f else 0f

    Canvas(
        modifier
            .size(tam)
            .semantics { contentDescription = "ECO, tu guía guardián" }
    ) {
        translate(top = desplazamiento * size.height) {
            dibujarEco(animo)
        }
    }
}

fun DrawScope.dibujarEco(animo: AnimoEco) {
    // Sombra suave
    ovaloN(0.24f, 0.88f, 0.52f, 0.08f, Color(0x22000000))

    // Capa de guardián
    figuraN(Capa, 0.26f, 0.44f, 0.74f, 0.44f, 0.86f, 0.84f, 0.14f, 0.84f)

    // Cuerpo
    circuloN(0.50f, 0.54f, 0.30f, Cuerpo)
    ovaloN(0.32f, 0.50f, 0.36f, 0.32f, Vientre)

    // Brotes en la cabeza
    figuraN(Hoja, 0.50f, 0.22f, 0.34f, 0.14f, 0.44f, 0.06f)
    figuraN(Hoja, 0.50f, 0.22f, 0.66f, 0.12f, 0.56f, 0.04f)
    lineaN(0.50f, 0.28f, 0.50f, 0.16f, CuerpoOscuro, 0.03f)

    // Ojos según el ánimo
    when (animo) {
        AnimoEco.FELIZ, AnimoEco.CELEBRA -> {
            arcoN(0.32f, 0.40f, 0.14f, 0.14f, 200f, 140f, Ojo, 0.045f)
            arcoN(0.54f, 0.40f, 0.14f, 0.14f, 200f, 140f, Ojo, 0.045f)
        }
        AnimoEco.PENSATIVO -> {
            circuloN(0.39f, 0.47f, 0.055f, Ojo)
            circuloN(0.61f, 0.47f, 0.055f, Ojo)
            lineaN(0.31f, 0.38f, 0.45f, 0.35f, Ojo, 0.035f)
            lineaN(0.69f, 0.38f, 0.55f, 0.35f, Ojo, 0.035f)
        }
        AnimoEco.ALERTA -> {
            circuloN(0.39f, 0.46f, 0.075f, Brillo)
            circuloN(0.61f, 0.46f, 0.075f, Brillo)
            circuloN(0.39f, 0.46f, 0.045f, Ojo)
            circuloN(0.61f, 0.46f, 0.045f, Ojo)
            lineaN(0.30f, 0.34f, 0.46f, 0.38f, Ojo, 0.035f)
            lineaN(0.70f, 0.34f, 0.54f, 0.38f, Ojo, 0.035f)
        }
        AnimoEco.NORMAL -> {
            circuloN(0.39f, 0.46f, 0.065f, Ojo)
            circuloN(0.61f, 0.46f, 0.065f, Ojo)
            circuloN(0.41f, 0.44f, 0.022f, Brillo)
            circuloN(0.63f, 0.44f, 0.022f, Brillo)
        }
    }

    // Mejillas y boca
    circuloN(0.30f, 0.56f, 0.045f, Mejilla)
    circuloN(0.70f, 0.56f, 0.045f, Mejilla)
    when (animo) {
        AnimoEco.CELEBRA -> {
            ovaloN(0.44f, 0.58f, 0.12f, 0.12f, Ojo)
        }
        AnimoEco.ALERTA -> {
            ovaloN(0.45f, 0.60f, 0.10f, 0.07f, Ojo)
        }
        else -> {
            arcoN(0.40f, 0.52f, 0.20f, 0.16f, 20f, 140f, Ojo, 0.035f)
        }
    }

    // Brazos
    if (animo == AnimoEco.CELEBRA) {
        lineaN(0.22f, 0.60f, 0.10f, 0.40f, Cuerpo, 0.07f)
        lineaN(0.78f, 0.60f, 0.90f, 0.40f, Cuerpo, 0.07f)
    } else {
        lineaN(0.22f, 0.60f, 0.12f, 0.70f, Cuerpo, 0.07f)
        lineaN(0.78f, 0.60f, 0.88f, 0.70f, Cuerpo, 0.07f)
    }

    // Insignia en el pecho
    circuloN(0.50f, 0.66f, 0.075f, CuerpoOscuro)
    figuraN(Hoja, 0.50f, 0.60f, 0.56f, 0.68f, 0.50f, 0.72f, 0.44f, 0.68f)
}

/** Logotipo de EcoGuardianes: escudo con hoja y lupa. */
@Composable
fun EcoLogo(modifier: Modifier = Modifier, tam: Dp = 120.dp) {
    Canvas(
        modifier
            .size(tam)
            .semantics { contentDescription = "Logotipo de EcoGuardianes" }
    ) {
        dibujarLogo()
    }
}

fun DrawScope.dibujarLogo() {
    figuraN(
        CuerpoOscuro,
        0.50f, 0.04f, 0.90f, 0.20f, 0.90f, 0.56f, 0.50f, 0.96f, 0.10f, 0.56f, 0.10f, 0.20f
    )
    figuraN(
        Color(0xFFFDF8EE),
        0.50f, 0.11f, 0.83f, 0.24f, 0.83f, 0.54f, 0.50f, 0.88f, 0.17f, 0.54f, 0.17f, 0.24f
    )
    figuraN(
        Cuerpo,
        0.50f, 0.16f, 0.78f, 0.27f, 0.78f, 0.53f, 0.50f, 0.82f, 0.22f, 0.53f, 0.22f, 0.27f
    )
    // Hoja
    figuraN(Hoja, 0.50f, 0.28f, 0.34f, 0.38f, 0.36f, 0.58f, 0.54f, 0.50f, 0.56f, 0.34f)
    lineaN(0.36f, 0.58f, 0.55f, 0.33f, CuerpoOscuro, 0.028f)
    // Lupa
    anilloN(0.60f, 0.52f, 0.14f, 0.045f, Color(0xFF0F4C75))
    circuloN(0.60f, 0.52f, 0.115f, Color(0x66CDE9F7))
    lineaN(0.69f, 0.62f, 0.78f, 0.72f, Color(0xFF0F4C75), 0.05f)
}
