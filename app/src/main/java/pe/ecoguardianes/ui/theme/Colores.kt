package pe.ecoguardianes.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Paleta propia de EcoGuardianes: naturaleza, agua, tierra y energía limpia.
 * No se usa el color por defecto de Material 3 como identidad visual.
 */
object EcoColores {
    // Verdes (vegetación)
    val VerdeSelva = Color(0xFF14573C)
    val VerdeHoja = Color(0xFF2E9E5B)
    val VerdeBrote = Color(0xFF6FCF7F)
    val VerdeNiebla = Color(0xFFE3F4E7)

    // Azules (agua y cielo)
    val AzulProfundo = Color(0xFF0F4C75)
    val AzulRio = Color(0xFF1E88C7)
    val AzulCielo = Color(0xFF6FB3D9)
    val AzulNiebla = Color(0xFFE1F1FA)

    // Tierra y arena
    val Tierra = Color(0xFF6B4A2F)
    val Arena = Color(0xFFE9D5AC)
    val ArenaClara = Color(0xFFF7ECD6)

    // Energía y sol
    val SolAmarillo = Color(0xFFF2B705)
    val NaranjaFuego = Color(0xFFE8722B)
    val AmbarSuave = Color(0xFFFFF0C6)

    // Alertas educativas
    val CoralAlerta = Color(0xFFD1495B)
    val CoralSuave = Color(0xFFFBE3E6)
    val AmbarObservacion = Color(0xFFE8A020)

    // Neutros
    val Crema = Color(0xFFFDF8EE)
    val CremaHonda = Color(0xFFF3EADA)
    val Carbon = Color(0xFF16302A)
    val CarbonSuave = Color(0xFF4A5D57)
    val Blanco = Color(0xFFFFFFFF)

    // Superficies oscuras
    val NocheProfunda = Color(0xFF0C1F1A)
    val NocheSuperficie = Color(0xFF152F28)
    val NocheBorde = Color(0xFF24463C)

    // Morado auditor (herramientas, detective)
    val MoradoLupa = Color(0xFF8E5FD9)
    val MoradoNiebla = Color(0xFFEDE4FB)
}

/** Convierte un color declarado en el dominio (Long ARGB) a Color de Compose. */
fun Long.aColor(): Color = Color(this)
