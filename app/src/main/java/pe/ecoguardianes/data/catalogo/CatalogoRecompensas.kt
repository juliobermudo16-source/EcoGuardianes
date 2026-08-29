package pe.ecoguardianes.data.catalogo

import pe.ecoguardianes.domain.model.Avatar
import pe.ecoguardianes.domain.model.Categoria
import pe.ecoguardianes.domain.model.Coleccionable
import pe.ecoguardianes.domain.model.IconoAmb
import pe.ecoguardianes.domain.model.Insignia
import pe.ecoguardianes.domain.model.Medida
import pe.ecoguardianes.domain.model.Requisito
import pe.ecoguardianes.domain.model.TipoColeccionable
import pe.ecoguardianes.domain.model.ZonaId

/** Insignias de EcoGuardianes. Todas se ganan con progreso real y medible. */
object CatalogoInsignias {

    val insignias: List<Insignia> = listOf(
        Insignia(
            id = "I_NATURALEZA",
            nombre = "Guardián de la Naturaleza",
            simbolo = "🌱",
            icono = IconoAmb.CESPED,
            colorHex = 0xFF4CAF50,
            descripcion = "Has cuidado los espacios verdes una y otra vez.",
            requisito = Requisito(Medida.HALLAZGOS_CATEGORIA, 6, Categoria.AREAS_VERDES)
        ),
        Insignia(
            id = "I_AGUA",
            nombre = "Protector del Agua",
            simbolo = "💧",
            icono = IconoAmb.GOTA,
            colorHex = 0xFF1E88C7,
            descripcion = "Detectaste fugas, derroches y aguas contaminadas.",
            requisito = Requisito(Medida.HALLAZGOS_CATEGORIA, 6, Categoria.AGUA)
        ),
        Insignia(
            id = "I_RESIDUOS",
            nombre = "Maestro de los Residuos",
            simbolo = "♻️",
            icono = IconoAmb.CONTENEDOR,
            colorHex = 0xFF2E9E5B,
            descripcion = "Ya sabes distinguir lo reciclable de lo que no lo es.",
            requisito = Requisito(Medida.HALLAZGOS_CATEGORIA, 8, Categoria.RESIDUOS)
        ),
        Insignia(
            id = "I_BOSQUES",
            nombre = "Defensor de los Bosques",
            simbolo = "🌳",
            icono = IconoAmb.ARBOL,
            colorHex = 0xFF2F7A3E,
            descripcion = "Dominaste la auditoría del parque.",
            requisito = Requisito(Medida.ESTRELLAS_ZONA, 2, zona = ZonaId.PARQUE)
        ),
        Insignia(
            id = "I_BIODIVERSIDAD",
            nombre = "Amigo de la Biodiversidad",
            simbolo = "🐾",
            icono = IconoAmb.PAJARO,
            colorHex = 0xFFE08A2E,
            descripcion = "Aprendiste a observar sin molestar a los animales.",
            requisito = Requisito(Medida.HALLAZGOS_CATEGORIA, 5, Categoria.BIODIVERSIDAD)
        ),
        Insignia(
            id = "I_ENERGIA",
            nombre = "Guardián de la Energía",
            simbolo = "⚡",
            icono = IconoAmb.BOMBILLA,
            colorHex = 0xFFF2B705,
            descripcion = "Apagaste el derroche allí donde lo encontraste.",
            requisito = Requisito(Medida.HALLAZGOS_CATEGORIA, 5, Categoria.ENERGIA)
        ),
        Insignia(
            id = "I_DETECTIVE",
            nombre = "Súper Detective",
            simbolo = "🔍",
            icono = IconoAmb.LUPA,
            colorHex = 0xFF8E5FD9,
            descripcion = "Encontraste todos los problemas de una zona, sin fallar ni uno.",
            requisito = Requisito(Medida.DETECCION_PERFECTA, 1)
        ),
        Insignia(
            id = "I_SUPREMO",
            nombre = "EcoGuardián Supremo",
            simbolo = "🌎",
            icono = IconoAmb.ESCUDO,
            colorHex = 0xFF0F6E8C,
            descripcion = "Completaste las seis zonas del mapa.",
            requisito = Requisito(Medida.ZONAS_COMPLETADAS, 6)
        ),
        Insignia(
            id = "I_IMPECABLE",
            nombre = "Auditoría Impecable",
            simbolo = "🏅",
            icono = IconoAmb.MEDALLA,
            colorHex = 0xFFD4A017,
            descripcion = "Alcanzaste 95% o más en una auditoría.",
            requisito = Requisito(Medida.PUNTAJE_MAXIMO, 95)
        ),
        Insignia(
            id = "I_MANOS_OBRA",
            nombre = "Manos a la Obra",
            simbolo = "🛠️",
            icono = IconoAmb.MOCHILA,
            colorHex = 0xFFB25A1F,
            descripcion = "Propusiste 20 acciones correctivas acertadas.",
            requisito = Requisito(Medida.ACCIONES_CORRECTAS, 20)
        ),
        Insignia(
            id = "I_CONSTANTE",
            nombre = "Guardián Constante",
            simbolo = "📋",
            icono = IconoAmb.PORTAPAPELES,
            colorHex = 0xFF3F6D8C,
            descripcion = "Completaste 8 auditorías ambientales.",
            requisito = Requisito(Medida.AUDITORIAS, 8)
        ),
        Insignia(
            id = "I_AIRE_LIMPIO",
            nombre = "Centinela del Aire",
            simbolo = "🌬️",
            icono = IconoAmb.HUMO,
            colorHex = 0xFF6FB3D9,
            descripcion = "Detectaste humos, quemas y polvo en varias zonas.",
            requisito = Requisito(Medida.HALLAZGOS_CATEGORIA, 4, Categoria.AIRE)
        )
    )

    val porId: Map<String, Insignia> = insignias.associateBy { it.id }

    fun insignia(id: String): Insignia? = porId[id]
}

/** Colección ambiental: se desbloquea únicamente con progreso real. */
object CatalogoColeccion {

    val coleccionables: List<Coleccionable> = listOf(
        // ---- Fauna ----
        Coleccionable(
            "C_PICAFLOR", "Picaflor", TipoColeccionable.FAUNA, IconoAmb.PAJARO, 0xFF2E9E5B,
            "Ave pequeñísima que puede quedarse quieta en el aire.",
            "Bate las alas tantas veces por segundo que casi no se ven.",
            Requisito(Medida.HALLAZGOS_CATEGORIA, 2, Categoria.BIODIVERSIDAD)
        ),
        Coleccionable(
            "C_RANA", "Rana del río", TipoColeccionable.FAUNA, IconoAmb.RANA, 0xFF4CAF50,
            "Vive entre el agua y la tierra.",
            "Respira en parte por la piel: por eso el agua sucia le afecta tanto.",
            Requisito(Medida.HALLAZGOS_CATEGORIA, 4, Categoria.BIODIVERSIDAD)
        ),
        Coleccionable(
            "C_PEZ", "Pez del cauce", TipoColeccionable.FAUNA, IconoAmb.PEZ, 0xFF1E88C7,
            "Indicador de que el río está sano.",
            "Si los peces se van de un tramo, algo cambió en el agua.",
            Requisito(Medida.ESTRELLAS_ZONA, 1, zona = ZonaId.RIO)
        ),
        Coleccionable(
            "C_MARIPOSA", "Mariposa", TipoColeccionable.FAUNA, IconoAmb.MARIPOSA, 0xFFE08A2E,
            "Poliniza flores mientras se alimenta.",
            "Muchas especies solo viven donde hay plantas nativas.",
            Requisito(Medida.HALLAZGOS_CATEGORIA, 3, Categoria.AREAS_VERDES)
        ),
        Coleccionable(
            "C_ABEJA", "Abeja", TipoColeccionable.FAUNA, IconoAmb.ABEJA, 0xFFF2B705,
            "Trabajadora incansable de la polinización.",
            "Buena parte de lo que comemos depende de insectos como ella.",
            Requisito(Medida.HALLAZGOS_TOTAL, 12)
        ),
        Coleccionable(
            "C_NIDO", "Nido protegido", TipoColeccionable.FAUNA, IconoAmb.NIDO, 0xFF8E5FD9,
            "Un hogar señalizado para que nadie lo moleste.",
            "Un nido abandonado por el ruido casi nunca se recupera.",
            Requisito(Medida.ESTRELLAS_ZONA, 2, zona = ZonaId.PARQUE)
        ),

        // ---- Flora ----
        Coleccionable(
            "C_MOLLE", "Molle serrano", TipoColeccionable.FLORA, IconoAmb.ARBOL, 0xFF2F7A3E,
            "Árbol resistente muy común en plazas del Perú.",
            "Aguanta la sequía mejor que muchos árboles importados.",
            Requisito(Medida.HALLAZGOS_CATEGORIA, 2, Categoria.AREAS_VERDES)
        ),
        Coleccionable(
            "C_TOTORA", "Totora", TipoColeccionable.FLORA, IconoAmb.CESPED, 0xFF4CAF50,
            "Planta de orilla que ayuda a limpiar el agua.",
            "Con ella se construyen balsas y esteras desde hace siglos.",
            Requisito(Medida.HALLAZGOS_CATEGORIA, 3, Categoria.AGUA)
        ),
        Coleccionable(
            "C_CANTUTA", "Cantuta", TipoColeccionable.FLORA, IconoAmb.FLOR, 0xFFD1495B,
            "Flor emblemática del Perú.",
            "Sus colores atraen justamente a los picaflores.",
            Requisito(Medida.AUDITORIAS, 3)
        ),
        Coleccionable(
            "C_HUERTO", "Biohuerto escolar", TipoColeccionable.FLORA, IconoAmb.ORGANICO, 0xFF6B4A2F,
            "Camas de cultivo que aprovechan el compost del colegio.",
            "Los restos de comida se convierten en abono en pocas semanas.",
            Requisito(Medida.ESTRELLAS_ZONA, 1, zona = ZonaId.ESCUELA)
        ),

        // ---- Reciclables ----
        Coleccionable(
            "C_BOTELLA_PET", "Botella PET", TipoColeccionable.RECICLABLE, IconoAmb.BOTELLA, 0xFF1E88C7,
            "Plástico transparente muy fácil de reciclar si va limpio.",
            "Con varias botellas recicladas se fabrica tela para polos.",
            Requisito(Medida.HALLAZGOS_CATEGORIA, 2, Categoria.RESIDUOS)
        ),
        Coleccionable(
            "C_PAPEL", "Papel limpio", TipoColeccionable.RECICLABLE, IconoAmb.PAPEL, 0xFFE0C9A6,
            "Se recicla varias veces si está seco.",
            "Mojado o con grasa, ya no sirve para reciclar.",
            Requisito(Medida.HALLAZGOS_CATEGORIA, 4, Categoria.RESIDUOS)
        ),
        Coleccionable(
            "C_LATA", "Lata de aluminio", TipoColeccionable.RECICLABLE, IconoAmb.LATA, 0xFF9AA5AC,
            "Se puede reciclar una y otra vez sin perder calidad.",
            "Reciclarla ahorra muchísima energía frente a fabricarla nueva.",
            Requisito(Medida.HALLAZGOS_CATEGORIA, 6, Categoria.RESIDUOS)
        ),
        Coleccionable(
            "C_VIDRIO", "Vidrio", TipoColeccionable.RECICLABLE, IconoAmb.VIDRIO, 0xFF6FB3D9,
            "Se recicla completo y sin residuos.",
            "Un frasco de vidrio puede volver a ser frasco infinitas veces.",
            Requisito(Medida.ACCIONES_CORRECTAS, 8)
        ),
        Coleccionable(
            "C_PILA", "Pila para acopio", TipoColeccionable.RECICLABLE, IconoAmb.PILA, 0xFFD1495B,
            "Residuo peligroso que necesita un punto especial.",
            "Nunca va al tacho común ni se entierra.",
            Requisito(Medida.HALLAZGOS_CATEGORIA, 3, Categoria.CONTAMINACION)
        ),

        // ---- Herramientas de auditor ----
        Coleccionable(
            "C_LUPA", "Lupa del guardián", TipoColeccionable.HERRAMIENTA, IconoAmb.LUPA, 0xFF8E5FD9,
            "Para mirar dos veces antes de decidir.",
            "Un buen auditor observa antes de opinar.",
            Requisito(Medida.HALLAZGOS_TOTAL, 3)
        ),
        Coleccionable(
            "C_PORTAPAPELES", "Ficha de auditoría", TipoColeccionable.HERRAMIENTA, IconoAmb.PORTAPAPELES, 0xFF3F6D8C,
            "Donde se registran los hallazgos.",
            "Lo que no se anota, se olvida.",
            Requisito(Medida.AUDITORIAS, 1)
        ),
        Coleccionable(
            "C_CASCO", "Casco de inspección", TipoColeccionable.HERRAMIENTA, IconoAmb.CASCO, 0xFFF2B705,
            "Seguridad primero en zonas industriales.",
            "Antes de auditar, hay que estar protegido.",
            Requisito(Medida.ESTRELLAS_ZONA, 1, zona = ZonaId.ZONA_INDUSTRIAL)
        ),
        Coleccionable(
            "C_CUADERNO", "Cuaderno de campo", TipoColeccionable.HERRAMIENTA, IconoAmb.CUADERNO, 0xFF6B4A2F,
            "Para apuntar lugar, hora y detalles.",
            "Las notas de campo son la memoria de la auditoría.",
            Requisito(Medida.AUDITORIAS, 5)
        ),
        Coleccionable(
            "C_MOCHILA", "Mochila de campo", TipoColeccionable.HERRAMIENTA, IconoAmb.MOCHILA, 0xFF2F7A3E,
            "Guantes, bolsas y todo lo necesario.",
            "Nunca se recoge un residuo peligroso con las manos.",
            Requisito(Medida.ACCIONES_CORRECTAS, 12)
        ),

        // ---- Descubrimientos ----
        Coleccionable(
            "C_CICLO_AGUA", "Ciclo del agua", TipoColeccionable.DESCUBRIMIENTO, IconoAmb.GOTA, 0xFF1E88C7,
            "El agua siempre está de viaje.",
            "La misma agua que bebes hoy ya existía hace millones de años.",
            Requisito(Medida.HALLAZGOS_CATEGORIA, 5, Categoria.AGUA)
        ),
        Coleccionable(
            "C_COMPOST", "Compost", TipoColeccionable.DESCUBRIMIENTO, IconoAmb.ORGANICO, 0xFF6B4A2F,
            "Los restos de comida se vuelven abono.",
            "Casi la mitad de la basura de una casa es orgánica.",
            Requisito(Medida.ACCIONES_CORRECTAS, 5)
        ),
        Coleccionable(
            "C_SOLAR", "Energía solar", TipoColeccionable.DESCUBRIMIENTO, IconoAmb.PANEL_SOLAR, 0xFFF2B705,
            "Electricidad directamente del sol.",
            "En un solo día el sol envía a la Tierra muchísima más energía de la que usamos.",
            Requisito(Medida.HALLAZGOS_CATEGORIA, 4, Categoria.ENERGIA)
        ),
        Coleccionable(
            "C_SILENCIO", "Mapa de ruido", TipoColeccionable.DESCUBRIMIENTO, IconoAmb.SILENCIO, 0xFF8E5FD9,
            "El ruido también se puede medir y dibujar.",
            "Existen mapas de ruido de ciudades enteras.",
            Requisito(Medida.HALLAZGOS_CATEGORIA, 3, Categoria.RUIDO)
        ),
        Coleccionable(
            "C_HUELLA", "Huella ambiental", TipoColeccionable.DESCUBRIMIENTO, IconoAmb.HUELLA, 0xFF0F6E8C,
            "La marca que dejan nuestras decisiones.",
            "Cada elección diaria hace la huella más grande o más pequeña.",
            Requisito(Medida.XP_TOTAL, 800)
        ),
        Coleccionable(
            "C_VITRINA", "Vitrina del guardián", TipoColeccionable.DESCUBRIMIENTO, IconoAmb.MEDALLA, 0xFFD4A017,
            "El estante donde se exhibe la colección.",
            "Se abre cuando ya has reunido buena parte de la colección.",
            Requisito(Medida.COLECCIONABLES, 15)
        )
    )

    val porId: Map<String, Coleccionable> = coleccionables.associateBy { it.id }

    fun coleccionable(id: String): Coleccionable? = porId[id]

    fun porTipo(tipo: TipoColeccionable): List<Coleccionable> =
        coleccionables.filter { it.tipo == tipo }
}

/** Avatares locales. No se pide ningún dato personal para elegirlos. */
object CatalogoAvatares {

    val avatares: List<Avatar> = listOf(
        Avatar("AV_HOJA", "Brote", 0xFF4CAF50, 0xFFEFF7E6, IconoAmb.CESPED),
        Avatar("AV_GOTA", "Gota", 0xFF1E88C7, 0xFFE3F2FB, IconoAmb.GOTA),
        Avatar("AV_SOL", "Rayo", 0xFFF2B705, 0xFFFFF6DA, IconoAmb.BOMBILLA),
        Avatar("AV_AVE", "Vuelo", 0xFFE08A2E, 0xFFFDF0E1, IconoAmb.PAJARO),
        Avatar("AV_PEZ", "Aleta", 0xFF0F6E8C, 0xFFDDF0F4, IconoAmb.PEZ),
        Avatar("AV_ARBOL", "Raíz", 0xFF2F7A3E, 0xFFE6F3E9, IconoAmb.ARBOL),
        Avatar("AV_LUPA", "Detective", 0xFF8E5FD9, 0xFFF0E9FB, IconoAmb.LUPA),
        Avatar("AV_ESCUDO", "Escudo", 0xFFD1495B, 0xFFFCE7EA, IconoAmb.ESCUDO)
    )

    val porId: Map<String, Avatar> = avatares.associateBy { it.id }

    val predeterminado: Avatar = avatares.first()

    fun avatar(id: String?): Avatar = porId[id] ?: predeterminado
}
