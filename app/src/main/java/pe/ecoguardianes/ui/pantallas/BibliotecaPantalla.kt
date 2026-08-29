package pe.ecoguardianes.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import pe.ecoguardianes.data.catalogo.CatalogoReglas
import pe.ecoguardianes.domain.model.Categoria
import pe.ecoguardianes.domain.model.IconoAmb
import pe.ecoguardianes.ui.art.AnimoEco
import pe.ecoguardianes.ui.art.EcoIcono
import pe.ecoguardianes.ui.componentes.BurbujaEco
import pe.ecoguardianes.ui.componentes.ChipCategoria
import pe.ecoguardianes.ui.componentes.EncabezadoEco
import pe.ecoguardianes.ui.componentes.PanelPlegable
import pe.ecoguardianes.ui.theme.EcoColores

/** Biblioteca ambiental: las reglas que el guardián va aprendiendo. */
@Composable
fun BibliotecaPantalla(alVolver: () -> Unit) {
    var filtro by remember { mutableStateOf<Categoria?>(null) }
    var abierta by remember { mutableStateOf<String?>(null) }
    val reglas = remember(filtro) {
        filtro?.let { CatalogoReglas.porCategoria(it) } ?: CatalogoReglas.reglas
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(EcoColores.AzulNiebla, EcoColores.Crema))
            )
            .padding(horizontal = 16.dp)
    ) {
        Spacer(Modifier.height(8.dp))
        EncabezadoEco(
            titulo = "Biblioteca ambiental",
            subtitulo = "" + CatalogoReglas.reglas.size + " reglas para proteger el planeta",
            onVolver = alVolver
        )

        BurbujaEco(
            "Cada problema tiene una regla detrás. Aquí las tienes todas explicadas.",
            animo = AnimoEco.NORMAL,
            tamMascota = 58.dp
        )
        Spacer(Modifier.height(10.dp))

        Row(
            Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FiltroTodas(activo = filtro == null) { filtro = null }
            Categoria.entries.forEach { categoria ->
                ChipCategoria(
                    categoria = categoria,
                    compacto = true,
                    modifier = Modifier.clickable {
                        filtro = if (filtro == categoria) null else categoria
                    }
                )
            }
        }

        Spacer(Modifier.height(10.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(reglas, key = { it.id }) { regla ->
                PanelPlegable(
                    titulo = regla.titulo,
                    expandido = abierta == regla.id,
                    icono = IconoAmb.LIBRO,
                    onCambiar = { abierta = if (abierta == regla.id) null else regla.id }
                ) {
                    FichaRegla(regla)
                }
            }
            item { Spacer(Modifier.height(24.dp)) }
        }
    }
}

@Composable
private fun FiltroTodas(activo: Boolean, onClick: () -> Unit) {
    Row(
        Modifier
            .clickable(onClick = onClick)
            .background(
                if (activo) EcoColores.VerdeHoja.copy(alpha = 0.2f) else EcoColores.CremaHonda,
                CircleShape
            )
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        EcoIcono(IconoAmb.LIBRO, tam = 18.dp)
        Spacer(Modifier.width(6.dp))
        Text("Todas", style = MaterialTheme.typography.labelMedium)
    }
}
