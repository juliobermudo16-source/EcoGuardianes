package pe.ecoguardianes.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import pe.ecoguardianes.data.catalogo.CatalogoAcciones
import pe.ecoguardianes.data.catalogo.CatalogoAvatares
import pe.ecoguardianes.data.catalogo.CatalogoColeccion
import pe.ecoguardianes.data.catalogo.CatalogoEscenarios
import pe.ecoguardianes.data.catalogo.CatalogoInsignias
import pe.ecoguardianes.data.catalogo.CatalogoReglas
import pe.ecoguardianes.data.catalogo.CatalogoRetos
import pe.ecoguardianes.domain.model.Categoria
import pe.ecoguardianes.domain.model.ZonaId

/** Verifica que los datos semilla estén completos y sean coherentes. */
class CatalogoTest {

    @Test
    fun `hay las seis zonas con su mision correspondiente`() {
        assertEquals(6, ZonaId.entries.size)
        ZonaId.entries.forEach { zona ->
            assertNotNull("Falta la misión de " + zona.name, CatalogoEscenarios.misionesPorZona[zona])
        }
    }

    @Test
    fun `cada escenario tiene al menos cinco situaciones interactivas`() {
        ZonaId.entries.forEach { zona ->
            val situaciones = CatalogoEscenarios.deZona(zona)
            assertTrue(
                zona.name + " tiene solo " + situaciones.size + " situaciones",
                situaciones.size >= 5
            )
        }
    }

    @Test
    fun `el catalogo cumple los minimos de contenido exigidos`() {
        assertTrue(
            "Se esperaban al menos 30 situaciones",
            CatalogoEscenarios.situaciones.size >= 30
        )
        assertTrue(
            "Se esperaban al menos 30 hallazgos posibles",
            CatalogoEscenarios.situaciones.count { it.esProblema } >= 30
        )
        assertTrue(
            "Se esperaban al menos 20 acciones correctivas",
            CatalogoAcciones.acciones.size >= 20
        )
        assertTrue("Se esperaban al menos 8 insignias", CatalogoInsignias.insignias.size >= 8)
        assertTrue(
            "Se esperaban al menos 20 coleccionables",
            CatalogoColeccion.coleccionables.size >= 20
        )
        assertTrue("Se esperaban al menos 8 avatares", CatalogoAvatares.avatares.size >= 8)
    }

    @Test
    fun `todos los identificadores son unicos`() {
        fun <T> unicos(nombre: String, lista: List<T>, id: (T) -> String) {
            val ids = lista.map(id)
            assertEquals(nombre + " tiene identificadores repetidos", ids.size, ids.toSet().size)
        }
        unicos("Situaciones", CatalogoEscenarios.situaciones) { it.id }
        unicos("Reglas", CatalogoReglas.reglas) { it.id }
        unicos("Acciones", CatalogoAcciones.acciones) { it.id }
        unicos("Retos", CatalogoRetos.retos) { it.id }
        unicos("Insignias", CatalogoInsignias.insignias) { it.id }
        unicos("Coleccionables", CatalogoColeccion.coleccionables) { it.id }
        unicos("Avatares", CatalogoAvatares.avatares) { it.id }
    }

    @Test
    fun `cada situacion apunta a una regla existente`() {
        CatalogoEscenarios.situaciones.forEach { situacion ->
            assertNotNull(
                "La situación " + situacion.id + " apunta a la regla inexistente " + situacion.reglaId,
                CatalogoReglas.regla(situacion.reglaId)
            )
        }
    }

    @Test
    fun `cada problema tiene tres acciones y solo una correcta`() {
        CatalogoEscenarios.situaciones.filter { it.esProblema }.forEach { situacion ->
            val acciones = CatalogoAcciones.accionesDe(situacion.id)
            assertEquals(
                "La situación " + situacion.id + " no tiene 3 acciones",
                3,
                acciones.size
            )
            assertEquals(
                "La situación " + situacion.id + " no tiene exactamente una acción correcta",
                1,
                acciones.count { it.esCorrecta }
            )
            assertEquals(acciones.map { it.id }, situacion.accionesIds)
        }
    }

    @Test
    fun `las situaciones conformes no llevan acciones correctivas`() {
        CatalogoEscenarios.situaciones.filter { !it.esProblema }.forEach { situacion ->
            assertTrue(
                "La situación correcta " + situacion.id + " no debería tener acciones",
                situacion.accionesIds.isEmpty()
            )
        }
    }

    @Test
    fun `cada reto declarado existe y es resoluble`() {
        CatalogoEscenarios.situaciones.mapNotNull { it.retoId }.distinct().forEach { retoId ->
            val reto = CatalogoRetos.reto(retoId)
            assertNotNull("Falta el reto " + retoId, reto)
            val solucion = reto!!.piezas.associate { it.id to it.destinoCorrectoId }
            assertTrue("El reto " + retoId + " no es resoluble", reto.esSolucionCorrecta(solucion))
            assertEquals(reto.piezas.size, reto.aciertos(solucion))
        }
    }

    @Test
    fun `los destinos correctos de los retos existen entre sus destinos`() {
        CatalogoRetos.retos.forEach { reto ->
            val ids = reto.destinos.map { it.id }.toSet()
            reto.piezas.forEach { pieza ->
                assertTrue(
                    "El reto " + reto.id + " apunta al destino inexistente " +
                        pieza.destinoCorrectoId,
                    pieza.destinoCorrectoId in ids
                )
            }
        }
    }

    @Test
    fun `una solucion incompleta o vacia no se da por correcta`() {
        val reto = CatalogoRetos.retos.first()
        assertFalse(reto.esSolucionCorrecta(emptyMap()))
        assertEquals(0, reto.aciertos(emptyMap()))
        val parcial = mapOf(reto.piezas.first().id to reto.piezas.first().destinoCorrectoId)
        assertFalse(reto.esSolucionCorrecta(parcial))
        assertEquals(1, reto.aciertos(parcial))
    }

    @Test
    fun `cada zona mezcla situaciones correctas y problematicas`() {
        ZonaId.entries.forEach { zona ->
            val situaciones = CatalogoEscenarios.deZona(zona)
            assertTrue(
                zona.name + " no tiene situaciones correctas",
                situaciones.any { !it.esProblema }
            )
            assertTrue(
                zona.name + " no tiene problemas",
                situaciones.count { it.esProblema } >= 4
            )
        }
    }

    @Test
    fun `las coordenadas de los objetos caen dentro del escenario`() {
        CatalogoEscenarios.situaciones.forEach {
            assertTrue(it.id + " se sale en X", it.x in 0.05f..0.95f)
            assertTrue(it.id + " se sale en Y", it.y in 0.05f..0.95f)
        }
    }

    @Test
    fun `todas las categorias educativas aparecen en algun escenario`() {
        val usadas = CatalogoEscenarios.situaciones.map { it.categoria }.toSet()
        Categoria.entries.forEach {
            assertTrue("La categoría " + it.name + " no se usa en ningún escenario", it in usadas)
        }
    }

    @Test
    fun `hay al menos dos reglas por categoria ambiental`() {
        Categoria.entries.forEach { categoria ->
            assertTrue(
                "La categoría " + categoria.name + " tiene menos de 2 reglas",
                CatalogoReglas.porCategoria(categoria).size >= 2
            )
        }
    }

    @Test
    fun `las referencias normativas tienen norma finalidad y emisor`() {
        val conReferencia = CatalogoReglas.reglas.filter { it.tieneReferenciaReal }
        assertTrue("Debe haber referencias normativas reales", conReferencia.size >= 10)
        conReferencia.forEach { regla ->
            val referencia = regla.referencia!!
            assertTrue(referencia.norma.isNotBlank())
            assertTrue(referencia.finalidad.length > 30)
            assertTrue(referencia.emisor.contains("Perú"))
        }
    }

    @Test
    fun `ningun texto educativo queda vacio`() {
        CatalogoEscenarios.situaciones.forEach {
            assertTrue(it.id + " sin nombre", it.nombre.isNotBlank())
            assertTrue(it.id + " sin observación", it.observacion.isNotBlank())
            assertTrue(it.id + " sin explicación", it.explicacion.isNotBlank())
            assertTrue(it.id + " sin pista", it.pista.isNotBlank())
        }
        CatalogoReglas.reglas.forEach {
            assertTrue(it.id + " sin regla simple", it.reglaSimple.isNotBlank())
            assertTrue(it.id + " sin explicación", it.explicacion.isNotBlank())
            assertTrue(it.id + " sin ejemplo", it.ejemplo.isNotBlank())
            assertTrue(it.id + " sin acción correcta", it.accionCorrecta.isNotBlank())
        }
        CatalogoAcciones.acciones.forEach {
            assertTrue(it.id + " sin texto", it.texto.isNotBlank())
            assertTrue(it.id + " sin explicación", it.explicacion.isNotBlank())
        }
    }

    @Test
    fun `los textos conservan tildes y letra enye`() {
        val todo = buildString {
            CatalogoEscenarios.situaciones.forEach {
                append(it.nombre).append(it.observacion).append(it.explicacion).append(it.pista)
            }
            CatalogoReglas.reglas.forEach {
                append(it.titulo).append(it.reglaSimple).append(it.explicacion).append(it.ejemplo)
            }
            CatalogoAcciones.acciones.forEach { append(it.texto).append(it.explicacion) }
            CatalogoEscenarios.misiones.forEach {
                append(it.titulo).append(it.briefingEco).append(it.objetivo)
            }
            CatalogoRetos.retos.forEach { append(it.enunciado).append(it.ayuda) }
        }

        listOf('á', 'é', 'í', 'ó', 'ú', 'ñ', '¿', '¡').forEach {
            assertTrue("Falta el carácter " + it + ": revisa la codificación UTF-8", todo.contains(it))
        }
        assertFalse("Hay caracteres corruptos de codificación", todo.contains("Ã"))
        assertFalse("Hay caracteres de reemplazo", todo.contains("�"))
    }

    @Test
    fun `las frases del contenido educativo son cortas`() {
        CatalogoEscenarios.situaciones.forEach {
            assertTrue(
                it.id + " tiene una observación demasiado larga",
                it.observacion.length <= 160
            )
        }
        CatalogoReglas.reglas.forEach {
            assertTrue(
                it.id + " tiene una regla simple demasiado larga",
                it.reglaSimple.length <= 110
            )
        }
    }

    @Test
    fun `las misiones piden un minimo alcanzable de hallazgos`() {
        CatalogoEscenarios.misiones.forEach { mision ->
            val problemas = CatalogoEscenarios.deZona(mision.zona).count { it.esProblema }
            assertTrue(
                "La misión " + mision.id + " pide más hallazgos de los que existen",
                mision.minimoHallazgos in 1..problemas
            )
            assertEquals(
                CatalogoEscenarios.deZona(mision.zona).size,
                mision.situacionesIds.size
            )
        }
    }

    @Test
    fun `las insignias y coleccionables tienen requisitos alcanzables`() {
        CatalogoInsignias.insignias.forEach {
            assertTrue(it.id + " tiene una meta inválida", it.requisito.meta > 0)
            assertTrue(it.id + " sin descripción", it.descripcion.isNotBlank())
        }
        CatalogoColeccion.coleccionables.forEach {
            assertTrue(it.id + " tiene una meta inválida", it.requisito.meta > 0)
            assertTrue(it.id + " sin dato curioso", it.datoCurioso.isNotBlank())
        }
    }

    @Test
    fun `las zonas exigen cada vez mas experiencia`() {
        val ordenadas = ZonaId.enOrden
        assertEquals(0, ordenadas.first().xpRequerido)
        ordenadas.zipWithNext().forEach { (a, b) ->
            assertTrue(
                "La zona " + b.name + " no exige más XP que " + a.name,
                b.xpRequerido > a.xpRequerido
            )
        }
    }

    @Test
    fun `las acciones barajadas mantienen las mismas opciones`() {
        val situacion = CatalogoEscenarios.situaciones.first { it.esProblema }
        val barajadas = pe.ecoguardianes.data.repo.EcoRepositorio
            .accionesBarajadas(situacion.id)

        assertEquals(3, barajadas.size)
        assertEquals(
            CatalogoAcciones.accionesDe(situacion.id).map { it.id }.toSet(),
            barajadas.map { it.id }.toSet()
        )
        assertEquals(1, barajadas.count { it.esCorrecta })
    }

    @Test
    fun `consultar contenido inexistente devuelve nulo en vez de fallar`() {
        assertEquals(null, CatalogoEscenarios.situacion("NO_EXISTE"))
        assertEquals(null, CatalogoReglas.regla(""))
        assertEquals(null, CatalogoAcciones.accion(null))
        assertEquals(null, CatalogoRetos.reto(null))
        assertTrue(CatalogoAcciones.idsDe("NO_EXISTE").isEmpty())
        assertEquals(CatalogoAvatares.predeterminado, CatalogoAvatares.avatar("NO_EXISTE"))
    }
}
