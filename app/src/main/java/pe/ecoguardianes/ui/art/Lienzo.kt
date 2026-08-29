package pe.ecoguardianes.ui.art

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

/**
 * Utilidades de dibujo en coordenadas normalizadas (0..1).
 *
 * Toda la ilustración de EcoGuardianes se dibuja con estas primitivas, de modo
 * que el arte escala a cualquier tamaño sin depender de imágenes externas.
 */

fun DrawScope.circuloN(cx: Float, cy: Float, radio: Float, color: Color) {
    drawCircle(
        color = color,
        radius = radio * size.minDimension,
        center = Offset(cx * size.width, cy * size.height)
    )
}

fun DrawScope.anilloN(cx: Float, cy: Float, radio: Float, grosor: Float, color: Color) {
    drawCircle(
        color = color,
        radius = radio * size.minDimension,
        center = Offset(cx * size.width, cy * size.height),
        style = Stroke(width = grosor * size.minDimension, cap = StrokeCap.Round)
    )
}

fun DrawScope.cajaN(
    x: Float,
    y: Float,
    ancho: Float,
    alto: Float,
    color: Color,
    esquina: Float = 0f
) {
    drawRoundRect(
        color = color,
        topLeft = Offset(x * size.width, y * size.height),
        size = Size(ancho * size.width, alto * size.height),
        cornerRadius = CornerRadius(esquina * size.minDimension, esquina * size.minDimension)
    )
}

fun DrawScope.lineaN(
    x1: Float,
    y1: Float,
    x2: Float,
    y2: Float,
    color: Color,
    grosor: Float = 0.06f,
    punteada: Boolean = false
) {
    drawLine(
        color = color,
        start = Offset(x1 * size.width, y1 * size.height),
        end = Offset(x2 * size.width, y2 * size.height),
        strokeWidth = grosor * size.minDimension,
        cap = StrokeCap.Round,
        pathEffect = if (punteada) {
            PathEffect.dashPathEffect(
                floatArrayOf(0.06f * size.minDimension, 0.06f * size.minDimension)
            )
        } else {
            null
        }
    )
}

fun DrawScope.figuraN(color: Color, vararg puntos: Float) {
    require(puntos.size % 2 == 0) { "Los puntos deben venir en pares x,y" }
    val ruta = Path()
    ruta.moveTo(puntos[0] * size.width, puntos[1] * size.height)
    var i = 2
    while (i < puntos.size) {
        ruta.lineTo(puntos[i] * size.width, puntos[i + 1] * size.height)
        i += 2
    }
    ruta.close()
    drawPath(ruta, color)
}

fun DrawScope.rutaN(color: Color, grosor: Float = 0f, bloque: Path.(Float, Float) -> Unit) {
    val ruta = Path()
    ruta.bloque(size.width, size.height)
    if (grosor > 0f) {
        drawPath(ruta, color, style = Stroke(width = grosor * size.minDimension, cap = StrokeCap.Round))
    } else {
        drawPath(ruta, color)
    }
}

fun DrawScope.arcoN(
    x: Float,
    y: Float,
    ancho: Float,
    alto: Float,
    inicio: Float,
    barrido: Float,
    color: Color,
    grosor: Float = 0.05f
) {
    drawArc(
        color = color,
        startAngle = inicio,
        sweepAngle = barrido,
        useCenter = false,
        topLeft = Offset(x * size.width, y * size.height),
        size = Size(ancho * size.width, alto * size.height),
        style = Stroke(width = grosor * size.minDimension, cap = StrokeCap.Round)
    )
}

fun DrawScope.ovaloN(x: Float, y: Float, ancho: Float, alto: Float, color: Color) {
    drawOval(
        color = color,
        topLeft = Offset(x * size.width, y * size.height),
        size = Size(ancho * size.width, alto * size.height)
    )
}

/** Rectángulo normalizado, útil para recortes y cálculos. */
fun DrawScope.rectN(x: Float, y: Float, ancho: Float, alto: Float): Rect = Rect(
    Offset(x * size.width, y * size.height),
    Size(ancho * size.width, alto * size.height)
)
