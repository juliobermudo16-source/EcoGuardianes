package pe.ecoguardianes.ui.pantallas

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import pe.ecoguardianes.data.catalogo.CatalogoColeccion
import pe.ecoguardianes.data.catalogo.CatalogoInsignias
import pe.ecoguardianes.domain.audit.EstadoJuego
import pe.ecoguardianes.domain.audit.EvaluadorRecompensas
import pe.ecoguardianes.domain.model.Coleccionable
import pe.ecoguardianes.domain.model.IconoAmb
import pe.ecoguardianes.domain.model.Insignia
import pe.ecoguardianes.domain.model.TipoColeccionable
import pe.ecoguardianes.ui.art.AnimoEco
import pe.ecoguardianes.ui.art.EcoIcono
import pe.ecoguardianes.ui.componentes.BarraProgresoEco
import pe.ecoguardianes.ui.componentes.BurbujaEco
import pe.ecoguardianes.ui.componentes.EncabezadoEco
import pe.ecoguardianes.ui.componentes.TarjetaEco
import pe.ecoguardianes.ui.theme.EcoColores
import pe.ecoguardianes.ui.theme.aColor

/** Colección ambiental: cada pieza se desbloquea con progreso real. */
@Composable
fun ColeccionPantalla(juego: EstadoJuego, alVolver: () -> Unit) {
    val progresos = remember(juego) {
        EvaluadorRecompensas.evaluarColeccionables(juego, CatalogoColeccion.coleccionables)
            .associateBy { it.id }
    }
    val desbloqueados = progresos.values.count { it.desbloqueada }
    var seleccionado by remember { mutableStateOf<Coleccionable?>(null) }
    var tipo by remember { mutableStateOf<TipoColeccionable?>(null) }
    val lista = remember(tipo) {
        tipo?.let { CatalogoColeccion.porTipo(it) } ?: CatalogoColeccion.coleccionables
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(EcoColores.VerdeNiebla, EcoColores.Crema)))
            .padding(horizontal = 16.dp)
    ) {
        Spacer(Modifier.height(8.dp))
        EncabezadoEco(
            titulo = "Colección ambiental",
            subtitulo = "" + desbloqueados + " de " + CatalogoColeccion.coleccionables.size +
                " piezas reunidas",
            onVolver = alVolver
        )
        BarraProgresoEco(
            progreso = desbloqueados.toFloat() / CatalogoColeccion.coleccionables.size,
            color = EcoColores.VerdeHoja
        )
        Spacer(Modifier.height(10.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            FiltroTipo("Todo", tipo == null) { tipo = null }
            TipoColeccionable.entries.take(4).forEach { t ->
                FiltroTipo(t.simbolo, tipo == t) { tipo = if (tipo == t) null else t }
            }
        }
        Spacer(Modifier.height(10.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(lista, key = { it.id }) { pieza ->
                val progreso = progresos[pieza.id]
                TarjetaColeccionable(
                    pieza = pieza,
                    desbloqueado = progreso?.desbloqueada == true,
                    progreso = progreso?.progreso ?: 0f,
                    onClick = { seleccionado = pieza }
                )
            }
        }
    }

    val elegido = seleccionado
    if (elegido != null) {
        val progreso = progresos[elegido.id]
        val abierto = progreso?.desbloqueada == true
        AlertDialog(
            onDismissRequest = { seleccionado = null },
            confirmButton = {
                TextButton(onClick = { seleccionado = null }) { Text("Cerrar") }
            },
            icon = { EcoIcono(elegido.icono, tam = 44.dp) },
            title = { Text(if (abierto) elegido.nombre else "Pieza por descubrir") },
            text = {
                Column {
                    if (abierto) {
                        Text(elegido.descripcion, style = MaterialTheme.typography.bodyMedium)
                        Spacer(Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.Top) {
                            EcoIcono(IconoAmb.LUPA, tam = 20.dp)
                            Spacer(Modifier.width(6.dp))
                            Text(
                                elegido.datoCurioso,
                                style = MaterialTheme.typography.bodySmall,
                                color = EcoColores.MoradoLupa
                            )
                        }
                    } else {
                        Text(
                            "Todavía no la tienes. Para conseguirla:",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            elegido.requisito.descripcion,
                            style = MaterialTheme.typography.titleSmall,
                            color = EcoColores.VerdeSelva
                        )
                        Spacer(Modifier.height(10.dp))
                        BarraProgresoEco(
                            progreso = progreso?.progreso ?: 0f,
                            color = EcoColores.SolAmarillo,
                            etiqueta = "" + (progreso?.actual ?: 0) + " de " +
                                (progreso?.meta ?: elegido.requisito.meta)
                        )
                    }
                }
            }
        )
    }
}

@Composable
private fun FiltroTipo(texto: String, activo: Boolean, onClick: () -> Unit) {
    Box(
        Modifier
            .clip(CircleShape)
            .background(
                if (activo) EcoColores.VerdeHoja.copy(alpha = 0.24f) else EcoColores.CremaHonda
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(texto, style = MaterialTheme.typography.labelMedium)
    }
}

@Composable
private fun TarjetaColeccionable(
    pieza: Coleccionable,
    desbloqueado: Boolean,
    progreso: Float,
    onClick: () -> Unit
) {
    val color = pieza.colorHex.aColor()
    Column(
        Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (desbloqueado) Color.White else EcoColores.CremaHonda)
            .border(
                BorderStroke(2.5.dp, if (desbloqueado) color else EcoColores.CarbonSuave.copy(alpha = 0.3f)),
                RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onClick)
            .padding(10.dp)
            .semantics {
                contentDescription = if (desbloqueado) {
                    pieza.nombre + ", conseguida"
                } else {
                    "Pieza bloqueada. " + pieza.requisito.descripcion
                }
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            Modifier.size(54.dp).clip(CircleShape).background(color.copy(alpha = 0.14f)),
            contentAlignment = Alignment.Center
        ) {
            if (desbloqueado) {
                EcoIcono(pieza.icono, tam = 38.dp)
            } else {
                Box(Modifier.alpha(0.35f)) { EcoIcono(pieza.icono, tam = 38.dp) }
                Text("🔒", style = MaterialTheme.typography.titleMedium)
            }
        }
        Spacer(Modifier.height(6.dp))
        Text(
            if (desbloqueado) pieza.nombre else "?",
            style = MaterialTheme.typography.labelMedium,
            textAlign = TextAlign.Center,
            color = if (desbloqueado) EcoColores.Carbon else EcoColores.CarbonSuave
        )
        Text(
            pieza.tipo.etiqueta,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        if (!desbloqueado) {
            Spacer(Modifier.height(6.dp))
            BarraProgresoEco(progreso = progreso, color = color, alto = 6.dp)
        }
    }
}

/** Vitrina de insignias con su progreso real. */
@Composable
fun InsigniasPantalla(juego: EstadoJuego, alVolver: () -> Unit) {
    val progresos = remember(juego) {
        EvaluadorRecompensas.evaluarInsignias(juego, CatalogoInsignias.insignias)
            .associateBy { it.id }
    }
    val ganadas = progresos.values.count { it.desbloqueada }

    Column(
        Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(EcoColores.AmbarSuave, EcoColores.Crema)))
            .padding(horizontal = 16.dp)
    ) {
        Spacer(Modifier.height(8.dp))
        EncabezadoEco(
            titulo = "Insignias",
            subtitulo = "" + ganadas + " de " + CatalogoInsignias.insignias.size + " conseguidas",
            onVolver = alVolver
        )
        BurbujaEco(
            if (ganadas == 0) {
                "Todavía no tienes insignias. ¡Completa misiones y llegarán!"
            } else {
                "¡" + ganadas + " insignias! Cada una cuenta una historia de tu trabajo."
            },
            animo = if (ganadas > 0) AnimoEco.CELEBRA else AnimoEco.NORMAL,
            tamMascota = 58.dp
        )
        Spacer(Modifier.height(12.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(CatalogoInsignias.insignias, key = { it.id }) { insignia ->
                val progreso = progresos[insignia.id]
                TarjetaInsignia(
                    insignia = insignia,
                    desbloqueada = progreso?.desbloqueada == true,
                    actual = progreso?.actual ?: 0,
                    meta = progreso?.meta ?: insignia.requisito.meta
                )
            }
            item { Spacer(Modifier.height(20.dp)) }
        }
    }
}

@Composable
private fun TarjetaInsignia(
    insignia: Insignia,
    desbloqueada: Boolean,
    actual: Int,
    meta: Int
) {
    val color = insignia.colorHex.aColor()
    TarjetaEco(
        modifier = Modifier
            .fillMaxWidth()
            .semantics {
                contentDescription = insignia.nombre +
                    (if (desbloqueada) ", conseguida" else ", en progreso " + actual + " de " + meta)
            },
        color = if (desbloqueada) Color.White else EcoColores.CremaHonda,
        borde = if (desbloqueada) color else EcoColores.CarbonSuave.copy(alpha = 0.28f)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = if (desbloqueada) 0.2f else 0.08f))
                    .border(BorderStroke(2.dp, color.copy(alpha = if (desbloqueada) 1f else 0.3f)), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Box(Modifier.alpha(if (desbloqueada) 1f else 0.35f)) {
                    EcoIcono(insignia.icono, tam = 32.dp)
                }
            }
            Spacer(Modifier.width(8.dp))
            Text(insignia.simbolo, style = MaterialTheme.typography.headlineSmall)
        }
        Spacer(Modifier.height(8.dp))
        Text(insignia.nombre, style = MaterialTheme.typography.titleSmall)
        Spacer(Modifier.height(4.dp))
        Text(
            if (desbloqueada) insignia.descripcion else insignia.requisito.descripcion,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(8.dp))
        BarraProgresoEco(
            progreso = if (meta <= 0) 1f else actual.toFloat() / meta,
            color = color,
            alto = 8.dp,
            etiqueta = if (desbloqueada) "¡Conseguida!" else "" + actual + " / " + meta
        )
    }
}
