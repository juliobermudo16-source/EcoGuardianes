package pe.ecoguardianes.ui.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import pe.ecoguardianes.data.catalogo.CatalogoAvatares
import pe.ecoguardianes.domain.model.Avatar
import pe.ecoguardianes.domain.model.IconoAmb
import pe.ecoguardianes.ui.art.AnimoEco
import pe.ecoguardianes.ui.art.EcoIcono
import pe.ecoguardianes.ui.art.EcoLogo
import pe.ecoguardianes.ui.art.EcoMascota
import pe.ecoguardianes.ui.componentes.BotonEco
import pe.ecoguardianes.ui.componentes.BotonEcoSuave
import pe.ecoguardianes.ui.componentes.TarjetaEco
import pe.ecoguardianes.ui.theme.EcoColores
import pe.ecoguardianes.ui.theme.aColor

private data class PaginaOnboarding(
    val titulo: String,
    val texto: String,
    val animo: AnimoEco,
    val icono: IconoAmb
)

private val paginas = listOf(
    PaginaOnboarding(
        titulo = "Somos EcoGuardianes",
        texto = "Un equipo de jóvenes que protege el planeta. Recibimos misiones para " +
            "revisar lugares y ayudar a cuidarlos.",
        animo = AnimoEco.FELIZ,
        icono = IconoAmb.ESCUDO
    ),
    PaginaOnboarding(
        titulo = "Yo soy ECO",
        texto = "Te acompaño en cada misión. Te doy pistas, te explico las reglas y " +
            "celebro contigo cada acierto.",
        animo = AnimoEco.NORMAL,
        icono = IconoAmb.LUPA
    ),
    PaginaOnboarding(
        titulo = "Así se investiga",
        texto = "Observa el escenario, toca lo que te llame la atención y decide: " +
            "¿está bien o es una no conformidad?",
        animo = AnimoEco.PENSATIVO,
        icono = IconoAmb.PORTAPAPELES
    ),
    PaginaOnboarding(
        titulo = "Gana y desbloquea",
        texto = "Cada problema resuelto te da XP, estrellas, insignias y nuevas piezas " +
            "para tu colección ambiental.",
        animo = AnimoEco.CELEBRA,
        icono = IconoAmb.MEDALLA
    )
)

/** Onboarding breve: cuatro pantallas y directo al mapa. */
@Composable
fun OnboardingPantalla(alContinuar: () -> Unit) {
    var indice by remember { mutableIntStateOf(0) }
    val pagina = paginas[indice]

    Column(
        Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(EcoColores.VerdeNiebla, EcoColores.Crema, EcoColores.AzulNiebla)
                )
            )
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(20.dp))
        EcoLogo(tam = 74.dp)
        Spacer(Modifier.height(10.dp))

        Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                EcoMascota(tam = 148.dp, animo = pagina.animo)
                Spacer(Modifier.height(18.dp))
                TarjetaEco(
                    color = Color.White,
                    borde = EcoColores.VerdeHoja.copy(alpha = 0.4f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        EcoIcono(pagina.icono, tam = 40.dp)
                        Spacer(Modifier.width(12.dp))
                        Text(pagina.titulo, style = MaterialTheme.typography.headlineSmall)
                    }
                    Spacer(Modifier.height(10.dp))
                    Text(pagina.texto, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }

        Row(
            Modifier.padding(vertical = 18.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            paginas.indices.forEach { i ->
                Box(
                    Modifier
                        .size(width = if (i == indice) 28.dp else 12.dp, height = 12.dp)
                        .clip(CircleShape)
                        .background(
                            if (i == indice) {
                                EcoColores.VerdeHoja
                            } else {
                                EcoColores.VerdeHoja.copy(alpha = 0.28f)
                            }
                        )
                )
            }
        }

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (indice > 0) {
                BotonEcoSuave("Atrás", onClick = { indice-- })
            } else {
                BotonEcoSuave("Saltar", onClick = alContinuar)
            }
            BotonEco(
                texto = if (indice == paginas.lastIndex) "¡Vamos!" else "Siguiente",
                icono = if (indice == paginas.lastIndex) IconoAmb.MAPA else null,
                onClick = { if (indice == paginas.lastIndex) alContinuar() else indice++ }
            )
        }
        Spacer(Modifier.height(8.dp))
    }
}

/** Creación del perfil: solo alias inventado y avatar. Nada personal. */
@Composable
fun PerfilCrearPantalla(
    aliasInicial: String = "",
    avatarInicial: String = CatalogoAvatares.predeterminado.id,
    alConfirmar: (String, String) -> Unit
) {
    var alias by remember { mutableStateOf(aliasInicial) }
    var avatarId by remember { mutableStateOf(avatarInicial) }

    Column(
        Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(EcoColores.AzulNiebla, EcoColores.Crema))
            )
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(16.dp))
        EcoMascota(tam = 110.dp, animo = AnimoEco.FELIZ)
        Text(
            "¿Cómo te llamamos, guardián?",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(6.dp))
        Text(
            "Elige un apodo inventado. No necesitamos tu nombre real ni ningún otro dato.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(18.dp))

        OutlinedTextField(
            value = alias,
            onValueChange = { if (it.length <= 16) alias = it },
            label = { Text("Tu apodo de guardián") },
            singleLine = true,
            shape = RoundedCornerShape(18.dp),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            "" + alias.length + "/16",
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.fillMaxWidth().padding(end = 6.dp, top = 4.dp),
            textAlign = TextAlign.End,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(14.dp))
        Text(
            "Elige tu avatar",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(CatalogoAvatares.avatares, key = { it.id }) { avatar ->
                AvatarSeleccionable(
                    avatar = avatar,
                    seleccionado = avatar.id == avatarId,
                    onClick = { avatarId = avatar.id }
                )
            }
        }

        Spacer(Modifier.height(12.dp))
        BotonEco(
            texto = "Entrar al mapa",
            icono = IconoAmb.MAPA,
            habilitado = alias.isNotBlank(),
            modifier = Modifier.fillMaxWidth(),
            onClick = { alConfirmar(alias.trim(), avatarId) }
        )
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
fun AvatarSeleccionable(
    avatar: Avatar,
    seleccionado: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val color = avatar.colorHex.aColor()
    Column(
        modifier
            .clip(RoundedCornerShape(20.dp))
            .background(avatar.colorSecundarioHex.aColor())
            .border(
                BorderStroke(if (seleccionado) 4.dp else 2.dp, if (seleccionado) color else color.copy(alpha = 0.3f)),
                RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            Modifier.size(46.dp).clip(CircleShape).background(color.copy(alpha = 0.18f)),
            contentAlignment = Alignment.Center
        ) {
            EcoIcono(avatar.accesorio, tam = 32.dp)
        }
        Spacer(Modifier.height(4.dp))
        Text(avatar.nombre, style = MaterialTheme.typography.labelSmall, color = color)
    }
}
