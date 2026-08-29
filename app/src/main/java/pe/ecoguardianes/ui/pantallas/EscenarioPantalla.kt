package pe.ecoguardianes.ui.pantallas

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import pe.ecoguardianes.domain.model.Situacion
import pe.ecoguardianes.domain.model.ZonaId
import pe.ecoguardianes.ui.art.EcoIcono
import pe.ecoguardianes.ui.art.FondoEscenario
import pe.ecoguardianes.ui.componentes.BotonEco
import pe.ecoguardianes.ui.componentes.BotonEcoSuave
import pe.ecoguardianes.ui.componentes.BurbujaEco
import pe.ecoguardianes.ui.componentes.EncabezadoEco
import pe.ecoguardianes.ui.theme.EcoColores

/** Estado visual de un objeto del escenario. */
private enum class MarcaVisual { SIN_REVISAR, REVISADO, REGISTRADO, RESUELTO }

/** Pantalla de auditoría: explorar el escenario y actuar sobre lo que se encuentra. */
@Composable
fun EscenarioPantalla(
    vm: AuditoriaViewModel,
    zona: ZonaId,
    alVolver: () -> Unit,
    alResultado: (Long) -> Unit
) {
    val estado by vm.estado.collectAsStateWithLifecycle()

    LaunchedEffect(zona) { vm.cargar(zona) }

    Box(Modifier.fillMaxSize().background(EcoColores.Crema)) {
        Column(Modifier.fillMaxSize()) {
            EncabezadoEco(
                titulo = zona.titulo,
                subtitulo = "Hallazgos registrados: " + estado.hallazgosRegistrados +
                    "  ·  Sin revisar: " + estado.situacionesRestantes,
                onVolver = alVolver,
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            BoxWithConstraints(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 12.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .border(
                        BorderStroke(3.dp, EcoColores.VerdeHoja.copy(alpha = 0.35f)),
                        RoundedCornerShape(22.dp)
                    )
            ) {
                val ancho = maxWidth
                val alto = maxHeight
                FondoEscenario(zona, Modifier.fillMaxSize())

                estado.situaciones.forEach { situacion ->
                    val visual = when {
                        situacion.id in estado.resueltas -> MarcaVisual.RESUELTO
                        estado.marcas[situacion.id]?.marcadaComoProblema == true ->
                            MarcaVisual.REGISTRADO
                        situacion.id in estado.revisadas -> MarcaVisual.REVISADO
                        else -> MarcaVisual.SIN_REVISAR
                    }
                    ObjetoEscenario(
                        situacion = situacion,
                        visual = visual,
                        onClick = { vm.tocarSituacion(situacion.id) },
                        modifier = Modifier.offset(
                            x = ancho * situacion.x - 27.dp,
                            y = alto * situacion.y - 27.dp
                        )
                    )
                }
            }

            Column(Modifier.padding(12.dp)) {
                BurbujaEco(
                    texto = estado.mensajeEco,
                    animo = estado.animo,
                    tamMascota = 62.dp
                )
                Spacer(Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    BotonEcoSuave(
                        texto = "Ver acta",
                        color = EcoColores.AzulRio,
                        modifier = Modifier.weight(1f),
                        onClick = { vm.prepararCierre() }
                    )
                    BotonEco(
                        texto = if (estado.puedeCerrar) "Cerrar auditoría" else "Faltan hallazgos",
                        colorFondo = EcoColores.VerdeHoja,
                        habilitado = estado.puedeCerrar && !estado.guardando,
                        modifier = Modifier.weight(1.3f),
                        onClick = { vm.cerrarAuditoria(alResultado) }
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = estado.fase != FaseAuditoria.EXPLORAR,
            enter = fadeIn() + slideInVertically { it / 3 },
            exit = fadeOut() + slideOutVertically { it / 3 }
        ) {
            PanelAuditoria(vm = vm, estado = estado, alResultado = alResultado)
        }
    }
}

@Composable
private fun ObjetoEscenario(
    situacion: Situacion,
    visual: MarcaVisual,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val transicion = rememberInfiniteTransition(label = "objeto")
    val pulso by transicion.animateFloat(
        initialValue = 1f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(tween(1400), RepeatMode.Reverse),
        label = "pulsoObjeto"
    )
    val (color, simbolo) = when (visual) {
        MarcaVisual.SIN_REVISAR -> EcoColores.SolAmarillo to "?"
        MarcaVisual.REVISADO -> EcoColores.CarbonSuave to "·"
        MarcaVisual.REGISTRADO -> EcoColores.CoralAlerta to "!"
        MarcaVisual.RESUELTO -> EcoColores.VerdeHoja to "✓"
    }
    val etiquetaEstado = when (visual) {
        MarcaVisual.SIN_REVISAR -> "sin revisar"
        MarcaVisual.REVISADO -> "revisado"
        MarcaVisual.REGISTRADO -> "registrado como hallazgo"
        MarcaVisual.RESUELTO -> "resuelto"
    }

    Box(
        modifier
            .size(54.dp)
            .scale(if (visual == MarcaVisual.SIN_REVISAR) pulso else 1f)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.92f))
            .border(BorderStroke(3.dp, color), CircleShape)
            .clickable(onClick = onClick)
            .semantics {
                contentDescription = situacion.nombre + ", " + etiquetaEstado
            },
        contentAlignment = Alignment.Center
    ) {
        EcoIcono(situacion.icono, tam = 30.dp)
        Box(
            Modifier
                .align(Alignment.TopEnd)
                .size(20.dp)
                .clip(CircleShape)
                .background(color),
            contentAlignment = Alignment.Center
        ) {
            Text(
                simbolo,
                style = MaterialTheme.typography.labelSmall,
                color = Color.White,
                fontWeight = FontWeight.Black
            )
        }
    }
}
