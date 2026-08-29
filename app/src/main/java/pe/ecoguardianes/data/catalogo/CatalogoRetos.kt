package pe.ecoguardianes.data.catalogo

import pe.ecoguardianes.domain.model.Categoria
import pe.ecoguardianes.domain.model.DestinoReto
import pe.ecoguardianes.domain.model.IconoAmb
import pe.ecoguardianes.domain.model.PiezaReto
import pe.ecoguardianes.domain.model.Reto
import pe.ecoguardianes.domain.model.TipoReto

/**
 * Mini-retos interactivos con los que el jugador repara de verdad la situación.
 * Cada reto declara sus piezas y sus destinos correctos, de modo que la
 * corrección se calcula y no se simula.
 */
object CatalogoRetos {

    private val SI = DestinoReto("si", "Sí ayuda", IconoAmb.ESCUDO, Categoria.RESIDUOS.colorHex)
    private val NO = DestinoReto("no", "No ayuda", IconoAmb.HUELLA, Categoria.CONTAMINACION.colorHex)

    private val APAGADO =
        DestinoReto("apagado", "Se apaga o se cierra", IconoAmb.SILENCIO, 0xFF2E9E5B)
    private val SIGUE =
        DestinoReto("sigue", "Debe seguir así", IconoAmb.RELOJ, 0xFF1E88C7)

    private fun pasos(n: Int) = (1..n).map {
        DestinoReto("paso$it", "Paso $it", IconoAmb.CUADERNO, 0xFF1E88C7)
    }

    val retos: List<Reto> = listOf(

        Reto(
            id = "RETO_CLASIFICAR_COCINA",
            tipo = TipoReto.CLASIFICAR,
            enunciado = "Separa la bolsa de la cocina en los tres recipientes.",
            ayuda = "Los restos de comida van al orgánico. Lo limpio y seco, al reciclable.",
            piezas = listOf(
                PiezaReto("p1", "Botella de plástico", IconoAmb.BOTELLA, "reciclable"),
                PiezaReto("p2", "Cáscara de plátano", IconoAmb.ORGANICO, "organico"),
                PiezaReto("p3", "Periódico", IconoAmb.PAPEL, "reciclable"),
                PiezaReto("p4", "Servilleta usada", IconoAmb.PAPEL, "general"),
                PiezaReto("p5", "Lata de conserva", IconoAmb.LATA, "reciclable"),
                PiezaReto("p6", "Restos de arroz", IconoAmb.ORGANICO, "organico")
            ),
            destinos = listOf(
                DestinoReto("reciclable", "Reciclable", IconoAmb.CONTENEDOR, 0xFFF2B705),
                DestinoReto("organico", "Orgánico", IconoAmb.ORGANICO, 0xFF6B4A2F),
                DestinoReto("general", "No aprovechable", IconoAmb.BOLSA_BASURA, 0xFF6E7A83)
            )
        ),

        Reto(
            id = "RETO_CERRAR_CANOS",
            tipo = TipoReto.INTERRUPTOR,
            enunciado = "Decide qué se cierra y qué debe seguir funcionando.",
            ayuda = "Cierra lo que gasta agua sin que nadie la esté usando.",
            piezas = listOf(
                PiezaReto("c1", "Caño abierto sin nadie", IconoAmb.GRIFO, "apagado"),
                PiezaReto("c2", "Caño que gotea", IconoAmb.GOTA, "apagado"),
                PiezaReto("c3", "Manguera regando la vereda", IconoAmb.MANGUERA, "apagado"),
                PiezaReto("c4", "Bebedero que alguien usa", IconoAmb.GOTA, "sigue")
            ),
            destinos = listOf(APAGADO, SIGUE)
        ),

        Reto(
            id = "RETO_APAGAR_LUCES",
            tipo = TipoReto.INTERRUPTOR,
            enunciado = "Apaga lo que no hace falta en el aula vacía.",
            ayuda = "Si no hay nadie usándolo, se apaga. La salida de emergencia se queda.",
            piezas = listOf(
                PiezaReto("l1", "Luces del aula vacía", IconoAmb.BOMBILLA, "apagado"),
                PiezaReto("l2", "Proyector sin usar", IconoAmb.PANTALLA, "apagado"),
                PiezaReto("l3", "Ventilador en sala vacía", IconoAmb.VENTILADOR, "apagado"),
                PiezaReto("l4", "Luz de salida de emergencia", IconoAmb.ESCUDO, "sigue")
            ),
            destinos = listOf(APAGADO, SIGUE)
        ),

        Reto(
            id = "RETO_PELIGROSOS",
            tipo = TipoReto.SELECCION,
            enunciado = "¿Qué va al punto de acopio especial y qué no?",
            ayuda = "Lo que tiene sustancias peligrosas nunca va al tacho común.",
            piezas = listOf(
                PiezaReto("h1", "Pilas usadas", IconoAmb.PILA, "si"),
                PiezaReto("h2", "Foco quemado", IconoAmb.BOMBILLA, "si"),
                PiezaReto("h3", "Medicinas vencidas", IconoAmb.ESCUDO, "si"),
                PiezaReto("h4", "Cáscara de naranja", IconoAmb.ORGANICO, "no"),
                PiezaReto("h5", "Cuaderno viejo", IconoAmb.CUADERNO, "no")
            ),
            destinos = listOf(
                DestinoReto("si", "Punto de acopio especial", IconoAmb.ESCUDO, 0xFFD1495B),
                DestinoReto("no", "Tacho normal", IconoAmb.CONTENEDOR, 0xFF2E9E5B)
            )
        ),

        Reto(
            id = "RETO_DESENCHUFAR",
            tipo = TipoReto.SELECCION,
            enunciado = "Marca lo que ayuda a no desperdiciar energía.",
            ayuda = "Piensa en lo que sigue consumiendo aunque parezca apagado.",
            piezas = listOf(
                PiezaReto("e1", "Desenchufar cargadores sin uso", IconoAmb.ENCHUFE, "si"),
                PiezaReto("e2", "Usar regleta con interruptor", IconoAmb.ENCHUFE, "si"),
                PiezaReto("e3", "Abrir la cortina y usar luz natural", IconoAmb.BOMBILLA, "si"),
                PiezaReto("e4", "Dejar la tele en espera todo el día", IconoAmb.PANTALLA, "no"),
                PiezaReto("e5", "Cargar el celular toda la noche", IconoAmb.ENCHUFE, "no")
            ),
            destinos = listOf(SI, NO)
        ),

        Reto(
            id = "RETO_PATIO_LIMPIO",
            tipo = TipoReto.ARRASTRAR,
            enunciado = "Coloca cada residuo del suelo donde corresponde.",
            ayuda = "Mira el material de cada objeto antes de soltarlo.",
            piezas = listOf(
                PiezaReto("q1", "Botella de gaseosa", IconoAmb.BOTELLA, "reciclable"),
                PiezaReto("q2", "Hojas de cuaderno", IconoAmb.PAPEL, "reciclable"),
                PiezaReto("q3", "Envoltura sucia", IconoAmb.BOLSA_BASURA, "general"),
                PiezaReto("q4", "Restos de fruta", IconoAmb.ORGANICO, "organico"),
                PiezaReto("q5", "Lata aplastada", IconoAmb.LATA, "reciclable")
            ),
            destinos = listOf(
                DestinoReto("reciclable", "Reciclable", IconoAmb.CONTENEDOR, 0xFFF2B705),
                DestinoReto("organico", "Orgánico", IconoAmb.ORGANICO, 0xFF6B4A2F),
                DestinoReto("general", "No aprovechable", IconoAmb.BOLSA_BASURA, 0xFF6E7A83)
            )
        ),

        Reto(
            id = "RETO_SILENCIO",
            tipo = TipoReto.SELECCION,
            enunciado = "Elige lo que de verdad baja el ruido de la zona.",
            ayuda = "Fíjate en quién vive o estudia cerca del ruido.",
            piezas = listOf(
                PiezaReto("r1", "Bajar el volumen del parlante", IconoAmb.ALTAVOZ, "si"),
                PiezaReto("r2", "Acordar horarios para la música", IconoAmb.RELOJ, "si"),
                PiezaReto("r3", "Usar la bocina solo si hay peligro", IconoAmb.BOCINA, "si"),
                PiezaReto("r4", "Poner un segundo parlante", IconoAmb.ALTAVOZ, "no"),
                PiezaReto("r5", "Cerrar la ventana y seguir igual", IconoAmb.SILENCIO, "no")
            ),
            destinos = listOf(SI, NO)
        ),

        Reto(
            id = "RETO_DESCARTABLES",
            tipo = TipoReto.CONECTAR,
            enunciado = "Une cada descartable con la alternativa que dura.",
            ayuda = "Busca el objeto que se puede lavar y volver a usar.",
            piezas = listOf(
                PiezaReto("d1", "Vaso descartable", IconoAmb.BOTELLA, "tomatodo"),
                PiezaReto("d2", "Bolsa de plástico", IconoAmb.BOLSA_BASURA, "bolsatela"),
                PiezaReto("d3", "Sorbete de plástico", IconoAmb.BOTELLA, "sinsorbete"),
                PiezaReto("d4", "Plato descartable", IconoAmb.PAPEL, "platolavable")
            ),
            destinos = listOf(
                DestinoReto("tomatodo", "Tomatodo reutilizable", IconoAmb.MOCHILA, 0xFF1E88C7),
                DestinoReto("bolsatela", "Bolsa de tela", IconoAmb.MOCHILA, 0xFF2E9E5B),
                DestinoReto("sinsorbete", "Beber sin sorbete", IconoAmb.GOTA, 0xFFF2B705),
                DestinoReto("platolavable", "Plato lavable", IconoAmb.CONTENEDOR, 0xFF8E5FD9)
            )
        ),

        Reto(
            id = "RETO_CESPED",
            tipo = TipoReto.ORDENAR,
            enunciado = "Ordena los pasos para recuperar el césped pisoteado.",
            ayuda = "Primero se protege la zona, después se siembra y al final se cuida.",
            piezas = listOf(
                PiezaReto("g1", "Cercar la zona pelada", IconoAmb.ESCUDO, "paso1", 1),
                PiezaReto("g2", "Remover la tierra y sembrar", IconoAmb.CESPED, "paso2", 2),
                PiezaReto("g3", "Regar y señalizar el sendero", IconoAmb.MANGUERA, "paso3", 3)
            ),
            destinos = pasos(3)
        ),

        Reto(
            id = "RETO_ARBOL",
            tipo = TipoReto.ORDENAR,
            enunciado = "Ordena cómo ayudar al árbol dañado.",
            ayuda = "Primero se avisa, luego se cura y por último se cuida.",
            piezas = listOf(
                PiezaReto("a1", "Reportar el daño al municipio", IconoAmb.PORTAPAPELES, "paso1", 1),
                PiezaReto("a2", "Proteger el tronco herido", IconoAmb.ARBOL, "paso2", 2),
                PiezaReto("a3", "Regar y revisar cada semana", IconoAmb.GOTA, "paso3", 3)
            ),
            destinos = pasos(3)
        ),

        Reto(
            id = "RETO_AVES",
            tipo = TipoReto.SELECCION,
            enunciado = "¿Qué hacemos al ver aves descansando?",
            ayuda = "Un animal tranquilo es un animal a salvo.",
            piezas = listOf(
                PiezaReto("v1", "Observar desde el sendero", IconoAmb.LUPA, "si"),
                PiezaReto("v2", "Hablar en voz baja", IconoAmb.SILENCIO, "si"),
                PiezaReto("v3", "Alejarse despacio del nido", IconoAmb.NIDO, "si"),
                PiezaReto("v4", "Correr para que vuelen", IconoAmb.PAJARO, "no"),
                PiezaReto("v5", "Darles pan y golosinas", IconoAmb.ORGANICO, "no")
            ),
            destinos = listOf(SI, NO)
        ),

        Reto(
            id = "RETO_RIEGO",
            tipo = TipoReto.ORDENAR,
            enunciado = "Ordena un riego que no desperdicie agua.",
            ayuda = "La hora correcta cambia todo: al mediodía el agua se evapora.",
            piezas = listOf(
                PiezaReto("w1", "Esperar al atardecer", IconoAmb.RELOJ, "paso1", 1),
                PiezaReto("w2", "Dirigir el agua a la tierra", IconoAmb.MANGUERA, "paso2", 2),
                PiezaReto("w3", "Cerrar bien la llave al terminar", IconoAmb.GRIFO, "paso3", 3)
            ),
            destinos = pasos(3)
        ),

        Reto(
            id = "RETO_LIMPIAR_RIO",
            tipo = TipoReto.ARRASTRAR,
            enunciado = "Retira lo que no pertenece al río.",
            ayuda = "Todo lo que hizo una persona sale del agua; lo natural se queda.",
            piezas = listOf(
                PiezaReto("t1", "Llanta vieja", IconoAmb.HUELLA, "retirar"),
                PiezaReto("t2", "Bolsa flotando", IconoAmb.BOLSA_BASURA, "retirar"),
                PiezaReto("t3", "Botella de plástico", IconoAmb.BOTELLA, "retirar"),
                PiezaReto("t4", "Tronco caído natural", IconoAmb.ARBOL, "dejar"),
                PiezaReto("t5", "Piedras del cauce", IconoAmb.CESPED, "dejar")
            ),
            destinos = listOf(
                DestinoReto("retirar", "Retirar del agua", IconoAmb.MOCHILA, 0xFFD1495B),
                DestinoReto("dejar", "Dejar como está", IconoAmb.ARBOL, 0xFF2E9E5B)
            )
        ),

        Reto(
            id = "RETO_VERTIMIENTO",
            tipo = TipoReto.ORDENAR,
            enunciado = "Ordena la respuesta ante un vertimiento sospechoso.",
            ayuda = "Primero la seguridad, después el registro y al final el aviso.",
            piezas = listOf(
                PiezaReto("x1", "No tocar el líquido y alejarse", IconoAmb.ESCUDO, "paso1", 1),
                PiezaReto("x2", "Anotar el lugar y la hora", IconoAmb.PORTAPAPELES, "paso2", 2),
                PiezaReto("x3", "Avisar a la autoridad ambiental", IconoAmb.CUADERNO, "paso3", 3)
            ),
            destinos = pasos(3)
        ),

        Reto(
            id = "RETO_DEVOLVER_FAUNA",
            tipo = TipoReto.ORDENAR,
            enunciado = "Ordena cómo devolver a los renacuajos al río.",
            ayuda = "Con calma y en el mismo lugar donde estaban.",
            piezas = listOf(
                PiezaReto("f1", "Volver al mismo punto del río", IconoAmb.CHARCO, "paso1", 1),
                PiezaReto("f2", "Inclinar la botella con cuidado", IconoAmb.RANA, "paso2", 2),
                PiezaReto("f3", "Observarlos desde la orilla", IconoAmb.LUPA, "paso3", 3)
            ),
            destinos = pasos(3)
        ),

        Reto(
            id = "RETO_FOGATA",
            tipo = TipoReto.SELECCION,
            enunciado = "Elige qué hacer con la fogata de plásticos.",
            ayuda = "Quemar plástico libera humo tóxico: nunca es la solución.",
            piezas = listOf(
                PiezaReto("o1", "Apagar el fuego con seguridad", IconoAmb.GOTA, "si"),
                PiezaReto("o2", "Separar los residuos que quedaron", IconoAmb.CONTENEDOR, "si"),
                PiezaReto("o3", "Llevarlos al punto de acopio", IconoAmb.MOCHILA, "si"),
                PiezaReto("o4", "Añadir más plástico al fuego", IconoAmb.HUMO, "no"),
                PiezaReto("o5", "Dejar que se apague solo", IconoAmb.RELOJ, "no")
            ),
            destinos = listOf(SI, NO)
        ),

        Reto(
            id = "RETO_HUMO",
            tipo = TipoReto.CONECTAR,
            enunciado = "Une cada fuente de humo con su solución.",
            ayuda = "Cada máquina tiene una medida distinta.",
            piezas = listOf(
                PiezaReto("m1", "Camión con motor encendido parado", IconoAmb.AUTO, "apagarmotor"),
                PiezaReto("m2", "Chimenea sin filtro", IconoAmb.CHIMENEA, "filtro"),
                PiezaReto("m3", "Quema de residuos", IconoAmb.HUMO, "acopio"),
                PiezaReto("m4", "Motor con mantenimiento atrasado", IconoAmb.AUTO, "mantenimiento")
            ),
            destinos = listOf(
                DestinoReto("apagarmotor", "Apagar el motor al parar", IconoAmb.SILENCIO, 0xFF2E9E5B),
                DestinoReto("filtro", "Instalar y revisar filtros", IconoAmb.ESCUDO, 0xFF1E88C7),
                DestinoReto("acopio", "Llevar todo al punto de acopio", IconoAmb.CONTENEDOR, 0xFFF2B705),
                DestinoReto("mantenimiento", "Programar el mantenimiento", IconoAmb.CUADERNO, 0xFF8E5FD9)
            )
        ),

        Reto(
            id = "RETO_POLVO",
            tipo = TipoReto.SELECCION,
            enunciado = "¿Qué evita que el polvo llegue a las casas vecinas?",
            ayuda = "El polvo vuela cuando está seco y descubierto.",
            piezas = listOf(
                PiezaReto("y1", "Cubrir el montículo con una malla", IconoAmb.ESCUDO, "si"),
                PiezaReto("y2", "Humedecer el material", IconoAmb.GOTA, "si"),
                PiezaReto("y3", "Cerrar la tolva del camión", IconoAmb.AUTO, "si"),
                PiezaReto("y4", "Barrer en seco con viento fuerte", IconoAmb.POLVO, "no"),
                PiezaReto("y5", "Amontonar más arena al costado", IconoAmb.POLVO, "no")
            ),
            destinos = listOf(SI, NO)
        ),

        Reto(
            id = "RETO_TRANSPORTE",
            tipo = TipoReto.CONECTAR,
            enunciado = "Une cada trayecto con la mejor forma de hacerlo.",
            ayuda = "Para distancias cortas casi siempre hay una opción más limpia.",
            piezas = listOf(
                PiezaReto("n1", "Tres cuadras hasta el colegio", IconoAmb.HUELLA, "caminar"),
                PiezaReto("n2", "Quince cuadras con mochila", IconoAmb.MOCHILA, "bici"),
                PiezaReto("n3", "Cruzar toda la ciudad", IconoAmb.AUTO, "publico"),
                PiezaReto("n4", "Ir cuatro vecinos al mismo lugar", IconoAmb.CASCO, "compartido")
            ),
            destinos = listOf(
                DestinoReto("caminar", "Caminar", IconoAmb.HUELLA, 0xFF2E9E5B),
                DestinoReto("bici", "Bicicleta", IconoAmb.HUELLA, 0xFFF2B705),
                DestinoReto("publico", "Transporte público", IconoAmb.AUTO, 0xFF1E88C7),
                DestinoReto("compartido", "Compartir el auto", IconoAmb.AUTO, 0xFF8E5FD9)
            )
        ),

        Reto(
            id = "RETO_ESTUDIO",
            tipo = TipoReto.ORDENAR,
            enunciado = "Ordena los pasos antes de poner en marcha la planta.",
            ayuda = "Primero se estudia, luego se aprueban las medidas y al final se opera.",
            piezas = listOf(
                PiezaReto("s1", "Estudiar los efectos ambientales", IconoAmb.LUPA, "paso1", 1),
                PiezaReto("s2", "Definir medidas de prevención", IconoAmb.PORTAPAPELES, "paso2", 2),
                PiezaReto("s3", "Operar y vigilar los resultados", IconoAmb.CASCO, "paso3", 3)
            ),
            destinos = pasos(3)
        ),

        Reto(
            id = "RETO_PECES",
            tipo = TipoReto.CONECTAR,
            enunciado = "Une cada señal del río con lo que significa.",
            ayuda = "El río avisa con señales antes de enfermarse del todo.",
            piezas = listOf(
                PiezaReto("z1", "Agua turbia y con espuma", IconoAmb.TUBERIA, "vertimiento"),
                PiezaReto("z2", "Peces que se alejan de la zona", IconoAmb.PEZ, "faltaoxigeno"),
                PiezaReto("z3", "Bolsas atrapadas en las ramas", IconoAmb.BOLSA_BASURA, "residuos"),
                PiezaReto("z4", "Orilla sin plantas", IconoAmb.ARBOL_SECO, "erosion")
            ),
            destinos = listOf(
                DestinoReto("vertimiento", "Alguien vertió algo", IconoAmb.TUBERIA, 0xFFD1495B),
                DestinoReto("faltaoxigeno", "Al agua le falta oxígeno", IconoAmb.PEZ, 0xFF1E88C7),
                DestinoReto("residuos", "Llegan residuos de arriba", IconoAmb.BOLSA_BASURA, 0xFFF2B705),
                DestinoReto("erosion", "La orilla se está erosionando", IconoAmb.CESPED, 0xFF6B4A2F)
            )
        )
    )

    val porId: Map<String, Reto> = retos.associateBy { it.id }

    fun reto(id: String?): Reto? = id?.let { porId[it] }
}
