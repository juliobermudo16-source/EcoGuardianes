package pe.ecoguardianes.ui.pantallas

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import pe.ecoguardianes.data.catalogo.CatalogoAvatares
import pe.ecoguardianes.data.catalogo.CatalogoEscenarios
import pe.ecoguardianes.domain.audit.DesbloqueoZonas
import pe.ecoguardianes.domain.audit.EstadoJuego
import pe.ecoguardianes.domain.audit.Progresion
import pe.ecoguardianes.domain.model.EstadoZona
import pe.ecoguardianes.domain.model.IconoAmb
import pe.ecoguardianes.domain.model.ZonaId
import pe.ecoguardianes.ui.art.AnimoEco
import pe.ecoguardianes.ui.art.EcoIcono
import pe.ecoguardianes.ui.art.circuloN
import pe.ecoguardianes.ui.art.figuraN
import pe.ecoguardianes.ui.art.lineaN
import pe.ecoguardianes.ui.art.ovaloN
import pe.ecoguardianes.ui.componentes.BarraProgresoEco
import pe.ecoguardianes.ui.componentes.BurbujaEco
import pe.ecoguardianes.ui.componentes.FilaEstrellas
import pe.ecoguardianes.ui.componentes.TarjetaEco
import pe.ecoguardianes.ui.theme.EcoColores
import pe.ecoguardianes.ui.theme.aColor

private val posiciones = mapOf(
    ZonaId.CASA to Pair(0.20f, 0.80f),
    ZonaId.ESCUELA to Pair(0.50f, 0.70f),
    ZonaId.PARQUE to Pair(0.78f, 0.78f),
    ZonaId.RIO to Pair(0.80f, 0.48f),
    ZonaId.CIUDAD to Pair(0.48f, 0.36f),
    ZonaId.ZONA_INDUSTRIAL to Pair(0.19f, 0.24f)
)

private val iconoZona = mapOf(
    ZonaId.CASA to IconoAmb.MOCHILA,
    ZonaId.ESCUELA to IconoAmb.LIBRO,
    ZonaId.PARQUE to IconoAmb.ARBOL,
    ZonaId.RIO to IconoAmb.PEZ,
    ZonaId.CIUDAD to IconoAmb.AUTO,
    ZonaId.ZONA_INDUSTRIAL to IconoAmb.CHIMENEA
)

/** Pantalla principal: mapa del mundo EcoGuardianes con todo el progreso. */
@Composable
fun MapaPantalla(
    estado: EstadoApp,
    alAbrirZona: (ZonaId) -> Unit,
    alAbrirColeccion: () -> Unit,
    alAbrirInsignias: () -> Unit,
    alAbrirBiblioteca: () -> Unit,
    alAbrirPerfil: () -> Unit
) {
    val juego = estado.juego
    val avatar = CatalogoAvatares.avatar(estado.perfil?.avatarId)
    val estados = DesbloqueoZonas.estadoDeTodas(juego)
    val siguiente = ZonaId.enOrden.firstOrNull {
        estados[it] == EstadoZona.DISPONIBLE || estados[it] == EstadoZona.EN_PROGRESO
    } ?: ZonaId.enOrden.firstOrNull { estados[it] != EstadoZona.DOMINADA }

    Column(
        Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(EcoColores.AzulNiebla, EcoColores.Crema, EcoColores.VerdeNiebla)
                )
            )
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        Spacer(Modifier.height(12.dp))

        // ------------------------------------------------ Cabecera de guardián
        TarjetaEco(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            borde = EcoColores.VerdeHoja.copy(alpha = 0.35f),
            onClick = alAbrirPerfil
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(avatar.colorSecundarioHex.aColor())
                        .border(BorderStroke(3.dp, avatar.colorHex.aColor()), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    EcoIcono(avatar.accesorio, tam = 34.dp)
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(
                        estado.perfil?.alias?.ifBlank { "Guardián" } ?: "Guardián",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            Progresion.rango(juego.xpTotal).simbolo + " " +
                                Progresion.rango(juego.xpTotal).titulo,
                            style = MaterialTheme.typography.labelMedium,
                            color = EcoColores.VerdeSelva
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "Nivel " + Progresion.nivel(juego.xpTotal),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        "" + juego.xpTotal + " XP",
                        style = MaterialTheme.typography.titleMedium,
                        color = EcoColores.NaranjaFuego
                    )
                    Text(
                        "" + juego.hallazgosTotales + " hallazgos",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(Modifier.height(10.dp))
            BarraProgresoEco(
                progreso = Progresion.progresoNivel(juego.xpTotal),
                color = EcoColores.SolAmarillo,
                etiqueta = if (Progresion.xpParaSiguienteNivel(juego.xpTotal) > 0) {
                    "Te faltan " + Progresion.xpParaSiguienteNivel(juego.xpTotal) +
                        " XP para el nivel " + (Progresion.nivel(juego.xpTotal) + 1)
                } else {
                    "Nivel máximo alcanzado"
                }
            )
        }

        Spacer(Modifier.height(12.dp))

        // ------------------------------------------------------- Misión actual
        if (siguiente != null) {
            val mision = CatalogoEscenarios.misionDe(siguiente)
            TarjetaEco(
                modifier = Modifier.fillMaxWidth(),
                color = EcoColores.AmbarSuave,
                borde = EcoColores.SolAmarillo
            ) {
                BurbujaEco(
                    texto = mision.briefingEco,
                    animo = AnimoEco.FELIZ,
                    tamMascota = 66.dp,
                    colorBurbuja = Color.White
                )
                Spacer(Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    EcoIcono(IconoAmb.PORTAPAPELES, tam = 26.dp)
                    Spacer(Modifier.width(8.dp))
                    Text(mision.titulo, style = MaterialTheme.typography.titleMedium)
                }
            }
            Spacer(Modifier.height(12.dp))
        }

        // ----------------------------------------------------------- El mapa
        Text("Mapa de EcoGuardianes", style = MaterialTheme.typography.headlineSmall)
        Text(
            "Toca una zona para ver su misión.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(8.dp))

        MapaMundo(
            juego = juego,
            estados = estados,
            alAbrirZona = alAbrirZona,
            modifier = Modifier
                .fillMaxWidth()
                .height(430.dp)
        )

        Spacer(Modifier.height(14.dp))

        // -------------------------------------------------------- Accesos
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            AccesoRapido(
                "Colección",
                IconoAmb.MOCHILA,
                EcoColores.VerdeHoja,
                Modifier.weight(1f),
                alAbrirColeccion
            )
            AccesoRapido(
                "Insignias",
                IconoAmb.MEDALLA,
                EcoColores.SolAmarillo,
                Modifier.weight(1f),
                alAbrirInsignias
            )
            AccesoRapido(
                "Biblioteca",
                IconoAmb.LIBRO,
                EcoColores.AzulRio,
                Modifier.weight(1f),
                alAbrirBiblioteca
            )
        }
        Spacer(Modifier.height(20.dp))
    }
}

@Composable
private fun AccesoRapido(
    texto: String,
    icono: IconoAmb,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier
            .clip(RoundedCornerShape(20.dp))
            .background(color.copy(alpha = 0.14f))
            .border(BorderStroke(2.dp, color.copy(alpha = 0.5f)), RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EcoIcono(icono, tam = 34.dp)
        Spacer(Modifier.height(6.dp))
        Text(texto, style = MaterialTheme.typography.labelMedium, textAlign = TextAlign.Center)
    }
}

/** Isla ilustrada con las seis zonas y su estado. */
@Composable
fun MapaMundo(
    juego: EstadoJuego,
    estados: Map<ZonaId, EstadoZona>,
    alAbrirZona: (ZonaId) -> Unit,
    modifier: Modifier = Modifier
) {
    val transicion = rememberInfiniteTransition(label = "mapa")
    val pulso by transicion.animateFloat(
        initialValue = 1f,
        targetValue = 1.09f,
        animationSpec = infiniteRepeatable(tween(1200), RepeatMode.Reverse),
        label = "pulso"
    )

    BoxWithConstraints(
        modifier
            .clip(RoundedCornerShape(26.dp))
            .background(EcoColores.AzulNiebla)
            .border(BorderStroke(3.dp, EcoColores.VerdeHoja.copy(alpha = 0.4f)), RoundedCornerShape(26.dp))
    ) {
        val ancho = maxWidth
        val alto = maxHeight

        Canvas(Modifier.fillMaxSize()) {
            // Mar
            drawRect(
                Brush.verticalGradient(
                    listOf(Color(0xFF9FD8F0), Color(0xFF6FC0E4))
                )
            )
            // Isla
            figuraN(
                Color(0xFF74C287),
                0.06f, 0.90f, 0.02f, 0.60f, 0.10f, 0.30f, 0.26f, 0.10f,
                0.55f, 0.06f, 0.82f, 0.14f, 0.96f, 0.38f, 0.94f, 0.72f,
                0.78f, 0.94f, 0.34f, 0.96f
            )
            figuraN(
                Color(0xFF8FD39C),
                0.12f, 0.86f, 0.08f, 0.58f, 0.16f, 0.32f, 0.30f, 0.16f,
                0.56f, 0.12f, 0.80f, 0.20f, 0.90f, 0.40f, 0.88f, 0.70f,
                0.74f, 0.88f, 0.36f, 0.90f
            )
            // Bosque y montañas
            figuraN(Color(0xFF9AA9B0), 0.06f, 0.34f, 0.18f, 0.14f, 0.32f, 0.34f)
            figuraN(Color(0xFFE9F1F4), 0.14f, 0.22f, 0.18f, 0.14f, 0.24f, 0.22f)
            circuloN(0.66f, 0.24f, 0.05f, Color(0xFF4FA96A))
            circuloN(0.74f, 0.30f, 0.04f, Color(0xFF3E8C58))
            circuloN(0.30f, 0.60f, 0.045f, Color(0xFF4FA96A))
            circuloN(0.62f, 0.60f, 0.04f, Color(0xFF3E8C58))
            // Río del mapa
            figuraN(
                Color(0xFF5FB6E0),
                0.86f, 0.36f, 0.92f, 0.40f, 0.78f, 0.66f, 0.62f, 0.86f,
                0.56f, 0.84f, 0.72f, 0.62f
            )
            // Playa
            ovaloN(0.02f, 0.86f, 0.30f, 0.10f, Color(0xFFF0E0B8))

            // Sendero punteado entre zonas
            val orden = ZonaId.enOrden
            for (i in 0 until orden.size - 1) {
                val a = posiciones.getValue(orden[i])
                val b = posiciones.getValue(orden[i + 1])
                lineaN(a.first, a.second, b.first, b.second, Color(0x99FFFFFF), 0.012f, punteada = true)
            }
        }

        ZonaId.enOrden.forEach { zona ->
            val (px, py) = posiciones.getValue(zona)
            val estadoZona = estados[zona] ?: EstadoZona.BLOQUEADA
            val destacada = estadoZona == EstadoZona.DISPONIBLE
            NodoZona(
                zona = zona,
                estadoZona = estadoZona,
                estrellas = juego.estrellasEn(zona),
                requisito = DesbloqueoZonas.requisitoPendiente(zona, juego),
                escala = if (destacada) pulso else 1f,
                onClick = { alAbrirZona(zona) },
                modifier = Modifier.offset(
                    x = ancho * px - 44.dp,
                    y = alto * py - 52.dp
                )
            )
        }
    }
}

@Composable
private fun NodoZona(
    zona: ZonaId,
    estadoZona: EstadoZona,
    estrellas: Int,
    requisito: String?,
    escala: Float,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bloqueada = estadoZona == EstadoZona.BLOQUEADA
    val colorBase = when (estadoZona) {
        EstadoZona.BLOQUEADA -> EcoColores.CarbonSuave
        EstadoZona.DISPONIBLE -> EcoColores.SolAmarillo
        EstadoZona.EN_PROGRESO -> EcoColores.AzulRio
        EstadoZona.COMPLETADA -> EcoColores.VerdeHoja
        EstadoZona.DOMINADA -> EcoColores.NaranjaFuego
    }
    val descripcion = zona.titulo + ", " + estadoZona.etiqueta +
        (if (bloqueada && requisito != null) ". " + requisito else "") +
        (if (!bloqueada) ". " + estrellas + " de 3 estrellas" else "")

    Column(
        modifier
            .width(88.dp)
            .semantics { contentDescription = descripcion },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            Modifier
                .size(62.dp)
                .scale(escala)
                .clip(CircleShape)
                .background(if (bloqueada) Color(0xFFDDE3E6) else Color.White)
                .border(BorderStroke(4.dp, colorBase), CircleShape)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            EcoIcono(iconoZona.getValue(zona), tam = 34.dp)
            Box(
                Modifier
                    .align(Alignment.TopEnd)
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(colorBase),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    estadoZona.simbolo,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White
                )
            }
        }
        Spacer(Modifier.height(2.dp))
        Box(
            Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White.copy(alpha = 0.92f))
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(
                zona.titulo,
                style = MaterialTheme.typography.labelSmall,
                color = EcoColores.Carbon,
                textAlign = TextAlign.Center
            )
        }
        if (!bloqueada) {
            FilaEstrellas(obtenidas = estrellas, tam = 14.dp)
        }
    }
}
