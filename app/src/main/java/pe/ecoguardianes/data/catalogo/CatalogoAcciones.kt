package pe.ecoguardianes.data.catalogo

import pe.ecoguardianes.domain.model.AccionCorrectiva
import pe.ecoguardianes.domain.model.Categoria

/**
 * Acciones correctivas que el jugador puede proponer para cada problema.
 *
 * Para cada situación problemática hay una acción correcta y dos distractores
 * plausibles. Todas llevan explicación, porque el feedback educativo nunca se
 * limita a "correcto" o "incorrecto".
 */
object CatalogoAcciones {

    private fun grupo(
        situacionId: String,
        categoria: Categoria,
        correcta: Pair<String, String>,
        malaA: Pair<String, String>,
        malaB: Pair<String, String>
    ): List<AccionCorrectiva> = listOf(
        AccionCorrectiva(situacionId + "_A1", correcta.first, true, categoria, correcta.second),
        AccionCorrectiva(situacionId + "_A2", malaA.first, false, categoria, malaA.second),
        AccionCorrectiva(situacionId + "_A3", malaB.first, false, categoria, malaB.second)
    )

    val porSituacion: Map<String, List<AccionCorrectiva>> = mapOf(

        // ---------------- CASA ----------------
        "S_CASA_01" to grupo(
            "S_CASA_01", Categoria.RESIDUOS,
            "Separar los residuos en reciclables, orgánicos y no aprovechables." to
                "Al separarlos, el papel y el plástico limpios sí se pueden reciclar.",
            "Meter todo en una bolsa más grande y cerrarla bien." to
                "La bolsa más grande no arregla nada: los materiales siguen mezclados.",
            "Esperar a que el camión de basura los separe después." to
                "El camión no separa nada; una vez mezclados casi todo se pierde."
        ),
        "S_CASA_02" to grupo(
            "S_CASA_02", Categoria.AGUA,
            "Cerrar el caño y avisar si sigue goteando." to
                "Cerrar a tiempo evita perder decenas de litros cada día.",
            "Poner un balde debajo para aprovechar el agua." to
                "Recoger el agua ayuda algo, pero el caño sigue abierto y el gasto continúa.",
            "Dejarlo abierto para que no se tape la tubería." to
                "Las tuberías no se tapan por cerrar el caño; eso solo desperdicia agua."
        ),
        "S_CASA_03" to grupo(
            "S_CASA_03", Categoria.ENERGIA,
            "Apagar las luces y abrir la cortina para usar luz natural." to
                "La luz del día es gratis y no genera contaminación.",
            "Cambiar los focos por otros de más potencia." to
                "Más potencia significa más consumo: el problema empeora.",
            "Dejarlas encendidas para que no se quemen al prender y apagar." to
                "Encender y apagar no daña los focos actuales; dejarlas gasta energía."
        ),
        "S_CASA_04" to grupo(
            "S_CASA_04", Categoria.CONTAMINACION,
            "Guardar las pilas aparte y llevarlas a un punto de acopio." to
                "Las pilas tienen metales que contaminan el suelo y el agua.",
            "Envolverlas en papel antes de botarlas al tacho." to
                "El papel no detiene las sustancias peligrosas que hay dentro.",
            "Enterrarlas en el jardín para que no molesten." to
                "Enterrarlas lleva los metales directamente al suelo y al agua subterránea."
        ),
        "S_CASA_05" to grupo(
            "S_CASA_05", Categoria.ENERGIA,
            "Desenchufar los cargadores o usar una regleta con interruptor." to
                "Aunque no carguen nada, siguen consumiendo un poco de energía.",
            "Dejarlos enchufados porque gastan muy poco." to
                "Poco por muchas horas y muchos aparatos termina siendo bastante.",
            "Enchufarlos todos en una sola extensión sin interruptor." to
                "Juntarlos no reduce el consumo si siguen conectados a la corriente."
        ),

        // ---------------- ESCUELA ----------------
        "S_ESC_01" to grupo(
            "S_ESC_01", Categoria.RESIDUOS,
            "Recoger la basura con guantes y colocarla dentro del contenedor." to
                "Dentro del contenedor no la esparce el viento ni atrae plagas.",
            "Barrerla hacia un rincón del patio." to
                "Solo cambia de sitio el problema; los residuos siguen sueltos.",
            "Esperar a que el personal de limpieza pase mañana." to
                "En un día el viento ya la habrá repartido por todo el patio."
        ),
        "S_ESC_02" to grupo(
            "S_ESC_02", Categoria.ENERGIA,
            "Apagar luces y proyector al salir del aula." to
                "La energía que no se usa es la más limpia que existe.",
            "Bajar el brillo del proyector y dejarlo encendido." to
                "Sigue consumiendo casi lo mismo aunque el brillo sea menor.",
            "Poner un cartel y no hacer nada más hoy." to
                "El cartel ayuda a futuro, pero ahora los equipos siguen gastando."
        ),
        "S_ESC_03" to grupo(
            "S_ESC_03", Categoria.AGUA,
            "Reportar la fuga para que cambien el empaque del caño." to
                "Reparar la fuga corta el desperdicio de raíz.",
            "Ajustar el caño con más fuerza cada vez." to
                "Forzarlo desgasta la rosca y al final gotea todavía más.",
            "Cerrar la llave general del baño todo el día." to
                "Dejaría a todo el colegio sin agua sin arreglar la fuga."
        ),
        "S_ESC_04" to grupo(
            "S_ESC_04", Categoria.RUIDO,
            "Bajar el volumen y acordar horarios para la música." to
                "En horario de clases el ruido impide concentrarse y aprender.",
            "Mover el parlante al otro extremo del patio." to
                "El sonido sigue llegando a las aulas: el volumen no cambió.",
            "Cerrar las ventanas de las aulas." to
                "Encierra el calor y el ruido igual entra; no resuelve la causa."
        ),
        "S_ESC_05" to grupo(
            "S_ESC_05", Categoria.RESIDUOS,
            "Reemplazar los descartables por tomatodo y vajilla lavable." to
                "Un objeto reutilizable evita cientos de descartables al año.",
            "Comprar descartables de un color distinto." to
                "El color no cambia nada: siguen siendo de un solo uso.",
            "Usar dos vasos descartables para que no se rompan." to
                "Duplica el residuo en lugar de reducirlo."
        ),

        // ---------------- PARQUE ----------------
        "S_PAR_01" to grupo(
            "S_PAR_01", Categoria.AREAS_VERDES,
            "Cercar la zona, sembrar césped nuevo y señalizar el sendero." to
                "Protegida y con un camino claro, la zona se recupera sola.",
            "Echar más agua sobre la tierra dura." to
                "Sin descompactar el suelo el agua escurre y el pasto no crece.",
            "Cubrir la zona pelada con arena." to
                "La arena impide que germine el césped y empeora el suelo."
        ),
        "S_PAR_02" to grupo(
            "S_PAR_02", Categoria.AREAS_VERDES,
            "Reportar el daño, proteger el tronco y regar mientras se recupera." to
                "Un árbol tarda años en crecer; cuidarlo a tiempo lo salva.",
            "Cortar el árbol y plantar otro más pequeño." to
                "Se pierden años de sombra y de refugio para los animales.",
            "Pintar el tronco para tapar las marcas." to
                "La pintura oculta la herida, pero el árbol sigue igual de dañado."
        ),
        "S_PAR_03" to grupo(
            "S_PAR_03", Categoria.RESIDUOS,
            "Recoger los residuos y separarlos en la estación de reciclaje." to
                "El parque queda limpio y los materiales vuelven a usarse.",
            "Amontonarlos junto a un árbol para recogerlos luego." to
                "El montón atrae insectos y el viento vuelve a esparcirlo.",
            "Enterrar las botellas al pie del árbol." to
                "El plástico enterrado dura siglos y daña las raíces."
        ),
        "S_PAR_04" to grupo(
            "S_PAR_04", Categoria.BIODIVERSIDAD,
            "Alejarse despacio, bajar la voz y observar desde el sendero." to
                "Un animal tranquilo se queda; uno asustado abandona su nido.",
            "Darles pan para que se acerquen y se calmen." to
                "El pan les hace daño y los vuelve dependientes de las personas.",
            "Atraparlas un momento para verlas de cerca." to
                "Atraparlas les provoca un estrés que puede ser mortal."
        ),
        "S_PAR_05" to grupo(
            "S_PAR_05", Categoria.AGUA,
            "Regar al atardecer, apuntando a la tierra y cerrando al terminar." to
                "A esa hora casi toda el agua llega a las raíces.",
            "Regar más rápido pero con más presión." to
                "Con más presión el agua rebota y se pierde igual.",
            "Regar también la vereda para asentar el polvo." to
                "El agua potable no debe usarse para lavar la vereda."
        ),

        // ---------------- RÍO ----------------
        "S_RIO_01" to grupo(
            "S_RIO_01", Categoria.AGUA,
            "Retirar los residuos de la orilla y avisar a la autoridad local." to
                "Lo que se saca hoy no viajará río abajo mañana.",
            "Empujar las bolsas hacia el centro para que se vayan." to
                "El problema solo se traslada a las comunidades de aguas abajo.",
            "Quemar los residuos en la orilla." to
                "Quemarlos cambia contaminación del agua por humo tóxico en el aire."
        ),
        "S_RIO_02" to grupo(
            "S_RIO_02", Categoria.CONTAMINACION,
            "No tocar el líquido, anotar lugar y hora, y avisar a la autoridad." to
                "El registro sirve de prueba y protege a quien lo reporta.",
            "Tapar la salida de la tubería con piedras." to
                "El líquido buscará otra salida y puedes exponerte a sustancias peligrosas.",
            "Echar mucha agua limpia para diluirlo." to
                "Diluir no elimina el contaminante: solo lo reparte más lejos."
        ),
        "S_RIO_03" to grupo(
            "S_RIO_03", Categoria.BIODIVERSIDAD,
            "Registrar la zona afectada y reportar el cambio en el agua." to
                "Los peces que se alejan avisan de un problema antes que nadie.",
            "Alimentar a los peces para que vuelvan." to
                "El alimento sobrante se pudre y quita todavía más oxígeno al agua.",
            "Trasladarlos a otra parte del río en baldes." to
                "Moverlos los estresa y no corrige la causa de la contaminación."
        ),
        "S_RIO_04" to grupo(
            "S_RIO_04", Categoria.BIODIVERSIDAD,
            "Devolver los renacuajos al mismo punto del río con cuidado." to
                "Fuera de su hábitat casi ninguno llega a ser rana adulta.",
            "Llevarlos a casa en una pecera con agua del caño." to
                "El agua del caño tiene cloro y para ellos es mortal.",
            "Soltarlos en otro río que esté más limpio." to
                "Cada río tiene su propio equilibrio; mezclarlos causa problemas."
        ),
        "S_RIO_05" to grupo(
            "S_RIO_05", Categoria.AIRE,
            "Apagar el fuego con seguridad y llevar los residuos al acopio." to
                "El humo del plástico quemado es tóxico para todos los que respiran cerca.",
            "Dejar que se consuma solo y vigilar de lejos." to
                "Mientras arde sigue soltando humo peligroso y puede propagarse.",
            "Echar tierra encima y seguir quemando el resto mañana." to
                "Repetir la quema repite el daño; el plástico nunca debe quemarse."
        ),

        // ---------------- CIUDAD ----------------
        "S_CIU_01" to grupo(
            "S_CIU_01", Categoria.RUIDO,
            "Esperar sin tocar bocina y respetar las zonas de silencio." to
                "El bocinazo continuo genera estrés y no adelanta el tráfico.",
            "Tocar la bocina más fuerte para avanzar antes." to
                "El embotellamiento no se mueve por el ruido: solo aumenta la molestia.",
            "Subir la música del auto para no oír las bocinas." to
                "Añade una fuente de ruido más al mismo lugar."
        ),
        "S_CIU_02" to grupo(
            "S_CIU_02", Categoria.AIRE,
            "Apagar el motor al estacionar y programar el mantenimiento." to
                "Un motor cuidado gasta menos y ensucia mucho menos el aire.",
            "Acelerar a fondo para que el humo salga de una vez." to
                "Acelerar libera todavía más partículas al aire.",
            "Estacionar más lejos con el motor encendido." to
                "El humo sigue produciéndose, solo que en otra cuadra."
        ),
        "S_CIU_03" to grupo(
            "S_CIU_03", Categoria.AIRE,
            "Cubrir el montículo con malla y humedecer el material." to
                "Cubierto y húmedo, el polvo fino deja de volar hacia las casas.",
            "Barrer la vereda cada mañana con escoba seca." to
                "Barrer en seco levanta de nuevo todo el polvo al aire.",
            "Mover la arena al otro lado de la pista." to
                "El montículo sigue descubierto y el viento hace lo mismo."
        ),
        "S_CIU_04" to grupo(
            "S_CIU_04", Categoria.RESIDUOS,
            "Reportar el punto crítico y colocar contenedores señalizados." to
                "Con un lugar claro y visible la basura deja de acumularse en la vereda.",
            "Poner un cartel de prohibido botar basura y nada más." to
                "Sin un contenedor cerca el cartel se ignora y el montón sigue creciendo.",
            "Empujar la basura hacia la pista para que la recoja el camión." to
                "En la pista se dispersa, tapa los desagües y es peligrosa."
        ),
        "S_CIU_05" to grupo(
            "S_CIU_05", Categoria.CONTAMINACION,
            "Caminar, usar bicicleta o compartir el viaje en trayectos cortos." to
                "Los trayectos cortos en auto son los que más contaminan por kilómetro.",
            "Cambiar el auto por otro más grande y cómodo." to
                "Un vehículo más grande consume más combustible por viaje.",
            "Hacer el mismo trayecto pero más rápido." to
                "Ir más rápido en distancias cortas aumenta el consumo."
        ),

        // ---------------- ZONA INDUSTRIAL ----------------
        "S_IND_01" to grupo(
            "S_IND_01", Categoria.AIRE,
            "Revisar e instalar los filtros de la chimenea y vigilar la emisión." to
                "Los filtros retienen las partículas antes de que salgan al aire.",
            "Construir una chimenea más alta." to
                "Más altura solo reparte el humo más lejos; sigue contaminando.",
            "Producir de noche para que no se note el humo." to
                "Que no se vea no significa que no esté; el aire se ensucia igual."
        ),
        "S_IND_02" to grupo(
            "S_IND_02", Categoria.CONTAMINACION,
            "Colocar el bidón sobre una bandeja de contención y avisar al encargado." to
                "La bandeja retiene el aceite y evita que llegue al suelo.",
            "Cubrir la mancha de aceite con tierra seca." to
                "La tierra oculta la mancha pero el aceite sigue filtrándose.",
            "Lavar la zona con agua a presión hacia el desagüe." to
                "Eso lleva el aceite directamente al alcantarillado y al río."
        ),
        "S_IND_03" to grupo(
            "S_IND_03", Categoria.CONTAMINACION,
            "Separar los residuos peligrosos y almacenarlos rotulados y aparte." to
                "Rotulados y separados pueden ir a un tratamiento seguro.",
            "Compactar todo junto para que ocupe menos espacio." to
                "Compactar mezcla aún más lo peligroso con lo común.",
            "Guardarlos en el patio hasta que alguien decida." to
                "A la intemperie la lluvia arrastra las sustancias al suelo."
        ),
        "S_IND_04" to grupo(
            "S_IND_04", Categoria.RUIDO,
            "Encerrar la máquina con barrera acústica y limitar el horario." to
                "La barrera baja el ruido y el horario protege el descanso de los vecinos.",
            "Repartir tapones para los oídos a los vecinos." to
                "Traslada la carga a quien sufre el ruido en vez de reducirlo.",
            "Trabajar de madrugada, cuando hay menos gente en la calle." to
                "De madrugada el ruido molesta todavía más: la gente duerme."
        ),
        "S_IND_05" to grupo(
            "S_IND_05", Categoria.CONTAMINACION,
            "Revisar el estudio ambiental y aplicar las medidas de prevención." to
                "Anticipar los efectos permite corregir el plan antes de causar daño.",
            "Empezar a operar y corregir los problemas si aparecen." to
                "Algunos daños ambientales ya no se pueden reparar después.",
            "Reducir la producción a la mitad sin revisar nada." to
                "Producir menos no responde a la pregunta de adónde irán los desechos."
        )
    )

    val acciones: List<AccionCorrectiva> = porSituacion.values.flatten()

    val porId: Map<String, AccionCorrectiva> = acciones.associateBy { it.id }

    fun idsDe(situacionId: String): List<String> =
        porSituacion[situacionId]?.map { it.id } ?: emptyList()

    fun accionesDe(situacionId: String): List<AccionCorrectiva> =
        porSituacion[situacionId] ?: emptyList()

    fun correctaDe(situacionId: String): AccionCorrectiva? =
        porSituacion[situacionId]?.firstOrNull { it.esCorrecta }

    fun accion(id: String?): AccionCorrectiva? = id?.let { porId[it] }
}
