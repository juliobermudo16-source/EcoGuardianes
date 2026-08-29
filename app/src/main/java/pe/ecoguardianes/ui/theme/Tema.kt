package pe.ecoguardianes.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat

private val EsquemaClaro = lightColorScheme(
    primary = EcoColores.VerdeHoja,
    onPrimary = Color.White,
    primaryContainer = EcoColores.VerdeNiebla,
    onPrimaryContainer = EcoColores.VerdeSelva,
    secondary = EcoColores.AzulRio,
    onSecondary = Color.White,
    secondaryContainer = EcoColores.AzulNiebla,
    onSecondaryContainer = EcoColores.AzulProfundo,
    tertiary = EcoColores.SolAmarillo,
    onTertiary = EcoColores.Carbon,
    tertiaryContainer = EcoColores.AmbarSuave,
    onTertiaryContainer = EcoColores.Tierra,
    background = EcoColores.Crema,
    onBackground = EcoColores.Carbon,
    surface = Color.White,
    onSurface = EcoColores.Carbon,
    surfaceVariant = EcoColores.CremaHonda,
    onSurfaceVariant = EcoColores.CarbonSuave,
    error = EcoColores.CoralAlerta,
    onError = Color.White,
    errorContainer = EcoColores.CoralSuave,
    onErrorContainer = EcoColores.CoralAlerta,
    outline = EcoColores.Arena,
    outlineVariant = EcoColores.CremaHonda
)

private val EsquemaOscuro = darkColorScheme(
    primary = EcoColores.VerdeBrote,
    onPrimary = EcoColores.NocheProfunda,
    primaryContainer = EcoColores.VerdeSelva,
    onPrimaryContainer = EcoColores.VerdeNiebla,
    secondary = EcoColores.AzulCielo,
    onSecondary = EcoColores.NocheProfunda,
    secondaryContainer = EcoColores.AzulProfundo,
    onSecondaryContainer = EcoColores.AzulNiebla,
    tertiary = EcoColores.SolAmarillo,
    onTertiary = EcoColores.NocheProfunda,
    tertiaryContainer = EcoColores.Tierra,
    onTertiaryContainer = EcoColores.AmbarSuave,
    background = EcoColores.NocheProfunda,
    onBackground = EcoColores.ArenaClara,
    surface = EcoColores.NocheSuperficie,
    onSurface = EcoColores.ArenaClara,
    surfaceVariant = EcoColores.NocheBorde,
    onSurfaceVariant = EcoColores.Arena,
    error = Color(0xFFFF8A98),
    onError = EcoColores.NocheProfunda,
    errorContainer = Color(0xFF5C1F27),
    onErrorContainer = Color(0xFFFFD9DE),
    outline = EcoColores.NocheBorde,
    outlineVariant = EcoColores.NocheBorde
)

/**
 * Tipografía pensada para lectores de 8 a 12 años: cuerpos grandes,
 * titulares con mucho peso y líneas generosas.
 */
private fun tipografiaEco(escala: Float): Typography {
    val fuente = FontFamily.SansSerif
    fun sp(v: Float) = (v * escala).sp
    return Typography(
        displayLarge = TextStyle(
            fontFamily = fuente, fontWeight = FontWeight.Black,
            fontSize = sp(40f), lineHeight = sp(46f), letterSpacing = (-0.5).sp
        ),
        displayMedium = TextStyle(
            fontFamily = fuente, fontWeight = FontWeight.Black,
            fontSize = sp(32f), lineHeight = sp(38f)
        ),
        headlineLarge = TextStyle(
            fontFamily = fuente, fontWeight = FontWeight.ExtraBold,
            fontSize = sp(28f), lineHeight = sp(34f)
        ),
        headlineMedium = TextStyle(
            fontFamily = fuente, fontWeight = FontWeight.ExtraBold,
            fontSize = sp(23f), lineHeight = sp(29f)
        ),
        headlineSmall = TextStyle(
            fontFamily = fuente, fontWeight = FontWeight.Bold,
            fontSize = sp(20f), lineHeight = sp(26f)
        ),
        titleLarge = TextStyle(
            fontFamily = fuente, fontWeight = FontWeight.Bold,
            fontSize = sp(19f), lineHeight = sp(25f)
        ),
        titleMedium = TextStyle(
            fontFamily = fuente, fontWeight = FontWeight.Bold,
            fontSize = sp(17f), lineHeight = sp(23f)
        ),
        titleSmall = TextStyle(
            fontFamily = fuente, fontWeight = FontWeight.SemiBold,
            fontSize = sp(15f), lineHeight = sp(21f)
        ),
        bodyLarge = TextStyle(
            fontFamily = fuente, fontWeight = FontWeight.Normal,
            fontSize = sp(17f), lineHeight = sp(25f)
        ),
        bodyMedium = TextStyle(
            fontFamily = fuente, fontWeight = FontWeight.Normal,
            fontSize = sp(15f), lineHeight = sp(22f)
        ),
        bodySmall = TextStyle(
            fontFamily = fuente, fontWeight = FontWeight.Normal,
            fontSize = sp(13f), lineHeight = sp(19f)
        ),
        labelLarge = TextStyle(
            fontFamily = fuente, fontWeight = FontWeight.Bold,
            fontSize = sp(15f), lineHeight = sp(20f), letterSpacing = 0.4.sp
        ),
        labelMedium = TextStyle(
            fontFamily = fuente, fontWeight = FontWeight.Bold,
            fontSize = sp(13f), lineHeight = sp(18f), letterSpacing = 0.4.sp
        ),
        labelSmall = TextStyle(
            fontFamily = fuente, fontWeight = FontWeight.SemiBold,
            fontSize = sp(11f), lineHeight = sp(15f), letterSpacing = 0.5.sp
        )
    )
}

private val FormasEco = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(14.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(28.dp),
    extraLarge = RoundedCornerShape(36.dp)
)

/** Preferencias de accesibilidad y sonido disponibles en toda la interfaz. */
data class PreferenciasEco(
    val sonido: Boolean = true,
    val haptica: Boolean = true,
    val textoGrande: Boolean = false,
    val pistas: Boolean = true
)

val LocalPreferenciasEco = compositionLocalOf { PreferenciasEco() }

/** Estilo centrado de uso frecuente en los diálogos de ECO. */
val TextoCentrado = TextStyle(textAlign = TextAlign.Center)

@Composable
fun EcoGuardianesTema(
    oscuro: Boolean = isSystemInDarkTheme(),
    preferencias: PreferenciasEco = PreferenciasEco(),
    contenido: @Composable () -> Unit
) {
    val esquema = if (oscuro) EsquemaOscuro else EsquemaClaro
    val vista = LocalView.current
    if (!vista.isInEditMode) {
        SideEffect {
            val ventana = (vista.context as? Activity)?.window ?: return@SideEffect
            WindowCompat.getInsetsController(ventana, vista)
                .isAppearanceLightStatusBars = !oscuro
        }
    }
    CompositionLocalProvider(LocalPreferenciasEco provides preferencias) {
        MaterialTheme(
            colorScheme = esquema,
            typography = tipografiaEco(if (preferencias.textoGrande) 1.18f else 1f),
            shapes = FormasEco,
            content = contenido
        )
    }
}
