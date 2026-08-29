package pe.ecoguardianes.ui.pantallas

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import pe.ecoguardianes.domain.model.DestinoReto
import pe.ecoguardianes.domain.model.IconoAmb
import pe.ecoguardianes.domain.model.PiezaReto
import pe.ecoguardianes.domain.model.Reto
import pe.ecoguardianes.domain.model.TipoReto
import pe.ecoguardianes.ui.art.EcoIcono
import pe.ecoguardianes.ui.componentes.TarjetaEco
import pe.ecoguardianes.ui.theme.EcoColores
import pe.ecoguardianes.ui.theme.LocalPreferenciasEco
import pe.ecoguardianes.ui.theme.aColor

/**
 * Mini-reto interactivo.
 *
 * Los retos de clasificación y colocación se resuelven arrastrando; los de
 * selección, conexión y orden se resuelven tocando la pieza y luego su destino.
 * En ambos casos la solución se comprueba contra los datos del catálogo.
 */
@Composable
fun RetoInteractivo(
    reto: Reto,
    solucion: Map<String, String>,
    onColocar: (String, String) -> Unit,
    onQuitar: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier.fillMaxWidth()) {
        Text(reto.enunciado, style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(4.dp))
        Text(
            reto.ayuda,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(12.dp))

        val arrastrable = reto.tipo == TipoReto.CLASIFICAR || reto.tipo == TipoReto.ARRASTRAR
        if (arrastrable) {
            TableroArrastre(reto, solucion, onColocar, onQuitar)
        } else {
            TableroToque(reto, solucion, onColocar, onQuitar)
        }
    }
}

// ------------------------------------------------------------ Drag and drop

@Composable
private fun TableroArrastre(
    reto: Reto,
    solucion: Map<String, String>,
    onColocar: (String, String) -> Unit,
    onQuitar: (String) -> Unit
) {
    val limites = remember { mutableStateOf(mapOf<String, Rect>()) }
    val haptica = LocalHapticFeedback.current
    val preferencias = LocalPreferenciasEco.current
    val pendientes = reto.piezas.filter { it.id !in solucion }

    Column {
        Text(
            "Arrastra cada elemento a su recipiente",
            style = MaterialTheme.typography.labelMedium,
            color = EcoColores.MoradoLupa
        )
        Spacer(Modifier.height(8.dp))

        Box(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(EcoColores.CremaHonda)
                .padding(10.dp)
        ) {
            if (pendientes.isEmpty()) {
                Text(
                    "Todo colocado. Pulsa Comprobar.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    pendientes.chunked(2).forEach { fila ->
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            fila.forEach { pieza ->
                                PiezaArrastrable(
                                    pieza = pieza,
                                    modifier = Modifier.weight(1f),
                                    onSoltar = { centro ->
                                        val destino = limites.value.entries
                                            .firstOrNull { it.value.contains(centro) }?.key
                                        if (destino != null) {
                                            if (preferencias.haptica) {
                                                haptica.performHapticFeedback(
                                                    HapticFeedbackType.LongPress
                                                )
                                            }
                                            onColocar(pieza.id, destino)
                                        }
                                    }
                                )
                            }
                            if (fila.size == 1) Spacer(Modifier.weight(1f))
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            reto.destinos.forEach { destino ->
                ContenedorDestino(
                    destino = destino,
                    piezas = reto.piezas.filter { solucion[it.id] == destino.id },
                    onQuitar = onQuitar,
                    modifier = Modifier
                        .weight(1f)
                        .onGloballyPositioned { coords ->
                            limites.value = limites.value + (destino.id to coords.boundsInRoot())
                        }
                )
            }
        }
    }
}

@Composable
private fun PiezaArrastrable(
    pieza: PiezaReto,
    onSoltar: (Offset) -> Unit,
    modifier: Modifier = Modifier
) {
    var desplazamiento by remember(pieza.id) { mutableStateOf(Offset.Zero) }
    var arrastrando by remember(pieza.id) { mutableStateOf(false) }
    var limites by remember(pieza.id) { mutableStateOf(Rect.Zero) }

    Row(
        modifier
            .onGloballyPositioned { limites = it.boundsInRoot() }
            .graphicsLayer {
                translationX = desplazamiento.x
                translationY = desplazamiento.y
                scaleX = if (arrastrando) 1.06f else 1f
                scaleY = if (arrastrando) 1.06f else 1f
            }
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(
                BorderStroke(2.dp, EcoColores.MoradoLupa.copy(alpha = 0.45f)),
                RoundedCornerShape(16.dp)
            )
            .pointerInput(pieza.id) {
                detectDragGestures(
                    onDragStart = { arrastrando = true },
                    onDragEnd = {
                        arrastrando = false
                        onSoltar(limites.center + desplazamiento)
                        desplazamiento = Offset.Zero
                    },
                    onDragCancel = {
                        arrastrando = false
                        desplazamiento = Offset.Zero
                    },
                    onDrag = { cambio, delta ->
                        cambio.consume()
                        desplazamiento += delta
                    }
                )
            }
            .padding(horizontal = 8.dp, vertical = 10.dp)
            .semantics { contentDescription = "Arrastra " + pieza.etiqueta },
        verticalAlignment = Alignment.CenterVertically
    ) {
        EcoIcono(pieza.icono, tam = 28.dp)
        Spacer(Modifier.width(6.dp))
        Text(pieza.etiqueta, style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
private fun ContenedorDestino(
    destino: DestinoReto,
    piezas: List<PiezaReto>,
    onQuitar: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val color = destino.colorHex.aColor()
    Column(
        modifier
            .clip(RoundedCornerShape(18.dp))
            .background(color.copy(alpha = 0.14f))
            .border(BorderStroke(3.dp, color), RoundedCornerShape(18.dp))
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EcoIcono(destino.icono, tam = 28.dp)
        Text(
            destino.etiqueta,
            style = MaterialTheme.typography.labelSmall,
            color = color,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(6.dp))
        piezas.forEach { pieza ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White)
                    .clickable { onQuitar(pieza.id) }
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                EcoIcono(pieza.icono, tam = 18.dp)
                Spacer(Modifier.width(4.dp))
                Text(
                    pieza.etiqueta,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.weight(1f)
                )
                Text("✕", style = MaterialTheme.typography.labelSmall)
            }
            Spacer(Modifier.height(4.dp))
        }
        if (piezas.isEmpty()) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(34.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Text("Suelta aquí", style = MaterialTheme.typography.labelSmall, color = color)
            }
        }
    }
}

// --------------------------------------------------------- Toque y selección

@Composable
private fun TableroToque(
    reto: Reto,
    solucion: Map<String, String>,
    onColocar: (String, String) -> Unit,
    onQuitar: (String) -> Unit
) {
    var seleccionada by remember { mutableStateOf<String?>(null) }
    val pendientes = reto.piezas.filter { it.id !in solucion }

    Column {
        Text(
            if (seleccionada == null) {
                "1) Toca un elemento"
            } else {
                "2) Ahora toca dónde va"
            },
            style = MaterialTheme.typography.labelMedium,
            color = EcoColores.MoradoLupa
        )
        Spacer(Modifier.height(8.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            pendientes.forEach { pieza ->
                val activa = seleccionada == pieza.id
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (activa) EcoColores.MoradoNiebla else Color.White)
                        .border(
                            BorderStroke(
                                if (activa) 3.dp else 2.dp,
                                EcoColores.MoradoLupa.copy(alpha = if (activa) 1f else 0.35f)
                            ),
                            RoundedCornerShape(16.dp)
                        )
                        .clickable { seleccionada = if (activa) null else pieza.id }
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    EcoIcono(pieza.icono, tam = 26.dp)
                    Spacer(Modifier.width(10.dp))
                    Text(pieza.etiqueta, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        Spacer(Modifier.height(12.dp))
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            reto.destinos.forEach { destino ->
                val color = destino.colorHex.aColor()
                val colocadas = reto.piezas.filter { solucion[it.id] == destino.id }
                Column(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(color.copy(alpha = 0.12f))
                        .border(BorderStroke(2.5.dp, color.copy(alpha = 0.7f)), RoundedCornerShape(18.dp))
                        .clickable(enabled = seleccionada != null) {
                            seleccionada?.let {
                                onColocar(it, destino.id)
                                seleccionada = null
                            }
                        }
                        .padding(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            Modifier.size(30.dp).clip(CircleShape).background(color.copy(alpha = 0.25f)),
                            contentAlignment = Alignment.Center
                        ) {
                            EcoIcono(destino.icono, tam = 20.dp)
                        }
                        Spacer(Modifier.width(8.dp))
                        Text(destino.etiqueta, style = MaterialTheme.typography.titleSmall, color = color)
                    }
                    colocadas.forEach { pieza ->
                        Spacer(Modifier.height(6.dp))
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White)
                                .clickable { onQuitar(pieza.id) }
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            EcoIcono(pieza.icono, tam = 20.dp)
                            Spacer(Modifier.width(6.dp))
                            Text(
                                pieza.etiqueta,
                                style = MaterialTheme.typography.labelMedium,
                                modifier = Modifier.weight(1f)
                            )
                            Text("✕", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
        }
    }
}

/** Vista compacta del reto para la ficha de resultado. */
@Composable
fun ResumenReto(reto: Reto, modifier: Modifier = Modifier) {
    TarjetaEco(modifier = modifier.fillMaxWidth(), color = Color.White) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            EcoIcono(IconoAmb.CASCO, tam = 26.dp)
            Spacer(Modifier.width(8.dp))
            Text(reto.tipo.etiqueta, style = MaterialTheme.typography.titleSmall)
        }
        Spacer(Modifier.height(4.dp))
        Text(reto.enunciado, style = MaterialTheme.typography.bodySmall)
    }
}
