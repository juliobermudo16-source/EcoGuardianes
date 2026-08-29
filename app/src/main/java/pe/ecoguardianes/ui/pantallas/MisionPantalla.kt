package pe.ecoguardianes.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pe.ecoguardianes.data.catalogo.CatalogoEscenarios
import pe.ecoguardianes.domain.audit.DesbloqueoZonas
import pe.ecoguardianes.domain.audit.EstadoJuego
import pe.ecoguardianes.domain.model.EstadoZona
import pe.ecoguardianes.domain.model.IconoAmb
import pe.ecoguardianes.domain.model.ZonaId
import pe.ecoguardianes.ui.art.AnimoEco
import pe.ecoguardianes.ui.art.EcoIcono
import pe.ecoguardianes.ui.art.FondoEscenario
import pe.ecoguardianes.ui.componentes.BotonEco
import pe.ecoguardianes.ui.componentes.BurbujaEco
import pe.ecoguardianes.ui.componentes.ChipCategoria
import pe.ecoguardianes.ui.componentes.EncabezadoEco
import pe.ecoguardianes.ui.componentes.FilaEstrellas
import pe.ecoguardianes.ui.componentes.TarjetaEco
import pe.ecoguardianes.ui.theme.EcoColores

/** Sala de instrucciones antes de entrar al escenario. */
@Composable
fun MisionPantalla(
    zona: ZonaId,
    juego: EstadoJuego,
    alVolver: () -> Unit,
    alEmpezar: () -> Unit
) {
    val mision = CatalogoEscenarios.misionDe(zona)
    val situaciones = CatalogoEscenarios.deZona(zona)
    val estadoZona = DesbloqueoZonas.estado(zona, juego)
    val abierta = DesbloqueoZonas.estaAbierta(zona, juego)
    val requisito = DesbloqueoZonas.requisitoPendiente(zona, juego)
    val categorias = situaciones.map { it.categoria }.distinct()

    Column(
        Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(EcoColores.Crema, EcoColores.VerdeNiebla))
            )
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        Spacer(Modifier.height(8.dp))
        EncabezadoEco(
            titulo = zona.titulo,
            subtitulo = zona.lema,
            onVolver = alVolver
        )

        Box(
            Modifier
                .fillMaxWidth()
                .height(190.dp)
                .clip(RoundedCornerShape(22.dp))
        ) {
            FondoEscenario(zona, Modifier.fillMaxSize())
            Box(
                Modifier
                    .align(Alignment.BottomStart)
                    .padding(10.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White.copy(alpha = 0.9f))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(estadoZona.simbolo, style = MaterialTheme.typography.labelMedium)
                    Spacer(Modifier.width(6.dp))
                    Text(estadoZona.etiqueta, style = MaterialTheme.typography.labelMedium)
                }
            }
        }

        Spacer(Modifier.height(14.dp))
        BurbujaEco(mision.briefingEco, animo = AnimoEco.NORMAL)

        Spacer(Modifier.height(14.dp))
        TarjetaEco(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            borde = EcoColores.VerdeHoja.copy(alpha = 0.4f)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                EcoIcono(IconoAmb.PORTAPAPELES, tam = 32.dp)
                Spacer(Modifier.width(10.dp))
                Text(mision.titulo, style = MaterialTheme.typography.titleLarge)
            }
            Spacer(Modifier.height(10.dp))
            Text("Objetivo", style = MaterialTheme.typography.titleSmall)
            Text(mision.objetivo, style = MaterialTheme.typography.bodyMedium)

            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                DatoMision(
                    "" + situaciones.size,
                    "puntos que revisar",
                    EcoColores.AzulRio,
                    Modifier.weight(1f)
                )
                DatoMision(
                    "" + mision.minimoHallazgos,
                    "hallazgos mínimos",
                    EcoColores.NaranjaFuego,
                    Modifier.weight(1f)
                )
                DatoMision(
                    "N" + mision.nivel,
                    "dificultad",
                    EcoColores.MoradoLupa,
                    Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(12.dp))
            Text("Puedes encontrar problemas de:", style = MaterialTheme.typography.titleSmall)
            Spacer(Modifier.height(6.dp))
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                categorias.chunked(2).forEach { fila ->
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        fila.forEach { ChipCategoria(it, compacto = true) }
                    }
                }
            }

            if (juego.estrellasEn(zona) > 0) {
                Spacer(Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Tu mejor resultado:", style = MaterialTheme.typography.titleSmall)
                    Spacer(Modifier.width(8.dp))
                    FilaEstrellas(juego.estrellasEn(zona), tam = 22.dp)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "" + (juego.zonas[zona]?.mejorPuntaje ?: 0) + "%",
                        style = MaterialTheme.typography.titleMedium,
                        color = EcoColores.VerdeSelva
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        if (abierta) {
            BotonEco(
                texto = if (estadoZona == EstadoZona.DISPONIBLE) {
                    "Empezar la auditoría"
                } else {
                    "Repetir la auditoría"
                },
                icono = IconoAmb.LUPA,
                modifier = Modifier.fillMaxWidth(),
                onClick = alEmpezar
            )
        } else {
            TarjetaEco(
                modifier = Modifier.fillMaxWidth(),
                color = EcoColores.CoralSuave,
                borde = EcoColores.CoralAlerta.copy(alpha = 0.5f)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🔒", style = MaterialTheme.typography.headlineSmall)
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Text("Zona bloqueada", style = MaterialTheme.typography.titleMedium)
                        Text(
                            requisito ?: "Sigue avanzando para abrirla.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun DatoMision(
    valor: String,
    etiqueta: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .clip(RoundedCornerShape(16.dp))
            .background(color.copy(alpha = 0.12f))
            .padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(valor, style = MaterialTheme.typography.headlineSmall, color = color)
        Text(
            etiqueta,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
