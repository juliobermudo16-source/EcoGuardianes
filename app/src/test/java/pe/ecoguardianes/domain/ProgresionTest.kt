package pe.ecoguardianes.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import pe.ecoguardianes.domain.audit.Progresion
import pe.ecoguardianes.domain.model.Rango

class ProgresionTest {

    @Test
    fun `el nivel empieza en uno y sube cada doscientos cincuenta XP`() {
        assertEquals(1, Progresion.nivel(0))
        assertEquals(1, Progresion.nivel(249))
        assertEquals(2, Progresion.nivel(250))
        assertEquals(3, Progresion.nivel(500))
        assertEquals(5, Progresion.nivel(1000))
    }

    @Test
    fun `un XP negativo no rompe el calculo de nivel`() {
        assertEquals(1, Progresion.nivel(-500))
        assertEquals(0, Progresion.xpEnNivel(-500))
        assertEquals(0f, Progresion.progresoNivel(-500), 0.001f)
    }

    @Test
    fun `el nivel tiene un tope`() {
        assertEquals(Progresion.NIVEL_MAXIMO, Progresion.nivel(999_999))
        assertEquals(0, Progresion.xpParaSiguienteNivel(999_999))
        assertEquals(1f, Progresion.progresoNivel(999_999), 0.001f)
    }

    @Test
    fun `el progreso dentro del nivel se calcula sobre el resto`() {
        assertEquals(0.5f, Progresion.progresoNivel(125), 0.001f)
        assertEquals(0.5f, Progresion.progresoNivel(375), 0.001f)
        assertEquals(125, Progresion.xpParaSiguienteNivel(125))
    }

    @Test
    fun `los rangos cambian en los umbrales previstos`() {
        assertEquals(Rango.EXPLORADOR, Progresion.rango(0))
        assertEquals(Rango.EXPLORADOR, Progresion.rango(599))
        assertEquals(Rango.DETECTIVE, Progresion.rango(600))
        assertEquals(Rango.DETECTIVE, Progresion.rango(1599))
        assertEquals(Rango.ECOGUARDIAN, Progresion.rango(1600))
        assertEquals(Rango.ECOGUARDIAN, Progresion.rango(50_000))
    }

    @Test
    fun `se detecta la subida de nivel y el cambio de rango`() {
        assertTrue(Progresion.subioDeNivel(240, 260))
        assertFalse(Progresion.subioDeNivel(240, 249))
        assertTrue(Progresion.cambioDeRango(580, 640))
        assertFalse(Progresion.cambioDeRango(100, 200))
    }
}
