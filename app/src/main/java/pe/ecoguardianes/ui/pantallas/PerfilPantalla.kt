package pe.ecoguardianes.ui.pantallas

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pe.ecoguardianes.data.catalogo.CatalogoAvatares
import pe.ecoguardianes.data.catalogo.CatalogoColeccion
import pe.ecoguardianes.data.catalogo.CatalogoInsignias
import pe.ecoguardianes.domain.audit.EvaluadorRecompensas
import pe.ecoguardianes.domain.audit.Progresion
import pe.ecoguardianes.domain.model.Categoria
import pe.ecoguardianes.domain.model.IconoAmb
import pe.ecoguardianes.domain.model.ZonaId
import pe.ecoguardianes.ui.art.AnimoEco
import pe.ecoguardianes.ui.art.EcoIcono
import pe.ecoguardianes.ui.componentes.BarraProgresoEco
import pe.ecoguardianes.ui.componentes.BotonEcoSuave
import pe.ecoguardianes.ui.componentes.BurbujaEco
import pe.ecoguardianes.ui.componentes.ChipCategoria
import pe.ecoguardianes.ui.componentes.CifraEco
import pe.ecoguardianes.ui.componentes.EncabezadoEco
import pe.ecoguardianes.ui.componentes.FilaEstrellas
import pe.ecoguardianes.ui.componentes.TarjetaEco
import pe.ecoguardianes.ui.theme.EcoColores
import pe.ecoguardianes.ui.theme.aColor

/** Perfil del guardián: identidad local y estadísticas calculadas. */
@Composable
fun PerfilPantalla(
    estado: EstadoApp,
    alVolver: () -> Unit,
    alAbrirAjustes: () -> Unit,
    alCambiarAvatar: (String) -> Unit
) {
    val juego = estado.juego
    val avatar = CatalogoAvatares.avatar(estado.perfil?.avatarId)
    val insignias = remember(juego) {
        EvaluadorRecompensas.evaluarInsignias(juego, CatalogoInsignias.insignias)
            .count { it.desbloqueada }
    }
    val piezas = remember(juego) {
        EvaluadorRecompensas.evaluarColeccionables(juego, CatalogoColeccion.coleccionables)
            .count { it.desbloqueada }
    }
    var eligiendoAvatar by remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(EcoColores.MoradoNiebla, EcoColores.Crema)))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        Spacer(Modifier.height(8.dp))
        EncabezadoEco(
            titulo = "Mi perfil",
            subtitulo = "Todo se guarda solo en este dispositivo",
            onVolver = alVolver,
            accion = {
                BotonEcoSuave(
                    texto = "Ajustes",
                    icono = IconoAmb.RELOJ,
                    color = EcoColores.CarbonSuave,
                    onClick = alAbrirAjustes
                )
            }
        )

        TarjetaEco(modifier = Modifier.fillMaxWidth(), color = Color.White) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .size(78.dp)
                        .clip(CircleShape)
                        .background(avatar.colorSecundarioHex.aColor())
                        .border(BorderStroke(4.dp, avatar.colorHex.aColor()), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    EcoIcono(avatar.accesorio, tam = 46.dp)
                }
                Spacer(Modifier.width(14.dp))
                Column(Modifier.weight(1f)) {
                    Text(
                        estado.perfil?.alias?.ifBlank { "Guardián" } ?: "Guardián",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        Progresion.rango(juego.xpTotal).simbolo + " " +
                            Progresion.rango(juego.xpTotal).titulo,
                        style = MaterialTheme.typography.titleSmall,
                        color = EcoColores.VerdeSelva
                    )
                    Text(
                        Progresion.rango(juego.xpTotal).descripcion,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
            BarraProgresoEco(
                progreso = Progresion.progresoNivel(juego.xpTotal),
                color = EcoColores.SolAmarillo,
                etiqueta = "Nivel " + Progresion.nivel(juego.xpTotal) + " · " +
                    juego.xpTotal + " XP"
            )
            Spacer(Modifier.height(10.dp))
            BotonEcoSuave(
                texto = "Cambiar avatar",
                icono = IconoAmb.MOCHILA,
                color = avatar.colorHex.aColor(),
                modifier = Modifier.fillMaxWidth(),
                onClick = { eligiendoAvatar = true }
            )
        }

        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CifraEco(
                "" + juego.auditoriasCompletadas,
                "auditorías",
                EcoColores.AzulRio,
                Modifier.weight(1f)
            )
            CifraEco(
                "" + juego.hallazgosTotales,
                "hallazgos",
                EcoColores.CoralAlerta,
                Modifier.weight(1f)
            )
            CifraEco("" + insignias, "insignias", EcoColores.SolAmarillo, Modifier.weight(1f))
            CifraEco("" + piezas, "colección", EcoColores.VerdeHoja, Modifier.weight(1f))
        }

        Spacer(Modifier.height(14.dp))
        TarjetaEco(modifier = Modifier.fillMaxWidth(), color = Color.White) {
            Text("Hallazgos por categoría", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(10.dp))
            val maximo = (juego.hallazgosPorCategoria.values.maxOrNull() ?: 1).coerceAtLeast(1)
            Categoria.entries.forEach { categoria ->
                val valor = juego.hallazgosPorCategoria[categoria] ?: 0
                Row(verticalAlignment = Alignment.CenterVertically) {
                    ChipCategoria(categoria, compacto = true, modifier = Modifier.width(150.dp))
                    Spacer(Modifier.width(8.dp))
                    BarraProgresoEco(
                        progreso = valor.toFloat() / maximo,
                        color = categoria.colorHex.aColor(),
                        alto = 10.dp,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("" + valor, style = MaterialTheme.typography.labelMedium)
                }
                Spacer(Modifier.height(6.dp))
            }
        }

        Spacer(Modifier.height(14.dp))
        TarjetaEco(modifier = Modifier.fillMaxWidth(), color = Color.White) {
            Text("Progreso por zona", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            ZonaId.enOrden.forEach { zona ->
                val resumen = juego.zonas[zona]
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        zona.titulo,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.width(130.dp)
                    )
                    FilaEstrellas(resumen?.estrellas ?: 0, tam = 18.dp)
                    Spacer(Modifier.width(10.dp))
                    Text(
                        if (resumen != null) "" + resumen.mejorPuntaje + "%" else "—",
                        style = MaterialTheme.typography.labelMedium,
                        color = EcoColores.VerdeSelva
                    )
                }
                Spacer(Modifier.height(6.dp))
            }
        }
        Spacer(Modifier.height(24.dp))
    }

    if (eligiendoAvatar) {
        AlertDialog(
            onDismissRequest = { eligiendoAvatar = false },
            confirmButton = {
                TextButton(onClick = { eligiendoAvatar = false }) { Text("Listo") }
            },
            title = { Text("Elige tu avatar") },
            text = {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.height(220.dp)
                ) {
                    items(CatalogoAvatares.avatares, key = { it.id }) { opcion ->
                        AvatarSeleccionable(
                            avatar = opcion,
                            seleccionado = opcion.id == avatar.id,
                            onClick = { alCambiarAvatar(opcion.id) }
                        )
                    }
                }
            }
        )
    }
}

/** Ajustes: sonido, accesibilidad, privacidad y reinicio del progreso. */
@Composable
fun AjustesPantalla(
    estado: EstadoApp,
    alVolver: () -> Unit,
    alCambiarAjuste: (Boolean?, Boolean?, Boolean?, Boolean?) -> Unit,
    alReiniciar: () -> Unit
) {
    var confirmando by remember { mutableStateOf(false) }
    val preferencias = estado.preferencias

    Column(
        Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(EcoColores.AzulNiebla, EcoColores.Crema)))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        Spacer(Modifier.height(8.dp))
        EncabezadoEco(titulo = "Configuración", onVolver = alVolver)

        TarjetaEco(modifier = Modifier.fillMaxWidth(), color = Color.White) {
            FilaAjuste(
                "Sonido",
                "Efectos suaves al acertar y desbloquear.",
                IconoAmb.ALTAVOZ,
                preferencias.sonido
            ) { alCambiarAjuste(it, null, null, null) }
            FilaAjuste(
                "Vibración",
                "Pequeña vibración al colocar piezas.",
                IconoAmb.HUELLA,
                preferencias.haptica
            ) { alCambiarAjuste(null, it, null, null) }
            FilaAjuste(
                "Texto grande",
                "Aumenta el tamaño de todas las letras.",
                IconoAmb.LIBRO,
                preferencias.textoGrande
            ) { alCambiarAjuste(null, null, it, null) }
            FilaAjuste(
                "Pistas de ECO",
                "ECO ofrece pistas cuando te atascas.",
                IconoAmb.LUPA,
                preferencias.pistas
            ) { alCambiarAjuste(null, null, null, it) }
        }

        Spacer(Modifier.height(14.dp))
        TarjetaEco(
            modifier = Modifier.fillMaxWidth(),
            color = EcoColores.VerdeNiebla,
            borde = EcoColores.VerdeHoja.copy(alpha = 0.5f)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                EcoIcono(IconoAmb.ESCUDO, tam = 30.dp)
                Spacer(Modifier.width(10.dp))
                Text("Privacidad", style = MaterialTheme.typography.titleMedium)
            }
            Spacer(Modifier.height(8.dp))
            Text(
                "EcoGuardianes funciona sin Internet. No pide nombre real, correo, " +
                    "teléfono, dirección ni ubicación. No hay anuncios ni seguimiento, y " +
                    "todo tu progreso se guarda únicamente en este dispositivo.",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(Modifier.height(14.dp))
        BurbujaEco(
            "Si reinicias, empezamos de cero: perderás XP, insignias y colección.",
            animo = AnimoEco.PENSATIVO,
            tamMascota = 56.dp
        )
        Spacer(Modifier.height(10.dp))
        BotonEcoSuave(
            texto = "Reiniciar mi progreso",
            icono = IconoAmb.RELOJ,
            color = EcoColores.CoralAlerta,
            modifier = Modifier.fillMaxWidth(),
            onClick = { confirmando = true }
        )

        Spacer(Modifier.height(20.dp))
        Text(
            "EcoGuardianes v1.0.0 · Detecta, aprende y protege.",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(24.dp))
    }

    if (confirmando) {
        AlertDialog(
            onDismissRequest = { confirmando = false },
            title = { Text("¿Reiniciar el progreso?") },
            text = {
                Text(
                    "Se borrarán tus auditorías, hallazgos, XP, insignias y colección. " +
                        "Esta acción no se puede deshacer."
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    alReiniciar()
                    confirmando = false
                }) { Text("Sí, reiniciar") }
            },
            dismissButton = {
                TextButton(onClick = { confirmando = false }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
private fun FilaAjuste(
    titulo: String,
    descripcion: String,
    icono: IconoAmb,
    valor: Boolean,
    onCambiar: (Boolean) -> Unit
) {
    Row(
        Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        EcoIcono(icono, tam = 28.dp)
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(titulo, style = MaterialTheme.typography.titleSmall)
            Text(
                descripcion,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(checked = valor, onCheckedChange = onCambiar)
    }
}
