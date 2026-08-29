package pe.ecoguardianes.data.catalogo

import pe.ecoguardianes.domain.model.Categoria
import pe.ecoguardianes.domain.model.Gravedad
import pe.ecoguardianes.domain.model.IconoAmb
import pe.ecoguardianes.domain.model.Mision
import pe.ecoguardianes.domain.model.Situacion
import pe.ecoguardianes.domain.model.ZonaId

/**
 * Contenido de los seis escenarios de EcoGuardianes.
 *
 * Cada zona mezcla situaciones correctas y problemáticas: el jugador tiene que
 * observar antes de acusar, porque marcar como problema algo que está bien
 * resta precisión en la ficha de auditoría.
 */
object CatalogoEscenarios {

    private fun sit(
        id: String,
        zona: ZonaId,
        nombre: String,
        icono: IconoAmb,
        x: Float,
        y: Float,
        categoria: Categoria,
        gravedad: Gravedad,
        observacion: String,
        explicacion: String,
        pista: String,
        reglaId: String,
        retoId: String? = null,
        xp: Int = 20,
        nivelMinimo: Int = 1
    ) = Situacion(
        id = id,
        zona = zona,
        nombre = nombre,
        icono = icono,
        x = x,
        y = y,
        categoria = categoria,
        gravedad = gravedad,
        observacion = observacion,
        explicacion = explicacion,
        pista = pista,
        reglaId = reglaId,
        accionesIds = CatalogoAcciones.idsDe(id),
        retoId = retoId,
        xp = xp,
        nivelMinimo = nivelMinimo
    )

    val situaciones: List<Situacion> = listOf(

        // ============================ CASA ============================
        sit(
            id = "S_CASA_01", zona = ZonaId.CASA,
            nombre = "Residuos mezclados en la cocina",
            icono = IconoAmb.BOLSA_BASURA, x = 0.22f, y = 0.62f,
            categoria = Categoria.RESIDUOS, gravedad = Gravedad.NO_CONFORMIDAD,
            observacion = "Una sola bolsa con cáscaras, botellas y periódicos revueltos.",
            explicacion = "Cuando todo se mezcla, el papel se moja y el plástico se ensucia: " +
                "ya no sirven para reciclar y terminan en el botadero.",
            pista = "Mira dentro de la bolsa de la cocina. ¿Todo lo que hay es la misma clase de residuo?",
            reglaId = "R_RES_01", retoId = "RETO_CLASIFICAR_COCINA", xp = 25
        ),
        sit(
            id = "S_CASA_02", zona = ZonaId.CASA,
            nombre = "Caño abierto en el lavadero",
            icono = IconoAmb.GRIFO, x = 0.46f, y = 0.55f,
            categoria = Categoria.AGUA, gravedad = Gravedad.NO_CONFORMIDAD,
            observacion = "El agua corre sin parar y no hay nadie usándola.",
            explicacion = "Un caño abierto un minuto de más, varias veces al día, se convierte " +
                "en muchos litros perdidos cada semana.",
            pista = "Escucha con atención. ¿Oyes agua corriendo cuando no debería?",
            reglaId = "R_AGU_01", retoId = "RETO_CERRAR_CANOS", xp = 25
        ),
        sit(
            id = "S_CASA_03", zona = ZonaId.CASA,
            nombre = "Luces encendidas a plena luz del día",
            icono = IconoAmb.BOMBILLA, x = 0.70f, y = 0.34f,
            categoria = Categoria.ENERGIA, gravedad = Gravedad.OBSERVACION,
            observacion = "Los focos de la sala están encendidos y entra sol por la ventana.",
            explicacion = "Casi toda la energía eléctrica cuesta recursos y contaminación; " +
                "usar luz natural es gratis y limpio.",
            pista = "Compara la ventana con los focos. ¿De verdad hacen falta los dos?",
            reglaId = "R_ENE_01", retoId = "RETO_APAGAR_LUCES", xp = 18
        ),
        sit(
            id = "S_CASA_04", zona = ZonaId.CASA,
            nombre = "Pilas usadas en el tacho común",
            icono = IconoAmb.PILA, x = 0.33f, y = 0.78f,
            categoria = Categoria.CONTAMINACION, gravedad = Gravedad.NO_CONFORMIDAD,
            observacion = "Tres pilas gastadas entre los residuos de la cocina.",
            explicacion = "Las pilas llevan metales que se filtran al suelo y al agua. " +
                "Una sola puede contaminar una cantidad enorme de agua.",
            pista = "Revisa el tacho de la cocina. ¿Hay algo que no debería estar ahí?",
            reglaId = "R_CON_01", retoId = "RETO_PELIGROSOS", xp = 30, nivelMinimo = 2
        ),
        sit(
            id = "S_CASA_05", zona = ZonaId.CASA,
            nombre = "Cargadores enchufados sin uso",
            icono = IconoAmb.ENCHUFE, x = 0.60f, y = 0.72f,
            categoria = Categoria.ENERGIA, gravedad = Gravedad.OBSERVACION,
            observacion = "Dos cargadores conectados y ningún aparato cargando.",
            explicacion = "Muchos equipos siguen consumiendo un poco de energía aunque " +
                "no estén haciendo nada: es el consumo fantasma.",
            pista = "Sigue los cables. ¿Adónde llevan?",
            reglaId = "R_ENE_02", retoId = "RETO_DESENCHUFAR", xp = 18
        ),
        sit(
            id = "S_CASA_06", zona = ZonaId.CASA,
            nombre = "Tachos separados y rotulados",
            icono = IconoAmb.CONTENEDOR, x = 0.83f, y = 0.66f,
            categoria = Categoria.RESIDUOS, gravedad = Gravedad.CONFORME,
            observacion = "Tres recipientes con etiqueta: reciclable, orgánico y no aprovechable.",
            explicacion = "Separar en el origen es la manera más eficaz de que los materiales " +
                "vuelvan a usarse.",
            pista = "Aquí alguien hizo bien su trabajo. Obsérvalo y sigue adelante.",
            reglaId = "R_RES_01", xp = 12
        ),
        sit(
            id = "S_CASA_07", zona = ZonaId.CASA,
            nombre = "Macetas regadas al atardecer",
            icono = IconoAmb.FLOR, x = 0.12f, y = 0.40f,
            categoria = Categoria.AGUA, gravedad = Gravedad.CONFORME,
            observacion = "La tierra está húmeda y el sol ya está bajando.",
            explicacion = "Regar cuando el sol baja evita que el agua se evapore antes de " +
                "llegar a las raíces.",
            pista = "Fíjate en la hora y en la tierra. ¿Ves algo mal hecho?",
            reglaId = "R_AGU_03", xp = 12
        ),

        // ============================ ESCUELA ============================
        sit(
            id = "S_ESC_01", zona = ZonaId.ESCUELA,
            nombre = "Basura fuera del contenedor",
            icono = IconoAmb.BOLSA_BASURA, x = 0.20f, y = 0.70f,
            categoria = Categoria.RESIDUOS, gravedad = Gravedad.NO_CONFORMIDAD,
            observacion = "Bolsas y envolturas alrededor del tacho del patio.",
            explicacion = "La basura suelta tapa desagües, se la lleva el viento y atrae " +
                "moscas y roedores que transmiten enfermedades.",
            pista = "No mires solo dentro del tacho: mira también el suelo alrededor.",
            reglaId = "R_RES_02", retoId = "RETO_PATIO_LIMPIO", xp = 25
        ),
        sit(
            id = "S_ESC_02", zona = ZonaId.ESCUELA,
            nombre = "Aula vacía con todo encendido",
            icono = IconoAmb.PANTALLA, x = 0.55f, y = 0.32f,
            categoria = Categoria.ENERGIA, gravedad = Gravedad.NO_CONFORMIDAD,
            observacion = "Luces y proyector funcionando en un aula sin nadie dentro.",
            explicacion = "Un aula vacía con todo encendido puede gastar en una mañana lo " +
                "mismo que una casa entera.",
            pista = "Asómate a las ventanas del segundo piso. ¿Hay alguien ahí?",
            reglaId = "R_ENE_01", retoId = "RETO_APAGAR_LUCES", xp = 25
        ),
        sit(
            id = "S_ESC_03", zona = ZonaId.ESCUELA,
            nombre = "Caño del baño goteando",
            icono = IconoAmb.GOTA, x = 0.78f, y = 0.58f,
            categoria = Categoria.AGUA, gravedad = Gravedad.OBSERVACION,
            observacion = "Una gota cae cada dos segundos, todo el día.",
            explicacion = "Una gota constante parece poco, pero puede sumar varios baldes " +
                "de agua limpia cada día.",
            pista = "A veces el problema no se ve: se oye.",
            reglaId = "R_AGU_01", retoId = "RETO_CERRAR_CANOS", xp = 18
        ),
        sit(
            id = "S_ESC_04", zona = ZonaId.ESCUELA,
            nombre = "Parlante a todo volumen en horario de clase",
            icono = IconoAmb.ALTAVOZ, x = 0.38f, y = 0.44f,
            categoria = Categoria.RUIDO, gravedad = Gravedad.NO_CONFORMIDAD,
            observacion = "La música se escucha desde la calle y desde todas las aulas.",
            explicacion = "El ruido alto impide concentrarse, cansa y, si es constante, " +
                "termina dañando la audición.",
            pista = "¿Podrías estudiar tú con ese sonido de fondo?",
            reglaId = "R_RUI_01", retoId = "RETO_SILENCIO", xp = 25, nivelMinimo = 2
        ),
        sit(
            id = "S_ESC_05", zona = ZonaId.ESCUELA,
            nombre = "Descartables en el quiosco",
            icono = IconoAmb.BOTELLA, x = 0.66f, y = 0.76f,
            categoria = Categoria.RESIDUOS, gravedad = Gravedad.OBSERVACION,
            observacion = "Cada refresco se entrega con vaso y sorbete de plástico.",
            explicacion = "Un vaso descartable se usa cinco minutos y después permanece " +
                "muchísimos años en el ambiente.",
            pista = "Cuenta cuántos plásticos se usan en un solo recreo.",
            reglaId = "R_RES_03", retoId = "RETO_DESCARTABLES", xp = 20
        ),
        sit(
            id = "S_ESC_06", zona = ZonaId.ESCUELA,
            nombre = "Biohuerto escolar cuidado",
            icono = IconoAmb.CESPED, x = 0.14f, y = 0.42f,
            categoria = Categoria.AREAS_VERDES, gravedad = Gravedad.CONFORME,
            observacion = "Camas de cultivo ordenadas, con riego por goteo y compost.",
            explicacion = "El biohuerto enseña de dónde viene la comida y aprovecha los " +
                "restos orgánicos del colegio.",
            pista = "Aquí el trabajo está bien hecho. Anótalo como situación correcta.",
            reglaId = "R_AVE_01", xp = 12
        ),
        sit(
            id = "S_ESC_07", zona = ZonaId.ESCUELA,
            nombre = "Punto de acopio de papel señalizado",
            icono = IconoAmb.PAPEL, x = 0.88f, y = 0.38f,
            categoria = Categoria.RESIDUOS, gravedad = Gravedad.CONFORME,
            observacion = "Caja rotulada, con papel limpio y seco, bajo techo.",
            explicacion = "El papel limpio y seco se recicla sin problema; mojado o sucio, no.",
            pista = "Comprueba si el papel está limpio y protegido de la lluvia.",
            reglaId = "R_RES_04", xp = 12
        ),

        // ============================ PARQUE ============================
        sit(
            id = "S_PAR_01", zona = ZonaId.PARQUE,
            nombre = "Césped pisoteado por un atajo",
            icono = IconoAmb.CESPED, x = 0.30f, y = 0.72f,
            categoria = Categoria.AREAS_VERDES, gravedad = Gravedad.OBSERVACION,
            observacion = "Una franja de tierra dura cruza el pasto de lado a lado.",
            explicacion = "El suelo tan pisado se compacta: el agua no entra y las raíces " +
                "no pueden crecer.",
            pista = "Sigue las huellas. ¿Adónde va la gente cuando corta camino?",
            reglaId = "R_AVE_01", retoId = "RETO_CESPED", xp = 20
        ),
        sit(
            id = "S_PAR_02", zona = ZonaId.PARQUE,
            nombre = "Árbol con ramas rotas y tronco tallado",
            icono = IconoAmb.ARBOL_SECO, x = 0.62f, y = 0.40f,
            categoria = Categoria.AREAS_VERDES, gravedad = Gravedad.NO_CONFORMIDAD,
            observacion = "Cortes profundos en la corteza y dos ramas quebradas.",
            explicacion = "La corteza es la piel del árbol: por ahí entran plagas y " +
                "enfermedades que pueden matarlo.",
            pista = "Acércate al tronco del árbol grande y míralo de cerca.",
            reglaId = "R_AVE_02", retoId = "RETO_ARBOL", xp = 25
        ),
        sit(
            id = "S_PAR_03", zona = ZonaId.PARQUE,
            nombre = "Botellas y papeles esparcidos",
            icono = IconoAmb.BOTELLA, x = 0.44f, y = 0.82f,
            categoria = Categoria.RESIDUOS, gravedad = Gravedad.NO_CONFORMIDAD,
            observacion = "Residuos repartidos por el pasto después de un partido.",
            explicacion = "Además de afear el parque, el vidrio roto y las latas pueden " +
                "herir a las personas y a los animales.",
            pista = "Recorre la zona de juegos con la mirada, sin prisa.",
            reglaId = "R_RES_02", retoId = "RETO_PATIO_LIMPIO", xp = 22
        ),
        sit(
            id = "S_PAR_04", zona = ZonaId.PARQUE,
            nombre = "Aves perseguidas junto a la laguna",
            icono = IconoAmb.PAJARO, x = 0.78f, y = 0.58f,
            categoria = Categoria.BIODIVERSIDAD, gravedad = Gravedad.NO_CONFORMIDAD,
            observacion = "Varias personas corren detrás de las aves que descansaban.",
            explicacion = "Al huir, las aves gastan la energía que necesitan para alimentarse " +
                "y a veces abandonan sus nidos para siempre.",
            pista = "Mira hacia la laguna. ¿Están tranquilas las aves?",
            reglaId = "R_BIO_01", retoId = "RETO_AVES", xp = 28, nivelMinimo = 2
        ),
        sit(
            id = "S_PAR_05", zona = ZonaId.PARQUE,
            nombre = "Manguera regando la vereda al mediodía",
            icono = IconoAmb.MANGUERA, x = 0.16f, y = 0.55f,
            categoria = Categoria.AGUA, gravedad = Gravedad.NO_CONFORMIDAD,
            observacion = "El agua cae sobre el cemento a la hora de más sol.",
            explicacion = "Al mediodía gran parte del agua se evapora, y la que cae en la " +
                "vereda no sirve a ninguna planta.",
            pista = "Mira dónde termina el chorro de agua.",
            reglaId = "R_AGU_03", retoId = "RETO_RIEGO", xp = 22
        ),
        sit(
            id = "S_PAR_06", zona = ZonaId.PARQUE,
            nombre = "Estación de reciclaje con tres contenedores",
            icono = IconoAmb.CONTENEDOR, x = 0.88f, y = 0.78f,
            categoria = Categoria.RESIDUOS, gravedad = Gravedad.CONFORME,
            observacion = "Contenedores rotulados, con tapa y sin residuos alrededor.",
            explicacion = "Una estación bien mantenida hace que separar sea fácil para todos.",
            pista = "Revisa si están limpios y bien señalizados.",
            reglaId = "R_RES_01", xp = 12
        ),
        sit(
            id = "S_PAR_07", zona = ZonaId.PARQUE,
            nombre = "Nido protegido con cartel informativo",
            icono = IconoAmb.NIDO, x = 0.70f, y = 0.24f,
            categoria = Categoria.BIODIVERSIDAD, gravedad = Gravedad.CONFORME,
            observacion = "Zona acordonada con un cartel que pide no acercarse.",
            explicacion = "Señalizar un nido permite que la gente lo observe sin molestar " +
                "a las crías.",
            pista = "Lee el cartel antes de decidir si algo está mal.",
            reglaId = "R_BIO_01", xp = 12
        ),

        // ============================ RÍO ============================
        sit(
            id = "S_RIO_01", zona = ZonaId.RIO,
            nombre = "Bolsas y llantas en la orilla",
            icono = IconoAmb.BOLSA_BASURA, x = 0.24f, y = 0.66f,
            categoria = Categoria.AGUA, gravedad = Gravedad.NO_CONFORMIDAD,
            observacion = "Residuos atrapados entre las ramas de la orilla.",
            explicacion = "Todo lo que entra al río viaja aguas abajo hasta las comunidades " +
                "que beben, pescan y riegan con esa agua.",
            pista = "Camina por la orilla y mira entre las ramas bajas.",
            reglaId = "R_AGU_02", retoId = "RETO_LIMPIAR_RIO", xp = 25
        ),
        sit(
            id = "S_RIO_02", zona = ZonaId.RIO,
            nombre = "Tubería vertiendo agua turbia",
            icono = IconoAmb.TUBERIA, x = 0.58f, y = 0.50f,
            categoria = Categoria.CONTAMINACION, gravedad = Gravedad.NO_CONFORMIDAD,
            observacion = "Un tubo descarga agua gris con espuma directamente al cauce.",
            explicacion = "Las descargas sin tratamiento cambian el color, el olor y el " +
                "oxígeno del agua, y matan la vida del río.",
            pista = "Sigue el color del agua hasta encontrar de dónde sale.",
            reglaId = "R_CON_03", retoId = "RETO_VERTIMIENTO", xp = 32, nivelMinimo = 3
        ),
        sit(
            id = "S_RIO_03", zona = ZonaId.RIO,
            nombre = "Peces alejados de la zona turbia",
            icono = IconoAmb.PEZ, x = 0.42f, y = 0.80f,
            categoria = Categoria.BIODIVERSIDAD, gravedad = Gravedad.OBSERVACION,
            observacion = "No se ve ni un pez cerca de la mancha gris del agua.",
            explicacion = "Los peces son un aviso temprano: cuando se van, al agua ya le " +
                "falta oxígeno o tiene algo que les hace daño.",
            pista = "Compara la parte limpia del río con la parte turbia.",
            reglaId = "R_BIO_02", retoId = "RETO_PECES", xp = 26, nivelMinimo = 3
        ),
        sit(
            id = "S_RIO_04", zona = ZonaId.RIO,
            nombre = "Renacuajos capturados en una botella",
            icono = IconoAmb.RANA, x = 0.74f, y = 0.72f,
            categoria = Categoria.BIODIVERSIDAD, gravedad = Gravedad.NO_CONFORMIDAD,
            observacion = "Una botella con agua y varios renacuajos dentro, al sol.",
            explicacion = "Dentro de la botella el agua se calienta y se queda sin oxígeno: " +
                "los renacuajos no sobreviven.",
            pista = "Mira lo que alguien dejó sobre las piedras.",
            reglaId = "R_BIO_02", retoId = "RETO_DEVOLVER_FAUNA", xp = 26
        ),
        sit(
            id = "S_RIO_05", zona = ZonaId.RIO,
            nombre = "Fogata con plásticos en la ribera",
            icono = IconoAmb.HUMO, x = 0.14f, y = 0.36f,
            categoria = Categoria.AIRE, gravedad = Gravedad.NO_CONFORMIDAD,
            observacion = "Humo negro y olor fuerte saliendo de un montón encendido.",
            explicacion = "El plástico quemado libera sustancias tóxicas que irritan los " +
                "ojos y los pulmones de quien está cerca.",
            pista = "El humo negro y el olor picante nunca son buena señal.",
            reglaId = "R_AIR_01", retoId = "RETO_FOGATA", xp = 28
        ),
        sit(
            id = "S_RIO_06", zona = ZonaId.RIO,
            nombre = "Vegetación de ribera conservada",
            icono = IconoAmb.ARBOL, x = 0.86f, y = 0.42f,
            categoria = Categoria.AREAS_VERDES, gravedad = Gravedad.CONFORME,
            observacion = "Una franja de arbustos y árboles acompaña la orilla.",
            explicacion = "Las plantas de la ribera sujetan la tierra, filtran el agua y " +
                "dan refugio a la fauna.",
            pista = "Una orilla con plantas es una orilla sana.",
            reglaId = "R_AVE_02", xp = 12
        ),
        sit(
            id = "S_RIO_07", zona = ZonaId.RIO,
            nombre = "Mirador de observación de aves",
            icono = IconoAmb.PAJARO, x = 0.34f, y = 0.28f,
            categoria = Categoria.BIODIVERSIDAD, gravedad = Gravedad.CONFORME,
            observacion = "Plataforma a distancia, con cartel de observación silenciosa.",
            explicacion = "Observar de lejos permite disfrutar de la fauna sin alterarla.",
            pista = "Observar no es molestar: aquí se hace bien.",
            reglaId = "R_BIO_01", xp = 12
        ),

        // ============================ CIUDAD ============================
        sit(
            id = "S_CIU_01", zona = ZonaId.CIUDAD,
            nombre = "Bocinazos en el embotellamiento",
            icono = IconoAmb.BOCINA, x = 0.30f, y = 0.62f,
            categoria = Categoria.RUIDO, gravedad = Gravedad.NO_CONFORMIDAD,
            observacion = "Decenas de autos tocando bocina sin avanzar.",
            explicacion = "El ruido continuo del tráfico produce estrés, dificulta el " +
                "descanso y ahuyenta a las aves urbanas.",
            pista = "Escucha la avenida antes de mirarla.",
            reglaId = "R_RUI_02", retoId = "RETO_SILENCIO", xp = 24
        ),
        sit(
            id = "S_CIU_02", zona = ZonaId.CIUDAD,
            nombre = "Camión con humo negro y motor encendido",
            icono = IconoAmb.AUTO, x = 0.56f, y = 0.70f,
            categoria = Categoria.AIRE, gravedad = Gravedad.NO_CONFORMIDAD,
            observacion = "Estacionado y con el motor en marcha, soltando humo oscuro.",
            explicacion = "Las partículas del humo entran hasta lo más profundo de los " +
                "pulmones; los niños son especialmente sensibles.",
            pista = "Busca de dónde sale la nube oscura de la esquina.",
            reglaId = "R_AIR_02", retoId = "RETO_HUMO", xp = 28
        ),
        sit(
            id = "S_CIU_03", zona = ZonaId.CIUDAD,
            nombre = "Montículo de arena sin cubrir",
            icono = IconoAmb.POLVO, x = 0.76f, y = 0.56f,
            categoria = Categoria.AIRE, gravedad = Gravedad.OBSERVACION,
            observacion = "Material de obra amontonado al borde de la pista, al viento.",
            explicacion = "El polvo fino se queda mucho tiempo en el aire y entra en las " +
                "casas y en las vías respiratorias.",
            pista = "Cuando pasa un auto, ¿qué se levanta del montículo?",
            reglaId = "R_AIR_03", retoId = "RETO_POLVO", xp = 20
        ),
        sit(
            id = "S_CIU_04", zona = ZonaId.CIUDAD,
            nombre = "Basura acumulada junto al paradero",
            icono = IconoAmb.BOLSA_BASURA, x = 0.18f, y = 0.80f,
            categoria = Categoria.RESIDUOS, gravedad = Gravedad.NO_CONFORMIDAD,
            observacion = "Un montón crece en la esquina, sin ningún contenedor cerca.",
            explicacion = "Los puntos críticos de basura atraen plagas, generan malos " +
                "olores y bloquean el paso de las personas.",
            pista = "¿Ves algún tacho en toda la cuadra?",
            reglaId = "R_RES_02", retoId = "RETO_PATIO_LIMPIO", xp = 24
        ),
        sit(
            id = "S_CIU_05", zona = ZonaId.CIUDAD,
            nombre = "Trayectos muy cortos siempre en auto",
            icono = IconoAmb.HUELLA, x = 0.44f, y = 0.36f,
            categoria = Categoria.CONTAMINACION, gravedad = Gravedad.OBSERVACION,
            observacion = "Autos haciendo tres cuadras con una sola persona dentro.",
            explicacion = "El motor frío de los primeros minutos es el que más contamina " +
                "por cada kilómetro recorrido.",
            pista = "Cuenta cuántas personas van en cada auto.",
            reglaId = "R_CON_04", retoId = "RETO_TRANSPORTE", xp = 22, nivelMinimo = 3
        ),
        sit(
            id = "S_CIU_06", zona = ZonaId.CIUDAD,
            nombre = "Ciclovía y estación de bicicletas en uso",
            icono = IconoAmb.HUELLA, x = 0.66f, y = 0.26f,
            categoria = Categoria.CONTAMINACION, gravedad = Gravedad.CONFORME,
            observacion = "Carril señalizado, separado del tráfico y con gente usándolo.",
            explicacion = "Cada viaje en bicicleta evita humo, ruido y congestión.",
            pista = "Aquí la ciudad hizo las cosas bien.",
            reglaId = "R_CON_04", xp = 12
        ),
        sit(
            id = "S_CIU_07", zona = ZonaId.CIUDAD,
            nombre = "Alumbrado público con sensores",
            icono = IconoAmb.BOMBILLA, x = 0.88f, y = 0.34f,
            categoria = Categoria.ENERGIA, gravedad = Gravedad.CONFORME,
            observacion = "Luminarias LED apagadas de día y con sensor de presencia.",
            explicacion = "El alumbrado eficiente ilumina lo necesario y ahorra mucha energía.",
            pista = "Mira si las luces de la calle están encendidas de día.",
            reglaId = "R_ENE_01", xp = 12
        ),

        // ======================= ZONA INDUSTRIAL =======================
        sit(
            id = "S_IND_01", zona = ZonaId.ZONA_INDUSTRIAL,
            nombre = "Chimenea con humo denso",
            icono = IconoAmb.CHIMENEA, x = 0.30f, y = 0.26f,
            categoria = Categoria.AIRE, gravedad = Gravedad.NO_CONFORMIDAD,
            observacion = "Una columna oscura sale sin parar y se ve desde lejos.",
            explicacion = "Sin filtros, las partículas de la combustión llegan a los barrios " +
                "vecinos y se quedan en el aire que respiran.",
            pista = "Levanta la vista hacia lo más alto de la planta.",
            reglaId = "R_AIR_02", retoId = "RETO_HUMO", xp = 30, nivelMinimo = 3
        ),
        sit(
            id = "S_IND_02", zona = ZonaId.ZONA_INDUSTRIAL,
            nombre = "Bidón oxidado goteando aceite",
            icono = IconoAmb.TUBERIA, x = 0.58f, y = 0.74f,
            categoria = Categoria.CONTAMINACION, gravedad = Gravedad.NO_CONFORMIDAD,
            observacion = "Una mancha oscura se extiende en la tierra bajo el bidón.",
            explicacion = "Un litro de aceite puede contaminar muchísima agua subterránea; " +
                "una vez en el suelo es carísimo de recuperar.",
            pista = "Mira el suelo alrededor de los bidones del patio.",
            reglaId = "R_CON_03", retoId = "RETO_VERTIMIENTO", xp = 32, nivelMinimo = 3
        ),
        sit(
            id = "S_IND_03", zona = ZonaId.ZONA_INDUSTRIAL,
            nombre = "Residuos peligrosos mezclados con los comunes",
            icono = IconoAmb.PILA, x = 0.20f, y = 0.66f,
            categoria = Categoria.CONTAMINACION, gravedad = Gravedad.NO_CONFORMIDAD,
            observacion = "Envases de químicos y trapos con solvente en el tacho general.",
            explicacion = "Mezclarlos convierte todo el contenedor en residuo peligroso y " +
                "pone en riesgo a quien lo manipula.",
            pista = "Lee las etiquetas de los envases del contenedor.",
            reglaId = "R_CON_01", retoId = "RETO_PELIGROSOS", xp = 30, nivelMinimo = 2
        ),
        sit(
            id = "S_IND_04", zona = ZonaId.ZONA_INDUSTRIAL,
            nombre = "Maquinaria ruidosa junto a las viviendas",
            icono = IconoAmb.ALTAVOZ, x = 0.74f, y = 0.48f,
            categoria = Categoria.RUIDO, gravedad = Gravedad.NO_CONFORMIDAD,
            observacion = "Un compresor funciona a pocos metros de las casas vecinas.",
            explicacion = "El ruido industrial constante impide descansar y afecta la salud " +
                "de quienes viven al lado.",
            pista = "Fíjate qué hay justo detrás de la reja de la planta.",
            reglaId = "R_RUI_01", retoId = "RETO_SILENCIO", xp = 26
        ),
        sit(
            id = "S_IND_05", zona = ZonaId.ZONA_INDUSTRIAL,
            nombre = "Obra en marcha sin revisar el estudio ambiental",
            icono = IconoAmb.CASCO, x = 0.44f, y = 0.54f,
            categoria = Categoria.CONTAMINACION, gravedad = Gravedad.NO_CONFORMIDAD,
            observacion = "La nueva línea ya opera y nadie sabe adónde irán sus desechos.",
            explicacion = "Estudiar antes los efectos permite corregir el plan; después, " +
                "muchos daños ya no tienen arreglo.",
            pista = "Pregunta por los papeles antes de mirar las máquinas.",
            reglaId = "R_CON_02", retoId = "RETO_ESTUDIO", xp = 34, nivelMinimo = 3
        ),
        sit(
            id = "S_IND_06", zona = ZonaId.ZONA_INDUSTRIAL,
            nombre = "Paneles solares en el almacén",
            icono = IconoAmb.PANEL_SOLAR, x = 0.86f, y = 0.28f,
            categoria = Categoria.ENERGIA, gravedad = Gravedad.CONFORME,
            observacion = "El techo del almacén está cubierto de paneles en funcionamiento.",
            explicacion = "La energía solar no genera humo ni ruido mientras produce.",
            pista = "Mira el techo del almacén antes de juzgar toda la planta.",
            reglaId = "R_ENE_01", xp = 12
        ),
        sit(
            id = "S_IND_07", zona = ZonaId.ZONA_INDUSTRIAL,
            nombre = "Área de contención para líquidos",
            icono = IconoAmb.ESCUDO, x = 0.12f, y = 0.42f,
            categoria = Categoria.CONTAMINACION, gravedad = Gravedad.CONFORME,
            observacion = "Los tanques están sobre una losa con bordes y canaleta.",
            explicacion = "Si algo se derrama, queda retenido y no llega al suelo ni al río.",
            pista = "Observa qué hay debajo de los tanques.",
            reglaId = "R_CON_03", xp = 12
        )
    )

    val porId: Map<String, Situacion> = situaciones.associateBy { it.id }

    fun deZona(zona: ZonaId): List<Situacion> = situaciones.filter { it.zona == zona }

    fun situacion(id: String): Situacion? = porId[id]

    // ============================ MISIONES ============================

    val misiones: List<Mision> = listOf(
        Mision(
            id = "M_CASA",
            zona = ZonaId.CASA,
            titulo = "Misión: Revisar la casa",
            briefingEco = "¡Guardianes! Empezamos donde todo comienza: tu propia casa. " +
                "Observa con calma antes de decidir.",
            objetivo = "Encuentra al menos 3 situaciones que no cumplen una regla ambiental.",
            situacionesIds = deZona(ZonaId.CASA).map { it.id },
            minimoHallazgos = 3,
            nivel = 1
        ),
        Mision(
            id = "M_ESCUELA",
            zona = ZonaId.ESCUELA,
            titulo = "Misión: Auditar la escuela",
            briefingEco = "El colegio gasta agua, luz y papel todos los días. " +
                "Vamos a ver cómo lo está haciendo.",
            objetivo = "Detecta 3 problemas y propón una acción correctiva para cada uno.",
            situacionesIds = deZona(ZonaId.ESCUELA).map { it.id },
            minimoHallazgos = 3,
            nivel = 1
        ),
        Mision(
            id = "M_PARQUE",
            zona = ZonaId.PARQUE,
            titulo = "Misión: Proteger el parque",
            briefingEco = "El parque es el pulmón del barrio. Cuidado: aquí hay cosas bien " +
                "hechas que no debes marcar como problema.",
            objetivo = "Detecta 4 problemas sin equivocarte con lo que está correcto.",
            situacionesIds = deZona(ZonaId.PARQUE).map { it.id },
            minimoHallazgos = 4,
            nivel = 2
        ),
        Mision(
            id = "M_RIO",
            zona = ZonaId.RIO,
            titulo = "Misión: Salvar el río",
            briefingEco = "El río lleva el agua de todos. Sigue las señales: el color, el " +
                "olor y los animales te dirán mucho.",
            objetivo = "Detecta 4 problemas y relaciona cada uno con su regla ambiental.",
            situacionesIds = deZona(ZonaId.RIO).map { it.id },
            minimoHallazgos = 4,
            nivel = 2
        ),
        Mision(
            id = "M_CIUDAD",
            zona = ZonaId.CIUDAD,
            titulo = "Misión: Auditar la ciudad",
            briefingEco = "Miles de personas comparten este aire. Busca lo que lo ensucia " +
                "y también lo que lo mejora.",
            objetivo = "Detecta 4 problemas y acierta 3 acciones correctivas.",
            situacionesIds = deZona(ZonaId.CIUDAD).map { it.id },
            minimoHallazgos = 4,
            nivel = 3
        ),
        Mision(
            id = "M_INDUSTRIAL",
            zona = ZonaId.ZONA_INDUSTRIAL,
            titulo = "Misión: Inspección industrial",
            briefingEco = "La auditoría más difícil. Aquí no todo es malo: hay medidas bien " +
                "aplicadas junto a fallas graves. Sé justo y preciso.",
            objetivo = "Detecta 4 problemas, clasifícalos y propón la acción correcta.",
            situacionesIds = deZona(ZonaId.ZONA_INDUSTRIAL).map { it.id },
            minimoHallazgos = 4,
            nivel = 3
        )
    )

    val misionesPorZona: Map<ZonaId, Mision> = misiones.associateBy { it.zona }

    fun misionDe(zona: ZonaId): Mision =
        misionesPorZona[zona] ?: error("Falta la misión de la zona " + zona.name)

    fun mision(id: String): Mision? = misiones.firstOrNull { it.id == id }
}
