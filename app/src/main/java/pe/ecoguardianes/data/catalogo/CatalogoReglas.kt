package pe.ecoguardianes.data.catalogo

import pe.ecoguardianes.domain.model.Categoria
import pe.ecoguardianes.domain.model.ReferenciaNormativa
import pe.ecoguardianes.domain.model.ReglaAmbiental

/**
 * Biblioteca ambiental de EcoGuardianes.
 *
 * Cada entrada tiene dos planos bien separados:
 *  1. La "regla EcoGuardián": lenguaje sencillo para niños de 8 a 12 años.
 *  2. La "referencia normativa": el nombre oficial de una norma peruana real
 *     y una descripción general de su finalidad.
 *
 * No se citan artículos ni se atribuyen obligaciones concretas a personas.
 * El material es educativo y no constituye asesoría jurídica.
 */
object CatalogoReglas {

    // --- Referencias normativas reales del Perú (solo nombre y finalidad) ---

    private val DL_1278 = ReferenciaNormativa(
        norma = "Decreto Legislativo N.° 1278",
        finalidad = "Ley de Gestión Integral de Residuos Sólidos. Establece cómo debe " +
            "manejarse la basura en el país, dando prioridad a reducir, reutilizar y reciclar.",
        emisor = "Poder Ejecutivo del Perú"
    )

    private val LEY_27972 = ReferenciaNormativa(
        norma = "Ley N.° 27972",
        finalidad = "Ley Orgánica de Municipalidades. Encarga a las municipalidades los " +
            "servicios de limpieza pública y el cuidado de los parques y jardines.",
        emisor = "Congreso de la República del Perú"
    )

    private val LEY_29419 = ReferenciaNormativa(
        norma = "Ley N.° 29419",
        finalidad = "Ley que regula la actividad de los recicladores y promueve su " +
            "formalización y protección.",
        emisor = "Congreso de la República del Perú"
    )

    private val LEY_30884 = ReferenciaNormativa(
        norma = "Ley N.° 30884",
        finalidad = "Ley que regula el plástico de un solo uso y los recipientes o envases " +
            "descartables, para reducir su impacto en el ambiente.",
        emisor = "Congreso de la República del Perú"
    )

    private val LEY_29338 = ReferenciaNormativa(
        norma = "Ley N.° 29338",
        finalidad = "Ley de Recursos Hídricos. Regula el uso y la protección del agua en el Perú.",
        emisor = "Congreso de la República del Perú"
    )

    private val DS_004_2017 = ReferenciaNormativa(
        norma = "Decreto Supremo N.° 004-2017-MINAM",
        finalidad = "Aprueba los Estándares de Calidad Ambiental (ECA) para Agua, que " +
            "indican qué tan limpia debe estar el agua según su uso.",
        emisor = "Ministerio del Ambiente del Perú"
    )

    private val DS_003_2017 = ReferenciaNormativa(
        norma = "Decreto Supremo N.° 003-2017-MINAM",
        finalidad = "Aprueba los Estándares de Calidad Ambiental (ECA) para Aire, que " +
            "indican los niveles de calidad del aire que se busca mantener.",
        emisor = "Ministerio del Ambiente del Perú"
    )

    private val DS_085_2003 = ReferenciaNormativa(
        norma = "Decreto Supremo N.° 085-2003-PCM",
        finalidad = "Reglamento de Estándares Nacionales de Calidad Ambiental para Ruido. " +
            "Define zonas y horarios en los que el ruido debe mantenerse bajo.",
        emisor = "Presidencia del Consejo de Ministros del Perú"
    )

    private val LEY_28611 = ReferenciaNormativa(
        norma = "Ley N.° 28611",
        finalidad = "Ley General del Ambiente. Es la norma principal del país sobre el " +
            "derecho a un ambiente saludable y el deber de protegerlo.",
        emisor = "Congreso de la República del Perú"
    )

    private val LEY_26834 = ReferenciaNormativa(
        norma = "Ley N.° 26834",
        finalidad = "Ley de Áreas Naturales Protegidas. Protege espacios del territorio " +
            "peruano por su valor natural y su diversidad biológica.",
        emisor = "Congreso de la República del Perú"
    )

    private val LEY_29763 = ReferenciaNormativa(
        norma = "Ley N.° 29763",
        finalidad = "Ley Forestal y de Fauna Silvestre. Regula el cuidado de los bosques y " +
            "de los animales silvestres del país.",
        emisor = "Congreso de la República del Perú"
    )

    private val LEY_27345 = ReferenciaNormativa(
        norma = "Ley N.° 27345",
        finalidad = "Ley de Promoción del Uso Eficiente de la Energía. Impulsa aprovechar " +
            "mejor la energía y evitar su desperdicio.",
        emisor = "Congreso de la República del Perú"
    )

    private val LEY_30754 = ReferenciaNormativa(
        norma = "Ley N.° 30754",
        finalidad = "Ley Marco sobre Cambio Climático. Orienta las acciones del país frente " +
            "al cambio climático.",
        emisor = "Congreso de la República del Perú"
    )

    private val LEY_27446 = ReferenciaNormativa(
        norma = "Ley N.° 27446",
        finalidad = "Ley del Sistema Nacional de Evaluación de Impacto Ambiental. Pide " +
            "estudiar por adelantado los efectos de un proyecto sobre el ambiente.",
        emisor = "Congreso de la República del Perú"
    )

    // --- Reglas educativas ---

    val reglas: List<ReglaAmbiental> = listOf(
        ReglaAmbiental(
            id = "R_RES_01",
            categoria = Categoria.RESIDUOS,
            titulo = "Cada residuo en su recipiente",
            reglaSimple = "Separa los residuos antes de botarlos.",
            explicacion = "Cuando el papel, el plástico y los restos de comida se mezclan, " +
                "ya casi nada se puede reciclar y todo termina en el botadero.",
            ejemplo = "En la cocina hay una sola bolsa con cáscaras, botellas y periódicos juntos.",
            accionCorrecta = "Separar en tres grupos: reciclables, orgánicos y lo que no se recicla.",
            referencia = DL_1278
        ),
        ReglaAmbiental(
            id = "R_RES_02",
            categoria = Categoria.RESIDUOS,
            titulo = "La basura nunca va al suelo",
            reglaSimple = "Los residuos van dentro del contenedor, nunca alrededor.",
            explicacion = "La basura suelta se la lleva el viento, tapa los desagües y " +
                "atrae moscas y roedores que enferman a las personas.",
            ejemplo = "Bolsas apiladas fuera del tacho, en la vereda del colegio.",
            accionCorrecta = "Recoger la basura con guantes y colocarla dentro del contenedor cerrado.",
            referencia = LEY_27972
        ),
        ReglaAmbiental(
            id = "R_RES_03",
            categoria = Categoria.RESIDUOS,
            titulo = "Menos plástico de un solo uso",
            reglaSimple = "Usa cosas que duren en vez de las de un solo uso.",
            explicacion = "Un vaso descartable se usa cinco minutos, pero el plástico " +
                "permanece muchísimos años en el ambiente.",
            ejemplo = "En el quiosco se reparten sorbetes y vasos descartables en cada compra.",
            accionCorrecta = "Llevar tomatodo y usar vajilla lavable en el colegio.",
            referencia = LEY_30884
        ),
        ReglaAmbiental(
            id = "R_RES_04",
            categoria = Categoria.RESIDUOS,
            titulo = "Reciclar es un trabajo de todos",
            reglaSimple = "Entrega limpio y seco lo que se puede reciclar.",
            explicacion = "Las botellas y los papeles sucios de comida ya no sirven para " +
                "reciclar y complican el trabajo de los recicladores.",
            ejemplo = "Botellas con restos de gaseosa dentro del tacho de reciclaje.",
            accionCorrecta = "Enjuagar el envase, aplastarlo y colocarlo en el recipiente correcto.",
            referencia = LEY_29419
        ),
        ReglaAmbiental(
            id = "R_AGU_01",
            categoria = Categoria.AGUA,
            titulo = "Ni una gota de más",
            reglaSimple = "Cierra el caño mientras no lo estés usando.",
            explicacion = "Un caño que gotea puede perder muchos litros al día, agua que " +
                "otras personas sí necesitan.",
            ejemplo = "El caño del lavadero queda abierto mientras alguien se lava los dientes.",
            accionCorrecta = "Cerrar el caño y avisar si gotea para que lo reparen.",
            referencia = LEY_29338
        ),
        ReglaAmbiental(
            id = "R_AGU_02",
            categoria = Categoria.AGUA,
            titulo = "El río no es un basurero",
            reglaSimple = "Nada de residuos ni líquidos raros en ríos y canales.",
            explicacion = "El agua del río la beben animales y personas río abajo; lo que " +
                "se arroja aquí aparece mucho más lejos.",
            ejemplo = "Bolsas y llantas flotando cerca de la orilla del río.",
            accionCorrecta = "Retirar los residuos de la orilla y avisar a la autoridad local.",
            referencia = DS_004_2017
        ),
        ReglaAmbiental(
            id = "R_AGU_03",
            categoria = Categoria.AGUA,
            titulo = "Regar con cabeza",
            reglaSimple = "Riega temprano o al atardecer, y solo lo necesario.",
            explicacion = "Regar al mediodía hace que gran parte del agua se evapore antes " +
                "de llegar a las raíces.",
            ejemplo = "Una manguera regando la vereda a pleno sol del mediodía.",
            accionCorrecta = "Regar a primera hora, dirigir el agua a la tierra y cerrar la manguera.",
            referencia = LEY_29338
        ),
        ReglaAmbiental(
            id = "R_AIR_01",
            categoria = Categoria.AIRE,
            titulo = "La basura no se quema",
            reglaSimple = "Nunca quemes residuos al aire libre.",
            explicacion = "El humo de la basura quemada tiene sustancias tóxicas que " +
                "irritan los ojos y dañan los pulmones.",
            ejemplo = "Alguien prende fuego a un montón de hojas y plásticos en el terreno.",
            accionCorrecta = "Apagar el fuego con seguridad y llevar los residuos al punto de acopio.",
            referencia = DS_003_2017
        ),
        ReglaAmbiental(
            id = "R_AIR_02",
            categoria = Categoria.AIRE,
            titulo = "Motores limpios, aire limpio",
            reglaSimple = "Un motor bien cuidado ensucia mucho menos el aire.",
            explicacion = "Los vehículos con mantenimiento atrasado sueltan humo negro " +
                "cargado de partículas que respiramos todos.",
            ejemplo = "Un camión detenido con el motor encendido soltando humo oscuro.",
            accionCorrecta = "Apagar el motor al estacionar y programar el mantenimiento.",
            referencia = LEY_28611
        ),
        ReglaAmbiental(
            id = "R_AIR_03",
            categoria = Categoria.AIRE,
            titulo = "El polvo también contamina",
            reglaSimple = "Cubre los materiales que sueltan polvo.",
            explicacion = "El polvo fino de obras y canteras se queda en el aire y llega " +
                "hasta las casas vecinas.",
            ejemplo = "Un montículo de arena sin cubrir junto a la pista, levantando polvo.",
            accionCorrecta = "Cubrir el material y humedecerlo para que el polvo no vuele.",
            referencia = DS_003_2017
        ),
        ReglaAmbiental(
            id = "R_RUI_01",
            categoria = Categoria.RUIDO,
            titulo = "Hay lugares que piden silencio",
            reglaSimple = "Baja el volumen cerca de colegios, hospitales y casas.",
            explicacion = "El ruido constante dificulta estudiar, descansar y concentrarse, " +
                "y con el tiempo daña la audición.",
            ejemplo = "Un parlante a todo volumen en la puerta del colegio durante clases.",
            accionCorrecta = "Bajar el volumen y acordar horarios para la música fuerte.",
            referencia = DS_085_2003
        ),
        ReglaAmbiental(
            id = "R_RUI_02",
            categoria = Categoria.RUIDO,
            titulo = "La bocina no es un juguete",
            reglaSimple = "Usa la bocina solo cuando de verdad haga falta.",
            explicacion = "El bocinazo continuo genera estrés en las personas y espanta a " +
                "las aves que viven en la ciudad.",
            ejemplo = "Autos tocando bocina sin parar en un embotellamiento.",
            accionCorrecta = "Esperar sin tocar bocina y respetar las zonas de silencio.",
            referencia = DS_085_2003
        ),
        ReglaAmbiental(
            id = "R_AVE_01",
            categoria = Categoria.AREAS_VERDES,
            titulo = "Los parques se cuidan entre todos",
            reglaSimple = "Camina por los senderos y no pises las plantas.",
            explicacion = "El césped muy pisado se compacta, deja de crecer y el suelo " +
                "queda pelado y polvoriento.",
            ejemplo = "Un atajo marcado sobre el césped, con la tierra dura y sin pasto.",
            accionCorrecta = "Usar los caminos, sembrar de nuevo la zona pelada y cercarla un tiempo.",
            referencia = LEY_27972
        ),
        ReglaAmbiental(
            id = "R_AVE_02",
            categoria = Categoria.AREAS_VERDES,
            titulo = "Un árbol tarda años en crecer",
            reglaSimple = "No cortes ni dañes los árboles.",
            explicacion = "Los árboles dan sombra, refrescan la ciudad, sujetan el suelo y " +
                "son casa de muchos animales.",
            ejemplo = "Un árbol del parque con el tronco tallado y ramas rotas.",
            accionCorrecta = "Reportar el daño, proteger el tronco y regar el árbol mientras se recupera.",
            referencia = LEY_29763
        ),
        ReglaAmbiental(
            id = "R_BIO_01",
            categoria = Categoria.BIODIVERSIDAD,
            titulo = "Mirar sí, molestar no",
            reglaSimple = "Observa a los animales desde lejos y en silencio.",
            explicacion = "Perseguir o asustar a un animal lo obliga a abandonar su nido y " +
                "a gastar la energía que necesita para vivir.",
            ejemplo = "Un grupo corriendo detrás de las aves que descansan en la laguna.",
            accionCorrecta = "Alejarse despacio, bajar la voz y observar desde el sendero.",
            referencia = LEY_29763
        ),
        ReglaAmbiental(
            id = "R_BIO_02",
            categoria = Categoria.BIODIVERSIDAD,
            titulo = "Cada especie tiene su lugar",
            reglaSimple = "No te lleves plantas ni animales de su hábitat.",
            explicacion = "Fuera de su ambiente casi nunca sobreviven, y su ausencia " +
                "desordena toda la cadena de vida del lugar.",
            ejemplo = "Alguien guarda renacuajos del río en una botella para llevárselos.",
            accionCorrecta = "Devolverlos al agua con cuidado y observarlos donde viven.",
            referencia = LEY_26834
        ),
        ReglaAmbiental(
            id = "R_ENE_01",
            categoria = Categoria.ENERGIA,
            titulo = "Si no lo usas, apágalo",
            reglaSimple = "Apaga luces y equipos cuando no haya nadie.",
            explicacion = "Producir electricidad casi siempre genera contaminación; la que " +
                "no gastamos es la más limpia de todas.",
            ejemplo = "Aula vacía con todas las luces y el proyector encendidos.",
            accionCorrecta = "Apagar luces y equipos al salir y aprovechar la luz natural.",
            referencia = LEY_27345
        ),
        ReglaAmbiental(
            id = "R_ENE_02",
            categoria = Categoria.ENERGIA,
            titulo = "El enchufe también consume",
            reglaSimple = "Desconecta los cargadores que no estén cargando.",
            explicacion = "Muchos aparatos siguen consumiendo un poco de energía aunque " +
                "parezcan apagados.",
            ejemplo = "Tres cargadores enchufados sin ningún equipo conectado.",
            accionCorrecta = "Desenchufarlos o usar una regleta con interruptor.",
            referencia = LEY_27345
        ),
        ReglaAmbiental(
            id = "R_CON_01",
            categoria = Categoria.CONTAMINACION,
            titulo = "Lo peligroso va aparte",
            reglaSimple = "Pilas, focos y medicinas nunca van al tacho común.",
            explicacion = "Contienen sustancias que se filtran al suelo y al agua, y una " +
                "sola pila puede contaminar mucho.",
            ejemplo = "Pilas usadas tiradas dentro del tacho de la cocina.",
            accionCorrecta = "Guardarlas aparte y llevarlas a un punto de acopio autorizado.",
            referencia = DL_1278
        ),
        ReglaAmbiental(
            id = "R_CON_02",
            categoria = Categoria.CONTAMINACION,
            titulo = "Pensar antes de construir",
            reglaSimple = "Antes de una obra grande hay que estudiar cómo afectará el ambiente.",
            explicacion = "Anticipar los efectos permite cambiar el plan a tiempo y evitar " +
                "daños difíciles de reparar.",
            ejemplo = "Una planta comienza a operar sin haber revisado adónde irán sus desechos.",
            accionCorrecta = "Revisar el estudio ambiental y aplicar las medidas de prevención.",
            referencia = LEY_27446
        ),
        ReglaAmbiental(
            id = "R_CON_03",
            categoria = Categoria.CONTAMINACION,
            titulo = "Nada se derrama sin control",
            reglaSimple = "Los líquidos de fábricas y talleres nunca van al desagüe común.",
            explicacion = "Aceites y químicos dañan las tuberías, el suelo y la vida de los ríos.",
            ejemplo = "Un bidón oxidado goteando aceite sobre la tierra del patio.",
            accionCorrecta = "Colocar el bidón sobre una bandeja de contención y llamar al encargado.",
            referencia = LEY_28611
        ),
        ReglaAmbiental(
            id = "R_CON_04",
            categoria = Categoria.CONTAMINACION,
            titulo = "Cuidar el clima es cuidarnos",
            reglaSimple = "Reducir el gasto de energía y transporte ayuda al clima.",
            explicacion = "Los gases que calientan el planeta vienen sobre todo de quemar " +
                "combustibles; usar menos es cuidar el futuro.",
            ejemplo = "Trayectos muy cortos hechos siempre en auto particular.",
            accionCorrecta = "Caminar, usar bicicleta o transporte compartido en trayectos cortos.",
            referencia = LEY_30754
        )
    )

    val porId: Map<String, ReglaAmbiental> = reglas.associateBy { it.id }

    fun regla(id: String): ReglaAmbiental? = porId[id]

    fun porCategoria(categoria: Categoria): List<ReglaAmbiental> =
        reglas.filter { it.categoria == categoria }
}
