package pe.ecoguardianes.domain.model

/**
 * Catálogo de ilustraciones vectoriales dibujadas con Compose Canvas.
 * Se usa un identificador de dominio para que el contenido educativo no
 * dependa de recursos de Android y pueda probarse sin la interfaz.
 */
enum class IconoAmb {
    // Residuos
    BOLSA_BASURA, CONTENEDOR, BOTELLA, PAPEL, LATA, ORGANICO, PILA, VIDRIO,

    // Agua
    GRIFO, GOTA, CHARCO, TUBERIA, MANGUERA,

    // Aire
    HUMO, CHIMENEA, AUTO, POLVO,

    // Ruido
    ALTAVOZ, BOCINA, SILENCIO,

    // Areas verdes y biodiversidad
    ARBOL, ARBOL_SECO, FLOR, CESPED, PAJARO, PEZ, MARIPOSA, ABEJA, RANA, NIDO,

    // Energia
    BOMBILLA, ENCHUFE, PANEL_SOLAR, VENTILADOR, PANTALLA,

    // Auditoria e interfaz
    LUPA, PORTAPAPELES, ESCUDO, ESTRELLA, MAPA, LIBRO, MEDALLA, MOCHILA,
    CASCO, CUADERNO, HUELLA, RELOJ
}
