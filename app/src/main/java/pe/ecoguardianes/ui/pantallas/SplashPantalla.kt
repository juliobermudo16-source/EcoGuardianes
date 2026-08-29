package pe.ecoguardianes.ui.pantallas

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import kotlinx.coroutines.delay
import pe.ecoguardianes.ui.art.EcoLogo
import pe.ecoguardianes.ui.componentes.BarraProgresoEco
import pe.ecoguardianes.ui.theme.EcoColores

/** Pantalla de arranque con la identidad de EcoGuardianes. */
@Composable
fun SplashPantalla(alTerminar: () -> Unit) {
    var visible by remember { mutableStateOf(false) }
    val escala by animateFloatAsState(
        targetValue = if (visible) 1f else 0.7f,
        animationSpec = tween(650),
        label = "escalaLogo"
    )
    val opacidad by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(650),
        label = "opacidadLogo"
    )

    LaunchedEffect(Unit) {
        visible = true
        delay(1500)
        alTerminar()
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        EcoColores.VerdeSelva,
                        EcoColores.VerdeHoja,
                        EcoColores.AzulRio
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
        ) {
            EcoLogo(
                tam = 168.dp,
                modifier = Modifier
                    .scale(escala)
                    .alpha(opacidad)
            )
            Spacer(Modifier.height(20.dp))
            Text(
                "EcoGuardianes",
                style = MaterialTheme.typography.displayMedium,
                color = Color.White,
                modifier = Modifier.alpha(opacidad)
            )
            Spacer(Modifier.height(6.dp))
            Text(
                "Detecta, aprende y protege.",
                style = MaterialTheme.typography.titleMedium,
                color = EcoColores.AmbarSuave,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(opacidad)
            )
            Spacer(Modifier.height(36.dp))
            BarraProgresoEco(
                progreso = if (visible) 1f else 0f,
                color = EcoColores.SolAmarillo,
                alto = 10.dp,
                modifier = Modifier
                    .padding(horizontal = 48.dp)
                    .alpha(opacidad)
            )
        }
    }
}
