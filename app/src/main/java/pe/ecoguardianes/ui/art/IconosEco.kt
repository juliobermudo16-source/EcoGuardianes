package pe.ecoguardianes.ui.art

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import pe.ecoguardianes.domain.model.IconoAmb

private val Verde = Color(0xFF2E9E5B)
private val VerdeOsc = Color(0xFF14573C)
private val VerdeClaro = Color(0xFF6FCF7F)
private val Azul = Color(0xFF1E88C7)
private val AzulOsc = Color(0xFF0F4C75)
private val AzulClaro = Color(0xFF9BD4F0)
private val Amarillo = Color(0xFFF2B705)
private val Naranja = Color(0xFFE8722B)
private val Rojo = Color(0xFFD1495B)
private val Tierra = Color(0xFF6B4A2F)
private val Arena = Color(0xFFE9D5AC)
private val Gris = Color(0xFF9AA5AC)
private val GrisOsc = Color(0xFF5A6B73)
private val Blanco = Color(0xFFFFFFFF)
private val Crema = Color(0xFFFDF8EE)
private val Morado = Color(0xFF8E5FD9)
private val Humo = Color(0xFF7C8A91)

/** Nombre accesible de cada icono, para lectores de pantalla. */
fun descripcionDe(icono: IconoAmb): String = when (icono) {
    IconoAmb.BOLSA_BASURA -> "Bolsa de basura"
    IconoAmb.CONTENEDOR -> "Contenedor de residuos"
    IconoAmb.BOTELLA -> "Botella de plástico"
    IconoAmb.PAPEL -> "Papel"
    IconoAmb.LATA -> "Lata"
    IconoAmb.ORGANICO -> "Residuo orgánico"
    IconoAmb.PILA -> "Pila usada"
    IconoAmb.VIDRIO -> "Frasco de vidrio"
    IconoAmb.GRIFO -> "Caño abierto"
    IconoAmb.GOTA -> "Gota de agua"
    IconoAmb.CHARCO -> "Agua estancada"
    IconoAmb.TUBERIA -> "Tubería de descarga"
    IconoAmb.MANGUERA -> "Manguera de riego"
    IconoAmb.HUMO -> "Humo"
    IconoAmb.CHIMENEA -> "Chimenea industrial"
    IconoAmb.AUTO -> "Vehículo"
    IconoAmb.POLVO -> "Polvo en el aire"
    IconoAmb.ALTAVOZ -> "Altavoz con volumen alto"
    IconoAmb.BOCINA -> "Bocina"
    IconoAmb.SILENCIO -> "Silencio"
    IconoAmb.ARBOL -> "Árbol sano"
    IconoAmb.ARBOL_SECO -> "Árbol dañado"
    IconoAmb.FLOR -> "Flor"
    IconoAmb.CESPED -> "Césped"
    IconoAmb.PAJARO -> "Ave"
    IconoAmb.PEZ -> "Pez"
    IconoAmb.MARIPOSA -> "Mariposa"
    IconoAmb.ABEJA -> "Abeja"
    IconoAmb.RANA -> "Rana"
    IconoAmb.NIDO -> "Nido"
    IconoAmb.BOMBILLA -> "Foco encendido"
    IconoAmb.ENCHUFE -> "Enchufe"
    IconoAmb.PANEL_SOLAR -> "Panel solar"
    IconoAmb.VENTILADOR -> "Ventilador"
    IconoAmb.PANTALLA -> "Pantalla encendida"
    IconoAmb.LUPA -> "Lupa"
    IconoAmb.PORTAPAPELES -> "Ficha de auditoría"
    IconoAmb.ESCUDO -> "Escudo protector"
    IconoAmb.ESTRELLA -> "Estrella"
    IconoAmb.MAPA -> "Mapa"
    IconoAmb.LIBRO -> "Libro"
    IconoAmb.MEDALLA -> "Medalla"
    IconoAmb.MOCHILA -> "Mochila de campo"
    IconoAmb.CASCO -> "Casco de seguridad"
    IconoAmb.CUADERNO -> "Cuaderno de campo"
    IconoAmb.HUELLA -> "Huella"
    IconoAmb.RELOJ -> "Reloj"
}

/** Icono ambiental dibujado íntegramente con Canvas. */
@Composable
fun EcoIcono(
    icono: IconoAmb,
    modifier: Modifier = Modifier,
    tam: Dp = 34.dp
) {
    val descripcion = descripcionDe(icono)
    Canvas(
        modifier
            .size(tam)
            .semantics { contentDescription = descripcion }
    ) {
        dibujarIcono(icono)
    }
}

@Suppress("LongMethod", "CyclomaticComplexMethod")
fun DrawScope.dibujarIcono(icono: IconoAmb) {
    when (icono) {

        // ------------------------------------------------------- Residuos
        IconoAmb.BOLSA_BASURA -> {
            figuraN(GrisOsc, 0.28f, 0.30f, 0.72f, 0.30f, 0.82f, 0.90f, 0.18f, 0.90f)
            figuraN(Gris, 0.34f, 0.30f, 0.66f, 0.30f, 0.62f, 0.16f, 0.38f, 0.16f)
            lineaN(0.40f, 0.44f, 0.38f, 0.80f, Color(0x33000000), 0.035f)
            lineaN(0.58f, 0.44f, 0.60f, 0.80f, Color(0x33000000), 0.035f)
        }
        IconoAmb.CONTENEDOR -> {
            cajaN(0.20f, 0.30f, 0.60f, 0.58f, Verde, 0.06f)
            cajaN(0.14f, 0.20f, 0.72f, 0.12f, VerdeOsc, 0.05f)
            cajaN(0.42f, 0.12f, 0.16f, 0.08f, VerdeOsc, 0.03f)
            lineaN(0.36f, 0.42f, 0.36f, 0.78f, Color(0x40FFFFFF), 0.05f)
            lineaN(0.50f, 0.42f, 0.50f, 0.78f, Color(0x40FFFFFF), 0.05f)
            lineaN(0.64f, 0.42f, 0.64f, 0.78f, Color(0x40FFFFFF), 0.05f)
        }
        IconoAmb.BOTELLA -> {
            cajaN(0.42f, 0.08f, 0.16f, 0.12f, AzulClaro, 0.03f)
            figuraN(AzulClaro, 0.36f, 0.24f, 0.64f, 0.24f, 0.70f, 0.42f, 0.70f, 0.88f, 0.30f, 0.88f, 0.30f, 0.42f)
            cajaN(0.40f, 0.06f, 0.20f, 0.06f, Azul, 0.02f)
            cajaN(0.34f, 0.54f, 0.32f, 0.14f, Blanco.copy(alpha = 0.65f), 0.02f)
        }
        IconoAmb.PAPEL -> {
            figuraN(Crema, 0.24f, 0.12f, 0.64f, 0.12f, 0.78f, 0.28f, 0.78f, 0.88f, 0.24f, 0.88f)
            figuraN(Arena, 0.64f, 0.12f, 0.78f, 0.28f, 0.64f, 0.28f)
            lineaN(0.32f, 0.44f, 0.68f, 0.44f, Gris, 0.04f)
            lineaN(0.32f, 0.58f, 0.68f, 0.58f, Gris, 0.04f)
            lineaN(0.32f, 0.72f, 0.56f, 0.72f, Gris, 0.04f)
        }
        IconoAmb.LATA -> {
            cajaN(0.30f, 0.18f, 0.40f, 0.64f, Gris, 0.06f)
            ovaloN(0.30f, 0.12f, 0.40f, 0.14f, Blanco)
            ovaloN(0.30f, 0.75f, 0.40f, 0.14f, GrisOsc)
            cajaN(0.30f, 0.38f, 0.40f, 0.16f, Rojo)
        }
        IconoAmb.ORGANICO -> {
            figuraN(Amarillo, 0.30f, 0.22f, 0.46f, 0.16f, 0.58f, 0.44f, 0.52f, 0.84f, 0.34f, 0.78f)
            figuraN(Naranja, 0.52f, 0.22f, 0.70f, 0.20f, 0.72f, 0.52f, 0.58f, 0.82f, 0.52f, 0.60f)
            lineaN(0.50f, 0.16f, 0.56f, 0.06f, VerdeOsc, 0.05f)
            circuloN(0.60f, 0.08f, 0.07f, VerdeClaro)
        }
        IconoAmb.PILA -> {
            cajaN(0.26f, 0.26f, 0.48f, 0.58f, Rojo, 0.05f)
            cajaN(0.42f, 0.16f, 0.16f, 0.10f, GrisOsc, 0.03f)
            lineaN(0.36f, 0.46f, 0.50f, 0.46f, Blanco, 0.06f)
            lineaN(0.43f, 0.39f, 0.43f, 0.53f, Blanco, 0.06f)
            lineaN(0.56f, 0.46f, 0.68f, 0.46f, Blanco, 0.06f)
        }
        IconoAmb.VIDRIO -> {
            cajaN(0.36f, 0.08f, 0.28f, 0.14f, VerdeClaro, 0.03f)
            figuraN(AzulClaro, 0.30f, 0.26f, 0.70f, 0.26f, 0.74f, 0.86f, 0.26f, 0.86f)
            cajaN(0.34f, 0.44f, 0.14f, 0.28f, Blanco.copy(alpha = 0.55f), 0.03f)
        }

        // ----------------------------------------------------------- Agua
        IconoAmb.GRIFO -> {
            cajaN(0.14f, 0.26f, 0.14f, 0.44f, Gris, 0.03f)
            cajaN(0.14f, 0.28f, 0.52f, 0.14f, Gris, 0.05f)
            cajaN(0.56f, 0.36f, 0.12f, 0.20f, GrisOsc, 0.03f)
            circuloN(0.21f, 0.20f, 0.10f, Azul)
            gota(0.62f, 0.66f, 0.09f)
            gota(0.62f, 0.86f, 0.06f)
        }
        IconoAmb.GOTA -> gota(0.5f, 0.52f, 0.30f)
        IconoAmb.CHARCO -> {
            ovaloN(0.10f, 0.56f, 0.80f, 0.30f, Azul.copy(alpha = 0.55f))
            ovaloN(0.24f, 0.62f, 0.32f, 0.12f, AzulClaro)
            arcoN(0.30f, 0.30f, 0.40f, 0.30f, 200f, 140f, AzulClaro, 0.05f)
        }
        IconoAmb.TUBERIA -> {
            cajaN(0.08f, 0.24f, 0.56f, 0.20f, GrisOsc, 0.04f)
            cajaN(0.58f, 0.20f, 0.14f, 0.28f, Gris, 0.03f)
            figuraN(Color(0xFF9E8B6B), 0.60f, 0.48f, 0.74f, 0.48f, 0.84f, 0.92f, 0.50f, 0.92f)
            circuloN(0.66f, 0.62f, 0.05f, Crema.copy(alpha = 0.8f))
            circuloN(0.74f, 0.78f, 0.04f, Crema.copy(alpha = 0.8f))
        }
        IconoAmb.MANGUERA -> {
            arcoN(0.10f, 0.24f, 0.62f, 0.56f, 130f, 200f, Verde, 0.09f)
            cajaN(0.62f, 0.60f, 0.20f, 0.10f, GrisOsc, 0.03f)
            gota(0.88f, 0.76f, 0.08f)
        }

        // ----------------------------------------------------------- Aire
        IconoAmb.HUMO -> {
            circuloN(0.36f, 0.62f, 0.20f, Humo)
            circuloN(0.58f, 0.56f, 0.16f, Humo.copy(alpha = 0.85f))
            circuloN(0.46f, 0.38f, 0.14f, Humo.copy(alpha = 0.7f))
            circuloN(0.62f, 0.24f, 0.10f, Humo.copy(alpha = 0.5f))
            cajaN(0.24f, 0.78f, 0.52f, 0.12f, GrisOsc, 0.04f)
        }
        IconoAmb.CHIMENEA -> {
            cajaN(0.16f, 0.56f, 0.68f, 0.36f, Gris, 0.03f)
            cajaN(0.30f, 0.24f, 0.18f, 0.34f, GrisOsc, 0.02f)
            circuloN(0.42f, 0.16f, 0.09f, Humo.copy(alpha = 0.8f))
            circuloN(0.56f, 0.09f, 0.07f, Humo.copy(alpha = 0.6f))
            cajaN(0.56f, 0.66f, 0.10f, 0.10f, Amarillo, 0.02f)
            cajaN(0.70f, 0.66f, 0.10f, 0.10f, Amarillo, 0.02f)
        }
        IconoAmb.AUTO -> {
            figuraN(Azul, 0.14f, 0.62f, 0.24f, 0.40f, 0.66f, 0.40f, 0.80f, 0.62f)
            cajaN(0.10f, 0.60f, 0.76f, 0.18f, AzulOsc, 0.05f)
            circuloN(0.28f, 0.82f, 0.09f, GrisOsc)
            circuloN(0.68f, 0.82f, 0.09f, GrisOsc)
            cajaN(0.30f, 0.44f, 0.14f, 0.14f, AzulClaro, 0.02f)
            cajaN(0.48f, 0.44f, 0.14f, 0.14f, AzulClaro, 0.02f)
        }
        IconoAmb.POLVO -> {
            figuraN(Arena, 0.10f, 0.86f, 0.50f, 0.42f, 0.90f, 0.86f)
            circuloN(0.24f, 0.36f, 0.06f, Humo.copy(alpha = 0.6f))
            circuloN(0.38f, 0.22f, 0.05f, Humo.copy(alpha = 0.5f))
            circuloN(0.62f, 0.30f, 0.07f, Humo.copy(alpha = 0.45f))
            circuloN(0.78f, 0.18f, 0.045f, Humo.copy(alpha = 0.4f))
        }

        // ---------------------------------------------------------- Ruido
        IconoAmb.ALTAVOZ -> {
            cajaN(0.12f, 0.38f, 0.16f, 0.24f, GrisOsc, 0.03f)
            figuraN(GrisOsc, 0.28f, 0.38f, 0.48f, 0.18f, 0.48f, 0.82f, 0.28f, 0.62f)
            arcoN(0.44f, 0.26f, 0.34f, 0.48f, -55f, 110f, Morado, 0.06f)
            arcoN(0.52f, 0.14f, 0.44f, 0.72f, -55f, 110f, Morado.copy(alpha = 0.6f), 0.055f)
        }
        IconoAmb.BOCINA -> {
            figuraN(Amarillo, 0.16f, 0.40f, 0.52f, 0.24f, 0.52f, 0.76f, 0.16f, 0.60f)
            cajaN(0.52f, 0.36f, 0.16f, 0.28f, Naranja, 0.04f)
            lineaN(0.72f, 0.34f, 0.86f, 0.26f, Rojo, 0.06f)
            lineaN(0.74f, 0.50f, 0.90f, 0.50f, Rojo, 0.06f)
            lineaN(0.72f, 0.66f, 0.86f, 0.74f, Rojo, 0.06f)
        }
        IconoAmb.SILENCIO -> {
            cajaN(0.14f, 0.40f, 0.14f, 0.20f, GrisOsc, 0.03f)
            figuraN(GrisOsc, 0.28f, 0.40f, 0.46f, 0.22f, 0.46f, 0.78f, 0.28f, 0.60f)
            lineaN(0.58f, 0.36f, 0.84f, 0.64f, Rojo, 0.07f)
            lineaN(0.84f, 0.36f, 0.58f, 0.64f, Rojo, 0.07f)
        }

        // ------------------------------------- Áreas verdes y biodiversidad
        IconoAmb.ARBOL -> {
            cajaN(0.44f, 0.60f, 0.12f, 0.32f, Tierra, 0.02f)
            circuloN(0.50f, 0.36f, 0.26f, Verde)
            circuloN(0.34f, 0.48f, 0.17f, VerdeClaro)
            circuloN(0.66f, 0.48f, 0.17f, VerdeOsc)
        }
        IconoAmb.ARBOL_SECO -> {
            cajaN(0.45f, 0.52f, 0.10f, 0.40f, Tierra, 0.02f)
            lineaN(0.50f, 0.60f, 0.24f, 0.36f, Tierra, 0.06f)
            lineaN(0.50f, 0.52f, 0.76f, 0.30f, Tierra, 0.06f)
            lineaN(0.50f, 0.44f, 0.36f, 0.18f, Tierra, 0.05f)
            circuloN(0.72f, 0.62f, 0.06f, Naranja)
            circuloN(0.30f, 0.68f, 0.05f, Naranja.copy(alpha = 0.7f))
        }
        IconoAmb.FLOR -> {
            cajaN(0.47f, 0.52f, 0.06f, 0.40f, Verde, 0.02f)
            figuraN(VerdeClaro, 0.50f, 0.70f, 0.28f, 0.62f, 0.34f, 0.80f)
            circuloN(0.50f, 0.24f, 0.12f, Rojo)
            circuloN(0.34f, 0.36f, 0.12f, Rojo)
            circuloN(0.66f, 0.36f, 0.12f, Rojo)
            circuloN(0.50f, 0.46f, 0.12f, Rojo.copy(alpha = 0.85f))
            circuloN(0.50f, 0.35f, 0.09f, Amarillo)
        }
        IconoAmb.CESPED -> {
            cajaN(0.06f, 0.72f, 0.88f, 0.18f, Tierra, 0.04f)
            figuraN(Verde, 0.14f, 0.74f, 0.22f, 0.34f, 0.30f, 0.74f)
            figuraN(VerdeClaro, 0.32f, 0.74f, 0.42f, 0.24f, 0.52f, 0.74f)
            figuraN(Verde, 0.54f, 0.74f, 0.64f, 0.36f, 0.74f, 0.74f)
            figuraN(VerdeOsc, 0.72f, 0.74f, 0.82f, 0.46f, 0.90f, 0.74f)
        }
        IconoAmb.PAJARO -> {
            ovaloN(0.24f, 0.42f, 0.52f, 0.36f, Azul)
            circuloN(0.72f, 0.38f, 0.15f, AzulOsc)
            figuraN(Amarillo, 0.84f, 0.36f, 0.96f, 0.42f, 0.84f, 0.46f)
            circuloN(0.76f, 0.34f, 0.035f, Blanco)
            figuraN(AzulClaro, 0.34f, 0.48f, 0.62f, 0.44f, 0.44f, 0.70f)
            lineaN(0.34f, 0.76f, 0.30f, 0.90f, Amarillo, 0.045f)
            lineaN(0.50f, 0.78f, 0.48f, 0.90f, Amarillo, 0.045f)
        }
        IconoAmb.PEZ -> {
            ovaloN(0.18f, 0.34f, 0.56f, 0.34f, Azul)
            figuraN(AzulOsc, 0.70f, 0.50f, 0.92f, 0.30f, 0.92f, 0.72f)
            circuloN(0.32f, 0.44f, 0.045f, Blanco)
            arcoN(0.30f, 0.44f, 0.28f, 0.20f, 20f, 140f, AzulClaro, 0.045f)
            figuraN(AzulClaro, 0.36f, 0.36f, 0.52f, 0.18f, 0.56f, 0.36f)
        }
        IconoAmb.MARIPOSA -> {
            cajaN(0.47f, 0.30f, 0.06f, 0.44f, Tierra, 0.02f)
            circuloN(0.30f, 0.36f, 0.17f, Naranja)
            circuloN(0.70f, 0.36f, 0.17f, Naranja)
            circuloN(0.32f, 0.64f, 0.13f, Amarillo)
            circuloN(0.68f, 0.64f, 0.13f, Amarillo)
            lineaN(0.50f, 0.30f, 0.40f, 0.14f, Tierra, 0.035f)
            lineaN(0.50f, 0.30f, 0.60f, 0.14f, Tierra, 0.035f)
        }
        IconoAmb.ABEJA -> {
            ovaloN(0.26f, 0.36f, 0.48f, 0.36f, Amarillo)
            cajaN(0.40f, 0.36f, 0.08f, 0.36f, GrisOsc)
            cajaN(0.56f, 0.36f, 0.08f, 0.36f, GrisOsc)
            circuloN(0.22f, 0.46f, 0.11f, GrisOsc)
            ovaloN(0.34f, 0.10f, 0.24f, 0.22f, Blanco.copy(alpha = 0.75f))
            ovaloN(0.54f, 0.10f, 0.24f, 0.22f, Blanco.copy(alpha = 0.6f))
        }
        IconoAmb.RANA -> {
            ovaloN(0.20f, 0.38f, 0.60f, 0.46f, Verde)
            circuloN(0.34f, 0.30f, 0.13f, VerdeClaro)
            circuloN(0.66f, 0.30f, 0.13f, VerdeClaro)
            circuloN(0.34f, 0.30f, 0.055f, Color.Black)
            circuloN(0.66f, 0.30f, 0.055f, Color.Black)
            arcoN(0.34f, 0.44f, 0.32f, 0.24f, 20f, 140f, VerdeOsc, 0.045f)
            figuraN(VerdeOsc, 0.10f, 0.78f, 0.28f, 0.70f, 0.24f, 0.88f)
            figuraN(VerdeOsc, 0.90f, 0.78f, 0.72f, 0.70f, 0.76f, 0.88f)
        }
        IconoAmb.NIDO -> {
            arcoN(0.14f, 0.36f, 0.72f, 0.62f, 0f, 180f, Tierra, 0.14f)
            circuloN(0.40f, 0.50f, 0.10f, Crema)
            circuloN(0.60f, 0.50f, 0.10f, Crema)
            circuloN(0.50f, 0.62f, 0.10f, Arena)
            lineaN(0.10f, 0.60f, 0.90f, 0.60f, Tierra.copy(alpha = 0.5f), 0.035f)
        }

        // -------------------------------------------------------- Energía
        IconoAmb.BOMBILLA -> {
            circuloN(0.50f, 0.40f, 0.26f, Amarillo)
            cajaN(0.40f, 0.62f, 0.20f, 0.16f, Gris, 0.03f)
            cajaN(0.42f, 0.78f, 0.16f, 0.08f, GrisOsc, 0.03f)
            lineaN(0.50f, 0.06f, 0.50f, 0.12f, Amarillo, 0.05f)
            lineaN(0.16f, 0.24f, 0.22f, 0.28f, Amarillo, 0.05f)
            lineaN(0.84f, 0.24f, 0.78f, 0.28f, Amarillo, 0.05f)
        }
        IconoAmb.ENCHUFE -> {
            cajaN(0.26f, 0.30f, 0.48f, 0.44f, Crema, 0.08f)
            circuloN(0.41f, 0.50f, 0.055f, GrisOsc)
            circuloN(0.59f, 0.50f, 0.055f, GrisOsc)
            lineaN(0.41f, 0.30f, 0.41f, 0.14f, Gris, 0.06f)
            lineaN(0.59f, 0.30f, 0.59f, 0.14f, Gris, 0.06f)
            cajaN(0.44f, 0.74f, 0.12f, 0.16f, GrisOsc, 0.03f)
        }
        IconoAmb.PANEL_SOLAR -> {
            figuraN(AzulOsc, 0.10f, 0.66f, 0.24f, 0.28f, 0.90f, 0.28f, 0.80f, 0.66f)
            lineaN(0.28f, 0.36f, 0.20f, 0.60f, AzulClaro, 0.035f)
            lineaN(0.48f, 0.32f, 0.44f, 0.62f, AzulClaro, 0.035f)
            lineaN(0.68f, 0.30f, 0.66f, 0.64f, AzulClaro, 0.035f)
            lineaN(0.18f, 0.46f, 0.86f, 0.46f, AzulClaro, 0.03f)
            cajaN(0.44f, 0.66f, 0.10f, 0.24f, GrisOsc, 0.02f)
            circuloN(0.20f, 0.14f, 0.10f, Amarillo)
        }
        IconoAmb.VENTILADOR -> {
            circuloN(0.50f, 0.46f, 0.08f, GrisOsc)
            ovaloN(0.16f, 0.34f, 0.34f, 0.16f, Azul)
            ovaloN(0.50f, 0.34f, 0.34f, 0.16f, Azul.copy(alpha = 0.8f))
            ovaloN(0.42f, 0.10f, 0.16f, 0.32f, AzulClaro)
            ovaloN(0.42f, 0.50f, 0.16f, 0.32f, AzulClaro)
            cajaN(0.46f, 0.78f, 0.08f, 0.14f, GrisOsc, 0.02f)
        }
        IconoAmb.PANTALLA -> {
            cajaN(0.10f, 0.20f, 0.80f, 0.50f, GrisOsc, 0.05f)
            cajaN(0.15f, 0.25f, 0.70f, 0.40f, AzulClaro, 0.03f)
            cajaN(0.42f, 0.70f, 0.16f, 0.10f, GrisOsc, 0.02f)
            cajaN(0.30f, 0.80f, 0.40f, 0.07f, GrisOsc, 0.03f)
            circuloN(0.30f, 0.40f, 0.05f, Amarillo)
        }

        // --------------------------------------------- Auditoría e interfaz
        IconoAmb.LUPA -> {
            anilloN(0.44f, 0.40f, 0.28f, 0.09f, Morado)
            circuloN(0.44f, 0.40f, 0.23f, AzulClaro.copy(alpha = 0.5f))
            lineaN(0.62f, 0.60f, 0.86f, 0.86f, Morado, 0.10f)
        }
        IconoAmb.PORTAPAPELES -> {
            cajaN(0.18f, 0.16f, 0.64f, 0.76f, Tierra, 0.06f)
            cajaN(0.24f, 0.24f, 0.52f, 0.62f, Crema, 0.04f)
            cajaN(0.38f, 0.10f, 0.24f, 0.12f, Gris, 0.04f)
            lineaN(0.32f, 0.42f, 0.68f, 0.42f, Verde, 0.045f)
            lineaN(0.32f, 0.56f, 0.68f, 0.56f, Amarillo, 0.045f)
            lineaN(0.32f, 0.70f, 0.56f, 0.70f, Rojo, 0.045f)
        }
        IconoAmb.ESCUDO -> {
            figuraN(VerdeOsc, 0.50f, 0.08f, 0.86f, 0.24f, 0.86f, 0.54f, 0.50f, 0.92f, 0.14f, 0.54f, 0.14f, 0.24f)
            figuraN(VerdeClaro, 0.50f, 0.18f, 0.76f, 0.30f, 0.76f, 0.52f, 0.50f, 0.80f, 0.24f, 0.52f, 0.24f, 0.30f)
            lineaN(0.36f, 0.48f, 0.46f, 0.60f, VerdeOsc, 0.08f)
            lineaN(0.46f, 0.60f, 0.66f, 0.36f, VerdeOsc, 0.08f)
        }
        IconoAmb.ESTRELLA -> estrella(Amarillo)
        IconoAmb.MAPA -> {
            figuraN(Arena, 0.08f, 0.24f, 0.36f, 0.14f, 0.64f, 0.28f, 0.92f, 0.16f, 0.92f, 0.78f, 0.64f, 0.90f, 0.36f, 0.76f, 0.08f, 0.88f)
            lineaN(0.36f, 0.16f, 0.36f, 0.76f, Verde, 0.035f)
            lineaN(0.64f, 0.28f, 0.64f, 0.90f, Verde, 0.035f)
            circuloN(0.50f, 0.46f, 0.08f, Rojo)
            circuloN(0.50f, 0.46f, 0.035f, Crema)
        }
        IconoAmb.LIBRO -> {
            cajaN(0.14f, 0.18f, 0.72f, 0.66f, VerdeOsc, 0.05f)
            cajaN(0.20f, 0.24f, 0.60f, 0.54f, Crema, 0.03f)
            lineaN(0.50f, 0.24f, 0.50f, 0.78f, Gris, 0.03f)
            lineaN(0.26f, 0.40f, 0.44f, 0.40f, Gris, 0.03f)
            lineaN(0.56f, 0.40f, 0.74f, 0.40f, Gris, 0.03f)
            lineaN(0.26f, 0.54f, 0.44f, 0.54f, Gris, 0.03f)
            lineaN(0.56f, 0.54f, 0.74f, 0.54f, Gris, 0.03f)
        }
        IconoAmb.MEDALLA -> {
            figuraN(Azul, 0.30f, 0.06f, 0.44f, 0.06f, 0.52f, 0.40f, 0.36f, 0.40f)
            figuraN(Rojo, 0.56f, 0.06f, 0.70f, 0.06f, 0.64f, 0.40f, 0.48f, 0.40f)
            circuloN(0.50f, 0.64f, 0.28f, Amarillo)
            circuloN(0.50f, 0.64f, 0.20f, Color(0xFFFFE08A))
            estrellaPequena(0.50f, 0.64f, 0.13f, Naranja)
        }
        IconoAmb.MOCHILA -> {
            cajaN(0.18f, 0.30f, 0.64f, 0.58f, Verde, 0.10f)
            arcoN(0.30f, 0.12f, 0.40f, 0.36f, 180f, 180f, VerdeOsc, 0.07f)
            cajaN(0.30f, 0.54f, 0.40f, 0.26f, VerdeClaro, 0.05f)
            cajaN(0.44f, 0.60f, 0.12f, 0.10f, VerdeOsc, 0.03f)
        }
        IconoAmb.CASCO -> {
            arcoN(0.12f, 0.24f, 0.76f, 0.76f, 180f, 180f, Amarillo, 0.26f)
            cajaN(0.08f, 0.60f, 0.84f, 0.10f, Amarillo, 0.04f)
            lineaN(0.50f, 0.26f, 0.50f, 0.58f, Naranja, 0.05f)
        }
        IconoAmb.CUADERNO -> {
            cajaN(0.20f, 0.12f, 0.62f, 0.76f, Crema, 0.05f)
            cajaN(0.16f, 0.12f, 0.10f, 0.76f, Naranja, 0.04f)
            lineaN(0.34f, 0.34f, 0.72f, 0.34f, Gris, 0.035f)
            lineaN(0.34f, 0.48f, 0.72f, 0.48f, Gris, 0.035f)
            lineaN(0.34f, 0.62f, 0.72f, 0.62f, Gris, 0.035f)
            lineaN(0.34f, 0.76f, 0.58f, 0.76f, Gris, 0.035f)
        }
        IconoAmb.HUELLA -> {
            ovaloN(0.28f, 0.40f, 0.44f, 0.42f, VerdeOsc)
            circuloN(0.26f, 0.28f, 0.08f, VerdeOsc)
            circuloN(0.44f, 0.20f, 0.085f, VerdeOsc)
            circuloN(0.63f, 0.22f, 0.08f, VerdeOsc)
            circuloN(0.78f, 0.34f, 0.07f, VerdeOsc)
        }
        IconoAmb.RELOJ -> {
            circuloN(0.50f, 0.52f, 0.36f, Crema)
            anilloN(0.50f, 0.52f, 0.36f, 0.07f, AzulOsc)
            lineaN(0.50f, 0.52f, 0.50f, 0.28f, AzulOsc, 0.055f)
            lineaN(0.50f, 0.52f, 0.68f, 0.60f, Azul, 0.05f)
            circuloN(0.50f, 0.52f, 0.045f, Rojo)
        }
    }
}

private fun DrawScope.gota(cx: Float, cy: Float, radio: Float) {
    figuraN(
        Color(0xFF1E88C7),
        cx, cy - radio * 1.7f,
        cx + radio, cy + radio * 0.2f,
        cx, cy + radio * 1.2f,
        cx - radio, cy + radio * 0.2f
    )
    circuloN(cx, cy + radio * 0.15f, radio * 0.95f, Color(0xFF1E88C7))
    circuloN(cx - radio * 0.3f, cy, radio * 0.28f, Color(0x88FFFFFF))
}

private fun DrawScope.estrella(color: Color) = estrellaPequena(0.5f, 0.52f, 0.42f, color)

private fun DrawScope.estrellaPequena(cx: Float, cy: Float, r: Float, color: Color) {
    val puntos = FloatArray(20)
    for (i in 0 until 10) {
        val radio = if (i % 2 == 0) r else r * 0.44f
        val angulo = (-90.0 + i * 36.0) * Math.PI / 180.0
        puntos[i * 2] = cx + (radio * Math.cos(angulo)).toFloat()
        puntos[i * 2 + 1] = cy + (radio * Math.sin(angulo)).toFloat()
    }
    figuraN(color, *puntos)
}
