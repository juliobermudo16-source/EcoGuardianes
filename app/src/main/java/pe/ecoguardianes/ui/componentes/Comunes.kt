package pe.ecoguardianes.ui.componentes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import pe.ecoguardianes.domain.model.Categoria
import pe.ecoguardianes.domain.model.Gravedad
import pe.ecoguardianes.domain.model.IconoAmb
import pe.ecoguardianes.ui.art.AnimoEco
import pe.ecoguardianes.ui.art.EcoIcono
import pe.ecoguardianes.ui.art.EcoMascota
import pe.ecoguardianes.ui.theme.EcoColores
import pe.ecoguardianes.ui.theme.aColor

/** Tarjeta base de EcoGuardianes: esquinas amables y borde de color propio. */
@Composable
fun TarjetaEco(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.surface,
    borde: Color = MaterialTheme.colorScheme.outline,
    grosorBorde: Dp = 2.dp,
    forma: RoundedCornerShape = RoundedCornerShape(22.dp),
    onClick: (() -> Unit)? = null,
    contenido: @Composable ColumnScopeAlias.() -> Unit
) {
    val base = modifier
        .clip(forma)
        .background(color)
        .border(BorderStroke(grosorBorde, borde), forma)
        .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
    Column(modifier = base.padding(16.dp), content = contenido)
}

typealias ColumnScopeAlias = androidx.compose.foundation.layout.ColumnScope

/** Botón grande, con icono y mucho contraste: pensado para dedos pequeños. */
@Composable
fun BotonEco(
    texto: String,
    modifier: Modifier = Modifier,
    icono: IconoAmb? = null,
    colorFondo: Color = MaterialTheme.colorScheme.primary,
    colorTexto: Color = if (colorFondo.luminance() > 0.55f) EcoColores.Carbon else Color.White,
    habilitado: Boolean = true,
    onClick: () -> Unit
) {
    val fondo = if (habilitado) colorFondo else colorFondo.copy(alpha = 0.35f)
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(fondo)
            .clickable(enabled = habilitado, onClick = onClick)
            .padding(horizontal = 22.dp, vertical = 15.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (icono != null) {
            EcoIcono(icono, tam = 26.dp)
            Spacer(Modifier.width(10.dp))
        }
        Text(
            text = texto,
            style = MaterialTheme.typography.labelLarge,
            color = if (habilitado) colorTexto else colorTexto.copy(alpha = 0.6f)
        )
    }
}

/** Botón secundario, con borde y fondo suave. */
@Composable
fun BotonEcoSuave(
    texto: String,
    modifier: Modifier = Modifier,
    icono: IconoAmb? = null,
    color: Color = MaterialTheme.colorScheme.primary,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .background(color.copy(alpha = 0.12f))
            .border(BorderStroke(2.dp, color.copy(alpha = 0.5f)), RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 18.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icono != null) {
            EcoIcono(icono, tam = 22.dp)
            Spacer(Modifier.width(8.dp))
        }
        Text(texto, style = MaterialTheme.typography.labelMedium, color = color)
    }
}

/** Encabezado con botón de retroceso e identidad de la pantalla. */
@Composable
fun EncabezadoEco(
    titulo: String,
    modifier: Modifier = Modifier,
    subtitulo: String? = null,
    onVolver: (() -> Unit)? = null,
    accion: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (onVolver != null) {
            Box(
                Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .clickable(onClick = onVolver)
                    .semantics { contentDescription = "Volver" },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Spacer(Modifier.width(12.dp))
        }
        Column(Modifier.weight(1f)) {
            Text(titulo, style = MaterialTheme.typography.headlineSmall)
            if (subtitulo != null) {
                Text(
                    subtitulo,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        accion?.invoke()
    }
}

/** Distintivo de categoría ambiental: icono + color + texto, nunca solo color. */
@Composable
fun ChipCategoria(
    categoria: Categoria,
    modifier: Modifier = Modifier,
    compacto: Boolean = false
) {
    val color = categoria.colorHex.aColor()
    Row(
        modifier = modifier
            .clip(CircleShape)
            .background(color.copy(alpha = 0.15f))
            .border(BorderStroke(1.5.dp, color.copy(alpha = 0.6f)), CircleShape)
            .padding(horizontal = if (compacto) 10.dp else 14.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(categoria.simbolo, style = MaterialTheme.typography.labelMedium)
        Spacer(Modifier.width(6.dp))
        Text(
            categoria.etiqueta,
            style = if (compacto) {
                MaterialTheme.typography.labelSmall
            } else {
                MaterialTheme.typography.labelMedium
            },
            color = color
        )
    }
}

/** Distintivo de gravedad con símbolo textual, para no depender del color. */
@Composable
fun ChipGravedad(
    gravedad: Gravedad,
    modifier: Modifier = Modifier
) {
    val color = gravedad.colorHex.aColor()
    Row(
        modifier = modifier
            .clip(CircleShape)
            .background(color.copy(alpha = 0.15f))
            .border(BorderStroke(1.5.dp, color), CircleShape)
            .padding(horizontal = 12.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier.size(20.dp).clip(CircleShape).background(color),
            contentAlignment = Alignment.Center
        ) {
            Text(
                gravedad.simbolo,
                style = MaterialTheme.typography.labelSmall,
                color = Color.White,
                fontWeight = FontWeight.Black
            )
        }
        Spacer(Modifier.width(7.dp))
        Text(gravedad.etiqueta, style = MaterialTheme.typography.labelMedium, color = color)
    }
}

/** Fila de estrellas ganadas, con texto alternativo accesible. */
@Composable
fun FilaEstrellas(
    obtenidas: Int,
    modifier: Modifier = Modifier,
    total: Int = 3,
    tam: Dp = 30.dp
) {
    Row(
        modifier = modifier.semantics {
            contentDescription = "" + obtenidas + " de " + total + " estrellas"
        },
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        repeat(total) { i ->
            Box(Modifier.size(tam)) {
                if (i < obtenidas) {
                    EcoIcono(IconoAmb.ESTRELLA, tam = tam)
                } else {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .padding(4.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.35f))
                    )
                }
            }
        }
    }
}

/** Barra de progreso redondeada con etiqueta legible. */
@Composable
fun BarraProgresoEco(
    progreso: Float,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    alto: Dp = 16.dp,
    etiqueta: String? = null
) {
    val animado by animateFloatAsState(
        targetValue = progreso.coerceIn(0f, 1f),
        animationSpec = tween(700),
        label = "progreso"
    )
    Column(modifier) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(alto)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.18f))
        ) {
            Box(
                Modifier
                    .fillMaxWidth(animado)
                    .height(alto)
                    .clip(CircleShape)
                    .background(color)
            )
        }
        if (etiqueta != null) {
            Spacer(Modifier.height(4.dp))
            Text(
                etiqueta,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * Barra segmentada de la ficha de auditoría.
 * Muestra conformes, observaciones y no conformidades en una sola línea.
 */
@Composable
fun BarraHallazgos(
    conformes: Int,
    observaciones: Int,
    noConformidades: Int,
    modifier: Modifier = Modifier,
    alto: Dp = 22.dp
) {
    val total = (conformes + observaciones + noConformidades).coerceAtLeast(1)
    Row(
        modifier
            .fillMaxWidth()
            .height(alto)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .semantics {
                contentDescription = "" + conformes + " situaciones correctas, " +
                    observaciones + " aspectos por mejorar y " +
                    noConformidades + " no conformidades"
            }
    ) {
        if (conformes > 0) {
            Box(
                Modifier
                    .weight(conformes.toFloat() / total)
                    .fillMaxSize()
                    .background(Gravedad.CONFORME.colorHex.aColor())
            )
        }
        if (observaciones > 0) {
            Box(
                Modifier
                    .weight(observaciones.toFloat() / total)
                    .fillMaxSize()
                    .background(Gravedad.OBSERVACION.colorHex.aColor())
            )
        }
        if (noConformidades > 0) {
            Box(
                Modifier
                    .weight(noConformidades.toFloat() / total)
                    .fillMaxSize()
                    .background(Gravedad.NO_CONFORMIDAD.colorHex.aColor())
            )
        }
    }
}

/** Burbuja de diálogo de ECO. Textos siempre breves. */
@Composable
fun BurbujaEco(
    texto: String,
    modifier: Modifier = Modifier,
    animo: AnimoEco = AnimoEco.NORMAL,
    tamMascota: Dp = 74.dp,
    colorBurbuja: Color = MaterialTheme.colorScheme.surface
) {
    Row(
        modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        EcoMascota(tam = tamMascota, animo = animo)
        Spacer(Modifier.width(8.dp))
        Surface(
            color = colorBurbuja,
            shape = RoundedCornerShape(topStart = 6.dp, topEnd = 20.dp, bottomEnd = 20.dp, bottomStart = 20.dp),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)),
            modifier = Modifier.weight(1f)
        ) {
            Text(
                texto,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)
            )
        }
    }
}

/** Panel plegable para explicaciones largas: mantiene las pantallas ligeras. */
@Composable
fun PanelPlegable(
    titulo: String,
    expandido: Boolean,
    onCambiar: () -> Unit,
    modifier: Modifier = Modifier,
    icono: IconoAmb = IconoAmb.LIBRO,
    contenido: @Composable ColumnScopeAlias.() -> Unit
) {
    Column(modifier.fillMaxWidth()) {
        Row(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .clickable(onClick = onCambiar)
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            EcoIcono(icono, tam = 24.dp)
            Spacer(Modifier.width(10.dp))
            Text(titulo, style = MaterialTheme.typography.titleSmall, modifier = Modifier.weight(1f))
            Text(if (expandido) "▲" else "▼", style = MaterialTheme.typography.labelMedium)
        }
        AnimatedVisibility(
            visible = expandido,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column(Modifier.padding(top = 8.dp, start = 4.dp, end = 4.dp), content = contenido)
        }
    }
}

/** Cifra grande con etiqueta, para las fichas de resultado. */
@Composable
fun CifraEco(
    valor: String,
    etiqueta: String,
    color: Color,
    modifier: Modifier = Modifier,
    simbolo: String? = null
) {
    Column(
        modifier
            .clip(RoundedCornerShape(18.dp))
            .background(color.copy(alpha = 0.12f))
            .border(BorderStroke(2.dp, color.copy(alpha = 0.45f)), RoundedCornerShape(18.dp))
            .padding(vertical = 12.dp, horizontal = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (simbolo != null) {
            Text(simbolo, style = MaterialTheme.typography.titleMedium, color = color)
        }
        Text(valor, style = MaterialTheme.typography.headlineMedium, color = color)
        Text(
            etiqueta,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

/** Separador decorativo con el punteado del cuaderno de campo. */
@Composable
fun SeparadorEco(modifier: Modifier = Modifier) {
    Box(
        modifier
            .fillMaxWidth()
            .height(2.dp)
            .clearAndSetSemantics { }
            .background(SolidColor(MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)))
    )
}
