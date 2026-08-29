package pe.ecoguardianes.ui.pantallas

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.ecoguardianes.data.catalogo.CatalogoColeccion
import pe.ecoguardianes.data.catalogo.CatalogoEscenarios
import pe.ecoguardianes.data.catalogo.CatalogoInsignias
import pe.ecoguardianes.data.catalogo.CatalogoReglas
import pe.ecoguardianes.data.local.AuditoriaEntity
import pe.ecoguardianes.data.local.HallazgoEntity
import pe.ecoguardianes.data.repo.EcoRepositorio
import pe.ecoguardianes.data.repo.ResumenGuardado
import pe.ecoguardianes.domain.model.Categoria
import pe.ecoguardianes.domain.model.Gravedad
import pe.ecoguardianes.domain.model.IconoAmb
import pe.ecoguardianes.domain.model.ZonaId
import pe.ecoguardianes.ui.art.AnimoEco
import pe.ecoguardianes.ui.art.EcoIcono
import pe.ecoguardianes.ui.componentes.BarraHallazgos
import pe.ecoguardianes.ui.componentes.BotonEco
import pe.ecoguardianes.ui.componentes.BotonEcoSuave
import pe.ecoguardianes.ui.componentes.BurbujaEco
import pe.ecoguardianes.ui.componentes.ChipCategoria
import pe.ecoguardianes.ui.componentes.CifraEco
import pe.ecoguardianes.ui.componentes.FilaEstrellas
import pe.ecoguardianes.ui.componentes.TarjetaEco
import pe.ecoguardianes.ui.theme.EcoColores
import pe.ecoguardianes.ui.theme.aColor

data class EstadoResultado(
    val cargando: Boolean = true,
    val auditoria: AuditoriaEntity? = null,
    val hallazgos: List<HallazgoEntity> = emptyList()
)

/** Carga la ficha desde la base de datos: los resultados no se simulan. */
class ResultadoViewModel(private val repo: EcoRepositorio) : ViewModel() {

    private val _estado = MutableStateFlow(EstadoResultado())
    val estado: StateFlow<EstadoResultado> = _estado.asStateFlow()

    fun cargar(auditoriaId: Long) {
        viewModelScope.launch {
            val auditoria = repo.auditoria(auditoriaId)
            val hallazgos = repo.hallazgosDe(auditoriaId)
            _estado.value = EstadoResultado(false, auditoria, hallazgos)
        }
    }
}

/** Ficha de auditoría educativa, construida con datos reales guardados. */
@Composable
fun ResultadoPantalla(
    vm: ResultadoViewModel,
    auditoriaId: Long,
    resumen: ResumenGuardado?,
    alVolverAlMapa: () -> Unit,
    alRepetir: (ZonaId) -> Unit
) {
    val estado by vm.estado.collectAsStateWithLifecycle()
    LaunchedEffect(auditoriaId) { vm.cargar(auditoriaId) }

    val auditoria = estado.auditoria
    if (estado.cargando || auditoria == null) {
        Box(Modifier.fillMaxSize().background(EcoColores.Crema), Alignment.Center) {
            Text("Preparando tu ficha de auditoría...", style = MaterialTheme.typography.titleMedium)
        }
        return
    }

    val zona = ZonaId.porId(auditoria.zonaId) ?: ZonaId.CASA
    val animo = when {
        auditoria.estrellas >= 3 -> AnimoEco.CELEBRA
        auditoria.estrellas >= 1 -> AnimoEco.FELIZ
        else -> AnimoEco.PENSATIVO
    }
    val mensaje = when {
        auditoria.estrellas >= 3 -> "¡Auditoría impecable! Eres un guardián de primera."
        auditoria.estrellas == 2 -> "¡Muy buen trabajo! Se te escapó algún detalle."
        auditoria.estrellas == 1 -> "Buen comienzo. Vuelve a mirar con calma y subirás."
        else -> "Tranquilo: repetir la misión es parte del oficio. Yo te ayudo."
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(EcoColores.VerdeNiebla, EcoColores.Crema))
            )
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Spacer(Modifier.height(8.dp))
        Text(
            "AUDITORÍA DE " + zona.titulo.uppercase(),
            style = MaterialTheme.typography.labelMedium,
            color = EcoColores.VerdeSelva
        )
        Text(
            CatalogoEscenarios.misionDe(zona).titulo,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(14.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            MedidorPuntaje(auditoria.puntaje, Modifier.size(148.dp))
            Spacer(Modifier.width(14.dp))
            Column {
                FilaEstrellas(auditoria.estrellas, tam = 34.dp)
                Spacer(Modifier.height(8.dp))
                Text(
                    "+" + auditoria.xpGanado + " XP",
                    style = MaterialTheme.typography.headlineSmall,
                    color = EcoColores.NaranjaFuego
                )
                Text(
                    if (auditoria.aprobada) "Misión superada" else "Misión no superada",
                    style = MaterialTheme.typography.labelMedium,
                    color = if (auditoria.aprobada) EcoColores.VerdeSelva else EcoColores.CoralAlerta
                )
            }
        }

        Spacer(Modifier.height(14.dp))
        BurbujaEco(mensaje, animo = animo)

        Spacer(Modifier.height(16.dp))
        TarjetaEco(modifier = Modifier.fillMaxWidth(), color = Color.White) {
            Text("Resumen de hallazgos", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(10.dp))
            BarraHallazgos(
                conformes = auditoria.conformes,
                observaciones = auditoria.observaciones,
                noConformidades = auditoria.noConformidades
            )
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CifraEco(
                    "" + auditoria.conformes,
                    "situaciones correctas",
                    Gravedad.CONFORME.colorHex.aColor(),
                    Modifier.weight(1f),
                    simbolo = "✓"
                )
                CifraEco(
                    "" + auditoria.observaciones,
                    "por mejorar",
                    Gravedad.OBSERVACION.colorHex.aColor(),
                    Modifier.weight(1f),
                    simbolo = "!"
                )
                CifraEco(
                    "" + auditoria.noConformidades,
                    "no conformidades",
                    Gravedad.NO_CONFORMIDAD.colorHex.aColor(),
                    Modifier.weight(1f),
                    simbolo = "✕"
                )
            }
            Spacer(Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CifraEco(
                    "" + auditoria.detectadosCorrectos + "/" + auditoria.problemasTotales,
                    "problemas detectados",
                    EcoColores.AzulRio,
                    Modifier.weight(1f)
                )
                CifraEco(
                    "" + auditoria.accionesCorrectas,
                    "acciones acertadas",
                    EcoColores.VerdeHoja,
                    Modifier.weight(1f)
                )
                CifraEco(
                    "" + auditoria.falsosPositivos,
                    "avisos erróneos",
                    EcoColores.CoralAlerta,
                    Modifier.weight(1f)
                )
            }
        }

        if (estado.hallazgos.isNotEmpty()) {
            Spacer(Modifier.height(16.dp))
            Text("Tu acta de auditoría", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                estado.hallazgos.forEach { FilaHallazgo(it) }
            }
        }

        if (auditoria.omitidos > 0) {
            Spacer(Modifier.height(14.dp))
            TarjetaEco(
                modifier = Modifier.fillMaxWidth(),
                color = EcoColores.AmbarSuave,
                borde = EcoColores.SolAmarillo
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    EcoIcono(IconoAmb.LUPA, tam = 26.dp)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Se te escaparon " + auditoria.omitidos + " problemas. " +
                            "Vuelve a la zona y búscalos con calma.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        if (resumen != null && (resumen.nuevasInsignias.isNotEmpty() ||
                resumen.nuevosColeccionables.isNotEmpty() || resumen.subioDeNivel)
        ) {
            Spacer(Modifier.height(16.dp))
            TarjetaEco(
                modifier = Modifier.fillMaxWidth(),
                color = EcoColores.MoradoNiebla,
                borde = EcoColores.MoradoLupa
            ) {
                Text("¡Recompensas nuevas!", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                if (resumen.subioDeNivel) {
                    Text("⬆ Subiste de nivel", style = MaterialTheme.typography.bodyMedium)
                }
                if (resumen.cambioDeRango) {
                    Text("🎖 Nuevo rango de guardián", style = MaterialTheme.typography.bodyMedium)
                }
                resumen.nuevasInsignias.forEach { id ->
                    val insignia = CatalogoInsignias.insignia(id)
                    if (insignia != null) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            EcoIcono(insignia.icono, tam = 24.dp)
                            Spacer(Modifier.width(8.dp))
                            Text(
                                "Insignia: " + insignia.nombre,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
                resumen.nuevosColeccionables.forEach { id ->
                    val pieza = CatalogoColeccion.coleccionable(id)
                    if (pieza != null) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            EcoIcono(pieza.icono, tam = 24.dp)
                            Spacer(Modifier.width(8.dp))
                            Text(
                                "Colección: " + pieza.nombre,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(18.dp))
        BotonEco(
            texto = "Volver al mapa",
            icono = IconoAmb.MAPA,
            modifier = Modifier.fillMaxWidth(),
            onClick = alVolverAlMapa
        )
        Spacer(Modifier.height(8.dp))
        BotonEcoSuave(
            texto = "Repetir la misión",
            icono = IconoAmb.LUPA,
            color = EcoColores.AzulRio,
            modifier = Modifier.fillMaxWidth(),
            onClick = { alRepetir(zona) }
        )
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun FilaHallazgo(hallazgo: HallazgoEntity) {
    val categoria = Categoria.porId(hallazgo.categoria) ?: Categoria.RESIDUOS
    val gravedad = runCatching { Gravedad.valueOf(hallazgo.gravedad) }.getOrDefault(Gravedad.OBSERVACION)
    val regla = CatalogoReglas.regla(hallazgo.reglaId)
    val valido = hallazgo.valido

    TarjetaEco(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        borde = if (valido) {
            gravedad.colorHex.aColor().copy(alpha = 0.5f)
        } else {
            EcoColores.CarbonSuave.copy(alpha = 0.4f)
        }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            EcoIcono(
                pe.ecoguardianes.data.catalogo.CatalogoEscenarios
                    .situacion(hallazgo.situacionId)?.icono ?: IconoAmb.PORTAPAPELES,
                tam = 30.dp
            )
            Spacer(Modifier.width(10.dp))
            Text(hallazgo.nombre, style = MaterialTheme.typography.titleSmall, modifier = Modifier.weight(1f))
            Text(
                if (valido) "✓" else "✕",
                style = MaterialTheme.typography.titleMedium,
                color = if (valido) EcoColores.VerdeHoja else EcoColores.CoralAlerta
            )
        }
        Spacer(Modifier.height(6.dp))
        Text(hallazgo.descripcion, style = MaterialTheme.typography.bodySmall)
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            ChipCategoria(categoria, compacto = true)
        }
        if (regla != null) {
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.Top) {
                EcoIcono(IconoAmb.LIBRO, tam = 20.dp)
                Spacer(Modifier.width(6.dp))
                Text(
                    regla.reglaSimple,
                    style = MaterialTheme.typography.bodySmall,
                    color = EcoColores.VerdeSelva
                )
            }
        }
        if (!valido) {
            Spacer(Modifier.height(6.dp))
            Text(
                "Aquí no había no conformidad: la situación cumplía la regla.",
                style = MaterialTheme.typography.labelSmall,
                color = EcoColores.CoralAlerta
            )
        }
    }
}

/** Medidor circular animado del puntaje ambiental. */
@Composable
fun MedidorPuntaje(puntaje: Int, modifier: Modifier = Modifier) {
    val progreso by animateFloatAsState(
        targetValue = puntaje / 100f,
        animationSpec = tween(900),
        label = "puntaje"
    )
    val color = when {
        puntaje >= 90 -> EcoColores.VerdeHoja
        puntaje >= 70 -> EcoColores.AzulRio
        puntaje >= 50 -> EcoColores.SolAmarillo
        else -> EcoColores.CoralAlerta
    }
    Box(modifier, contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxSize()) {
            val grosor = size.minDimension * 0.12f
            val inset = grosor / 2
            drawArc(
                color = color.copy(alpha = 0.18f),
                startAngle = 135f,
                sweepAngle = 270f,
                useCenter = false,
                topLeft = Offset(inset, inset),
                size = Size(size.width - grosor, size.height - grosor),
                style = Stroke(width = grosor, cap = StrokeCap.Round)
            )
            drawArc(
                color = color,
                startAngle = 135f,
                sweepAngle = 270f * progreso,
                useCenter = false,
                topLeft = Offset(inset, inset),
                size = Size(size.width - grosor, size.height - grosor),
                style = Stroke(width = grosor, cap = StrokeCap.Round)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "" + puntaje + "%",
                style = MaterialTheme.typography.displayMedium,
                color = color
            )
            Text(
                "puntaje ambiental",
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
