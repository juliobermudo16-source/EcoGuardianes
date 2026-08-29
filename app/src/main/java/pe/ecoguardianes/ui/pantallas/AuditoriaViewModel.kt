package pe.ecoguardianes.ui.pantallas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.ecoguardianes.data.catalogo.CatalogoAcciones
import pe.ecoguardianes.data.catalogo.CatalogoEscenarios
import pe.ecoguardianes.data.catalogo.CatalogoReglas
import pe.ecoguardianes.data.catalogo.CatalogoRetos
import pe.ecoguardianes.data.repo.EcoRepositorio
import pe.ecoguardianes.data.repo.ResumenGuardado
import pe.ecoguardianes.domain.audit.MarcaJugador
import pe.ecoguardianes.domain.audit.MotorAuditoria
import pe.ecoguardianes.domain.audit.ResultadoAuditoria
import pe.ecoguardianes.domain.model.AccionCorrectiva
import pe.ecoguardianes.domain.model.Categoria
import pe.ecoguardianes.domain.model.Gravedad
import pe.ecoguardianes.domain.model.Mision
import pe.ecoguardianes.domain.model.ReglaAmbiental
import pe.ecoguardianes.domain.model.Reto
import pe.ecoguardianes.domain.model.Situacion
import pe.ecoguardianes.domain.model.ZonaId
import pe.ecoguardianes.ui.art.AnimoEco

/** Momento del ciclo de auditoría en el que se encuentra el jugador. */
enum class FaseAuditoria {
    EXPLORAR,
    INSPECCION,
    FALSO_POSITIVO,
    CLASIFICAR,
    REGLA,
    ACCION,
    RETO,
    RESUELTO,
    CIERRE
}

/** Mensaje de feedback educativo: nunca es un simple "correcto" o "incorrecto". */
data class FeedbackEco(
    val acierto: Boolean,
    val titulo: String,
    val texto: String,
    val pista: String? = null
)

data class EstadoAuditoria(
    val cargando: Boolean = true,
    val zona: ZonaId = ZonaId.CASA,
    val mision: Mision? = null,
    val situaciones: List<Situacion> = emptyList(),
    val marcas: Map<String, MarcaJugador> = emptyMap(),
    val revisadas: Set<String> = emptySet(),
    val resueltas: Set<String> = emptySet(),
    val fase: FaseAuditoria = FaseAuditoria.EXPLORAR,
    val situacionId: String? = null,
    val mensajeEco: String = "",
    val animo: AnimoEco = AnimoEco.NORMAL,
    val pistaVisible: Boolean = false,
    val feedback: FeedbackEco? = null,
    val categoriaElegida: Categoria? = null,
    val gravedadElegida: Gravedad? = null,
    val accionElegidaId: String? = null,
    val intentos: Int = 0,
    val solucionReto: Map<String, String> = emptyMap(),
    val retoVerificado: Boolean? = null,
    val retiradasDisponibles: Int = RETIRADAS_INICIALES,
    val iniciadaEn: Long = 0L,
    val guardando: Boolean = false,
    val resumen: ResumenGuardado? = null,
    val auditoriaId: Long? = null
) {
    val situacion: Situacion?
        get() = situacionId?.let { id -> situaciones.firstOrNull { it.id == id } }

    val hallazgosRegistrados: Int
        get() = marcas.values.count { it.marcadaComoProblema }

    val situacionesRestantes: Int
        get() = situaciones.count { it.id !in revisadas }

    val puedeCerrar: Boolean
        get() = hallazgosRegistrados >= (mision?.minimoHallazgos ?: 0) || situacionesRestantes == 0

    val acciones: List<AccionCorrectiva>
        get() = situacionId?.let { EcoRepositorio.accionesBarajadas(it) } ?: emptyList()

    val regla: ReglaAmbiental?
        get() = situacion?.let { CatalogoReglas.regla(it.reglaId) }

    val reto: Reto?
        get() = CatalogoRetos.reto(situacion?.retoId)

    companion object {
        const val RETIRADAS_INICIALES = 1
    }
}

/**
 * Motor de la partida: enlaza el escenario, el motor de auditoría y la
 * persistencia. Todo lo que el jugador hace tiene consecuencias reales.
 */
class AuditoriaViewModel(
    private val repo: EcoRepositorio,
    private val ahora: () -> Long = { System.currentTimeMillis() }
) : ViewModel() {

    private val _estado = MutableStateFlow(EstadoAuditoria())
    val estado: StateFlow<EstadoAuditoria> = _estado.asStateFlow()

    fun cargar(zona: ZonaId) {
        if (_estado.value.mision?.zona == zona && !_estado.value.cargando) return
        val mision = CatalogoEscenarios.misionDe(zona)
        val situaciones = CatalogoEscenarios.deZona(zona)
        _estado.value = EstadoAuditoria(
            cargando = false,
            zona = zona,
            mision = mision,
            situaciones = situaciones,
            mensajeEco = mision.briefingEco,
            animo = AnimoEco.NORMAL,
            iniciadaEn = ahora()
        )
        viewModelScope.launch { repo.marcarZonaIniciada(zona) }
    }

    // ------------------------------------------------------------ Exploración

    fun tocarSituacion(id: String) {
        val situacion = _estado.value.situaciones.firstOrNull { it.id == id } ?: return
        if (id in _estado.value.resueltas) {
            _estado.update {
                it.copy(
                    mensajeEco = "Esta ya la resolviste. ¡Buen trabajo, guardián!",
                    animo = AnimoEco.FELIZ
                )
            }
            return
        }
        _estado.update {
            it.copy(
                fase = FaseAuditoria.INSPECCION,
                situacionId = id,
                pistaVisible = false,
                feedback = null,
                categoriaElegida = null,
                gravedadElegida = null,
                accionElegidaId = null,
                intentos = 0,
                solucionReto = emptyMap(),
                retoVerificado = null,
                mensajeEco = "Observa con calma: " + situacion.nombre.lowercase() + ".",
                animo = AnimoEco.PENSATIVO
            )
        }
    }

    fun cerrarPanel() {
        _estado.update {
            it.copy(
                fase = FaseAuditoria.EXPLORAR,
                situacionId = null,
                feedback = null,
                pistaVisible = false,
                animo = AnimoEco.NORMAL,
                mensajeEco = "Sigue explorando. Todavía hay cosas por revisar."
            )
        }
    }

    fun mostrarPista() {
        _estado.update { it.copy(pistaVisible = true, animo = AnimoEco.PENSATIVO) }
    }

    /** El jugador afirma que la situación está correcta. */
    fun marcarComoCorrecta() {
        val estado = _estado.value
        val situacion = estado.situacion ?: return
        if (!situacion.esProblema) {
            _estado.update {
                it.copy(
                    fase = FaseAuditoria.RESUELTO,
                    revisadas = it.revisadas + situacion.id,
                    resueltas = it.resueltas + situacion.id,
                    animo = AnimoEco.FELIZ,
                    feedback = FeedbackEco(
                        acierto = true,
                        titulo = "¡Bien visto!",
                        texto = situacion.explicacion
                    ),
                    mensajeEco = "Correcto: aquí sí se cumple la regla ambiental."
                )
            }
        } else {
            _estado.update {
                it.copy(
                    revisadas = it.revisadas + situacion.id,
                    pistaVisible = true,
                    animo = AnimoEco.PENSATIVO,
                    feedback = FeedbackEco(
                        acierto = false,
                        titulo = "Mmm... míralo otra vez",
                        texto = "Lo anoté como revisado, pero algo aquí no me convence. " +
                            "Puedes volver a este punto cuando quieras.",
                        pista = situacion.pista
                    ),
                    mensajeEco = "Usa la lupa: a veces el problema no salta a la vista."
                )
            }
        }
    }

    /** El jugador registra la situación como hallazgo. */
    fun registrarHallazgo() {
        val estado = _estado.value
        val situacion = estado.situacion ?: return
        if (situacion.esProblema) {
            _estado.update {
                it.copy(
                    fase = FaseAuditoria.CLASIFICAR,
                    revisadas = it.revisadas + situacion.id,
                    marcas = it.marcas + (situacion.id to MarcaJugador(situacion.id, true)),
                    animo = AnimoEco.ALERTA,
                    feedback = null,
                    mensajeEco = "¡Buen ojo! Ahora dime de qué tipo es este problema."
                )
            }
        } else {
            _estado.update {
                it.copy(
                    fase = FaseAuditoria.FALSO_POSITIVO,
                    revisadas = it.revisadas + situacion.id,
                    marcas = it.marcas + (situacion.id to MarcaJugador(situacion.id, true)),
                    animo = AnimoEco.PENSATIVO,
                    feedback = FeedbackEco(
                        acierto = false,
                        titulo = "Aquí no hay no conformidad",
                        texto = situacion.explicacion
                    ),
                    mensajeEco = "Anotaste algo que en realidad está bien hecho."
                )
            }
        }
    }

    /** Usa la única retirada disponible para corregir un hallazgo equivocado. */
    fun retirarHallazgo() {
        val estado = _estado.value
        val id = estado.situacionId ?: return
        if (estado.retiradasDisponibles <= 0) return
        _estado.update {
            it.copy(
                fase = FaseAuditoria.RESUELTO,
                marcas = it.marcas - id,
                resueltas = it.resueltas + id,
                retiradasDisponibles = it.retiradasDisponibles - 1,
                animo = AnimoEco.FELIZ,
                feedback = FeedbackEco(
                    acierto = true,
                    titulo = "Acta corregida",
                    texto = "Un buen auditor también sabe rectificar. Ya no cuenta como error."
                ),
                mensajeEco = "Retirado del acta. Te queda " + (it.retiradasDisponibles - 1) +
                    " corrección."
            )
        }
    }

    /** Mantiene el hallazgo equivocado: quedará registrado como falso positivo. */
    fun mantenerHallazgo() {
        _estado.update {
            it.copy(
                fase = FaseAuditoria.RESUELTO,
                resueltas = it.resueltas + (it.situacionId ?: ""),
                animo = AnimoEco.NORMAL,
                feedback = FeedbackEco(
                    acierto = false,
                    titulo = "Quedará en el acta",
                    texto = "Lo veremos al final, en la ficha de auditoría."
                ),
                mensajeEco = "Anotado. Seguimos."
            )
        }
    }

    // ---------------------------------------------------------- Clasificación

    fun elegirCategoria(categoria: Categoria) {
        _estado.update { it.copy(categoriaElegida = categoria) }
    }

    fun elegirGravedad(gravedad: Gravedad) {
        _estado.update { it.copy(gravedadElegida = gravedad) }
    }

    fun confirmarClasificacion() {
        val estado = _estado.value
        val situacion = estado.situacion ?: return
        val categoria = estado.categoriaElegida ?: return
        val gravedad = estado.gravedadElegida ?: return

        val categoriaOk = categoria == situacion.categoria
        val gravedadOk = gravedad == situacion.gravedad
        val intentos = estado.intentos + 1

        if (categoriaOk && gravedadOk) {
            _estado.update {
                it.copy(
                    fase = FaseAuditoria.REGLA,
                    intentos = intentos,
                    animo = AnimoEco.FELIZ,
                    marcas = it.marcas + (
                        situacion.id to (
                            it.marcas[situacion.id]
                                ?: MarcaJugador(situacion.id, true)
                            ).copy(
                            categoriaElegida = categoria,
                            gravedadElegida = gravedad,
                            intentos = intentos
                        )
                        ),
                    feedback = FeedbackEco(
                        acierto = true,
                        titulo = "Clasificación correcta",
                        texto = situacion.explicacion
                    ),
                    mensajeEco = "¡Exacto! Ahora veamos qué regla ambiental se relaciona."
                )
            }
            return
        }

        val revelar = intentos >= MAX_INTENTOS
        val detalle = buildString {
            if (!categoriaOk) append("La categoría todavía no encaja: piensa en qué recurso se está afectando. ")
            if (!gravedadOk) append("Revisa qué tan serio es lo que ves. ")
            if (revelar) {
                append("Te lo cuento: es un problema de ")
                append(situacion.categoria.etiqueta.lowercase())
                append(" y se clasifica como ")
                append(situacion.gravedad.etiqueta.lowercase())
                append(". ")
                append(situacion.explicacion)
            }
        }

        _estado.update {
            it.copy(
                fase = if (revelar) FaseAuditoria.REGLA else FaseAuditoria.CLASIFICAR,
                intentos = intentos,
                animo = AnimoEco.PENSATIVO,
                categoriaElegida = if (revelar) situacion.categoria else categoria,
                gravedadElegida = if (revelar) situacion.gravedad else gravedad,
                marcas = it.marcas + (
                    situacion.id to (
                        it.marcas[situacion.id] ?: MarcaJugador(situacion.id, true)
                        ).copy(
                        categoriaElegida = if (revelar) null else categoria,
                        gravedadElegida = if (revelar) null else gravedad,
                        intentos = intentos
                    )
                    ),
                feedback = FeedbackEco(
                    acierto = false,
                    titulo = if (revelar) "Lo vemos juntos" else "Casi, prueba otra vez",
                    texto = detalle.trim(),
                    pista = if (revelar) null else situacion.pista
                ),
                mensajeEco = if (revelar) {
                    "No pasa nada: lo importante es entenderlo."
                } else {
                    "Piénsalo un poco más. Tienes otro intento."
                }
            )
        }
    }

    fun continuarDesdeRegla() {
        _estado.update {
            it.copy(
                fase = FaseAuditoria.ACCION,
                feedback = null,
                intentos = 0,
                animo = AnimoEco.NORMAL,
                mensajeEco = "Ya sabemos la regla. ¿Qué acción arregla de verdad el problema?"
            )
        }
    }

    // ------------------------------------------------------ Acción correctiva

    fun elegirAccion(accionId: String) {
        val estado = _estado.value
        val situacion = estado.situacion ?: return
        val accion = CatalogoAcciones.accion(accionId) ?: return
        val intentos = estado.intentos + 1

        if (accion.esCorrecta) {
            val siguiente =
                if (situacion.retoId != null) FaseAuditoria.RETO else FaseAuditoria.RESUELTO
            _estado.update {
                it.copy(
                    fase = siguiente,
                    intentos = 0,
                    accionElegidaId = accionId,
                    animo = AnimoEco.FELIZ,
                    resueltas = if (siguiente == FaseAuditoria.RESUELTO) {
                        it.resueltas + situacion.id
                    } else {
                        it.resueltas
                    },
                    marcas = it.marcas + (
                        situacion.id to (
                            it.marcas[situacion.id] ?: MarcaJugador(situacion.id, true)
                            ).copy(
                            accionElegidaId = accionId,
                            accionAcertada = true
                        )
                        ),
                    feedback = FeedbackEco(
                        acierto = true,
                        titulo = "Buena acción correctiva",
                        texto = accion.explicacion
                    ),
                    mensajeEco = if (siguiente == FaseAuditoria.RETO) {
                        "¡Ahora hazlo de verdad!"
                    } else {
                        "Problema resuelto. Sigamos."
                    }
                )
            }
            return
        }

        val revelar = intentos >= MAX_INTENTOS
        val correcta = CatalogoAcciones.correctaDe(situacion.id)
        _estado.update {
            it.copy(
                fase = if (revelar && situacion.retoId != null) {
                    FaseAuditoria.RETO
                } else if (revelar) {
                    FaseAuditoria.RESUELTO
                } else {
                    FaseAuditoria.ACCION
                },
                intentos = if (revelar) 0 else intentos,
                accionElegidaId = if (revelar) correcta?.id else accionId,
                resueltas = if (revelar && situacion.retoId == null) {
                    it.resueltas + situacion.id
                } else {
                    it.resueltas
                },
                animo = AnimoEco.PENSATIVO,
                marcas = it.marcas + (
                    situacion.id to (
                        it.marcas[situacion.id] ?: MarcaJugador(situacion.id, true)
                        ).copy(
                        accionElegidaId = accionId,
                        accionAcertada = false
                    )
                    ),
                feedback = FeedbackEco(
                    acierto = false,
                    titulo = if (revelar) "Lo vemos juntos" else "Esa no soluciona el problema",
                    texto = if (revelar) {
                        accion.explicacion + " Lo que sí funciona es: " +
                            (correcta?.texto ?: "") + " " + (correcta?.explicacion ?: "")
                    } else {
                        accion.explicacion
                    }
                ),
                mensajeEco = if (revelar) {
                    "Recuérdalo para la próxima."
                } else {
                    "Prueba con otra. Piensa en la causa del problema."
                }
            )
        }
    }

    // ----------------------------------------------------------- Mini-retos

    fun colocarPieza(piezaId: String, destinoId: String) {
        _estado.update {
            it.copy(
                solucionReto = it.solucionReto + (piezaId to destinoId),
                retoVerificado = null
            )
        }
    }

    fun quitarPieza(piezaId: String) {
        _estado.update { it.copy(solucionReto = it.solucionReto - piezaId, retoVerificado = null) }
    }

    fun verificarReto() {
        val estado = _estado.value
        val reto = estado.reto ?: return
        val situacion = estado.situacion ?: return
        val correcto = reto.esSolucionCorrecta(estado.solucionReto)
        val aciertos = reto.aciertos(estado.solucionReto)

        if (correcto) {
            _estado.update {
                it.copy(
                    fase = FaseAuditoria.RESUELTO,
                    retoVerificado = true,
                    resueltas = it.resueltas + situacion.id,
                    animo = AnimoEco.CELEBRA,
                    marcas = it.marcas + (
                        situacion.id to (
                            it.marcas[situacion.id] ?: MarcaJugador(situacion.id, true)
                            ).copy(retoSuperado = true)
                        ),
                    feedback = FeedbackEco(
                        acierto = true,
                        titulo = "¡Arreglado!",
                        texto = "Acabas de aplicar la acción correctiva. Así se hace, guardián."
                    ),
                    mensajeEco = "¡Excelente trabajo de campo!"
                )
            }
        } else {
            _estado.update {
                it.copy(
                    retoVerificado = false,
                    animo = AnimoEco.PENSATIVO,
                    feedback = FeedbackEco(
                        acierto = false,
                        titulo = "Vas por buen camino",
                        texto = "Llevas " + aciertos + " de " + reto.piezas.size +
                            " en su lugar. Revisa las que faltan.",
                        pista = reto.ayuda
                    ),
                    mensajeEco = "Casi. Ajusta lo que falta y vuelve a intentarlo."
                )
            }
        }
    }

    fun terminarSituacion() {
        _estado.update {
            it.copy(
                fase = FaseAuditoria.EXPLORAR,
                situacionId = null,
                feedback = null,
                pistaVisible = false,
                animo = AnimoEco.NORMAL,
                mensajeEco = if (it.puedeCerrar) {
                    "Ya puedes cerrar el acta, o seguir revisando la zona."
                } else {
                    "Sigue explorando el escenario."
                }
            )
        }
    }

    // -------------------------------------------------------------- Cierre

    fun prepararCierre() {
        _estado.update { it.copy(fase = FaseAuditoria.CIERRE, animo = AnimoEco.NORMAL) }
    }

    fun volverAExplorar() {
        _estado.update { it.copy(fase = FaseAuditoria.EXPLORAR, situacionId = null) }
    }

    /** Calcula la ficha con el motor de auditoría y la guarda en la base de datos. */
    fun cerrarAuditoria(alTerminar: (Long) -> Unit) {
        val estado = _estado.value
        val mision = estado.mision ?: return
        if (estado.guardando) return
        _estado.update { it.copy(guardando = true) }

        viewModelScope.launch {
            val resultado: ResultadoAuditoria = MotorAuditoria.calcular(
                mision = mision,
                situaciones = estado.situaciones,
                marcas = estado.marcas.values.toList()
            )
            val resumen = repo.guardarAuditoria(
                mision = mision,
                situaciones = estado.situaciones,
                marcas = estado.marcas.values.toList(),
                resultado = resultado,
                iniciadaEn = estado.iniciadaEn
            )
            _estado.update {
                it.copy(guardando = false, resumen = resumen, auditoriaId = resumen.auditoriaId)
            }
            alTerminar(resumen.auditoriaId)
        }
    }

    fun reiniciarMision() {
        val zona = _estado.value.zona
        _estado.value = EstadoAuditoria(cargando = true)
        cargar(zona)
    }

    companion object {
        const val MAX_INTENTOS = 2
    }
}
