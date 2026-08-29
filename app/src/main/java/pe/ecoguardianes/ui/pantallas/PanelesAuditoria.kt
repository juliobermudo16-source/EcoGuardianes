package pe.ecoguardianes.ui.pantallas

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import pe.ecoguardianes.data.catalogo.CatalogoEscenarios
import pe.ecoguardianes.domain.model.Categoria
import pe.ecoguardianes.domain.model.Gravedad
import pe.ecoguardianes.domain.model.IconoAmb
import pe.ecoguardianes.domain.model.ReglaAmbiental
import pe.ecoguardianes.ui.art.AnimoEco
import pe.ecoguardianes.ui.art.EcoIcono
import pe.ecoguardianes.ui.componentes.BotonEco
import pe.ecoguardianes.ui.componentes.BotonEcoSuave
import pe.ecoguardianes.ui.componentes.BurbujaEco
import pe.ecoguardianes.ui.componentes.ChipCategoria
import pe.ecoguardianes.ui.componentes.ChipGravedad
import pe.ecoguardianes.ui.componentes.SeparadorEco
import pe.ecoguardianes.ui.componentes.TarjetaEco
import pe.ecoguardianes.ui.theme.EcoColores
import pe.ecoguardianes.ui.theme.aColor

/** Panel modal que cubre el escenario durante cada paso de la auditoría. */
@Composable
fun PanelAuditoria(
    vm: AuditoriaViewModel,
    estado: EstadoAuditoria,
    alResultado: (Long) -> Unit
) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color(0xCC0C1F1A)),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .heightIn(max = 640.dp)
                .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                .background(EcoColores.Crema)
                .padding(18.dp)
                .verticalScroll(rememberScrollState())
        ) {
            when (estado.fase) {
                FaseAuditoria.INSPECCION -> PanelInspeccion(vm, estado)
                FaseAuditoria.FALSO_POSITIVO -> PanelFalsoPositivo(vm, estado)
                FaseAuditoria.CLASIFICAR -> PanelClasificar(vm, estado)
                FaseAuditoria.REGLA -> PanelRegla(vm, estado)
                FaseAuditoria.ACCION -> PanelAccion(vm, estado)
                FaseAuditoria.RETO -> PanelReto(vm, estado)
                FaseAuditoria.RESUELTO -> PanelResuelto(vm, estado)
                FaseAuditoria.CIERRE -> PanelCierre(vm, estado, alResultado)
                FaseAuditoria.EXPLORAR -> Unit
            }
            Spacer(Modifier.height(6.dp))
        }
    }
}

@Composable
private fun CabeceraPaso(paso: String, titulo: String, icono: IconoAmb, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier.size(46.dp).clip(CircleShape).background(color.copy(alpha = 0.18f)),
            contentAlignment = Alignment.Center
        ) {
            EcoIcono(icono, tam = 30.dp)
        }
        Spacer(Modifier.width(10.dp))
        Column {
            Text(
                paso.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = color
            )
            Text(titulo, style = MaterialTheme.typography.headlineSmall)
        }
    }
    Spacer(Modifier.height(10.dp))
}

@Composable
private fun TarjetaFeedback(feedback: FeedbackEco) {
    val color = if (feedback.acierto) EcoColores.VerdeHoja else EcoColores.AmbarObservacion
    TarjetaEco(
        modifier = Modifier.fillMaxWidth(),
        color = color.copy(alpha = 0.12f),
        borde = color.copy(alpha = 0.55f)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                if (feedback.acierto) "✓" else "!",
                style = MaterialTheme.typography.headlineSmall,
                color = color
            )
            Spacer(Modifier.width(10.dp))
            Text(feedback.titulo, style = MaterialTheme.typography.titleMedium, color = color)
        }
        Spacer(Modifier.height(6.dp))
        Text(feedback.texto, style = MaterialTheme.typography.bodyMedium)
        if (feedback.pista != null) {
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.Top) {
                EcoIcono(IconoAmb.LUPA, tam = 22.dp)
                Spacer(Modifier.width(8.dp))
                Text(
                    feedback.pista,
                    style = MaterialTheme.typography.bodySmall,
                    color = EcoColores.MoradoLupa
                )
            }
        }
    }
}

// -------------------------------------------------------------- INSPECCIÓN

@Composable
private fun PanelInspeccion(vm: AuditoriaViewModel, estado: EstadoAuditoria) {
    val situacion = estado.situacion ?: return
    CabeceraPaso("Paso 1 · Observar", situacion.nombre, situacion.icono, EcoColores.AzulRio)

    TarjetaEco(modifier = Modifier.fillMaxWidth(), color = Color.White) {
        Text("Lo que ves", style = MaterialTheme.typography.titleSmall)
        Spacer(Modifier.height(4.dp))
        Text(situacion.observacion, style = MaterialTheme.typography.bodyLarge)
    }

    if (estado.pistaVisible) {
        Spacer(Modifier.height(10.dp))
        BurbujaEco(situacion.pista, animo = AnimoEco.PENSATIVO, tamMascota = 56.dp)
    }

    if (estado.feedback != null) {
        Spacer(Modifier.height(10.dp))
        TarjetaFeedback(estado.feedback)
    }

    Spacer(Modifier.height(14.dp))
    Text(
        "¿Qué anotas en tu ficha de auditoría?",
        style = MaterialTheme.typography.titleMedium
    )
    Spacer(Modifier.height(10.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        BotonEco(
            texto = "Está correcto",
            icono = IconoAmb.ESCUDO,
            colorFondo = EcoColores.VerdeHoja,
            modifier = Modifier.weight(1f),
            onClick = { vm.marcarComoCorrecta() }
        )
        BotonEco(
            texto = "Registrar hallazgo",
            icono = IconoAmb.PORTAPAPELES,
            colorFondo = EcoColores.CoralAlerta,
            modifier = Modifier.weight(1f),
            onClick = { vm.registrarHallazgo() }
        )
    }
    Spacer(Modifier.height(10.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        if (!estado.pistaVisible) {
            BotonEcoSuave(
                texto = "Pedir pista a ECO",
                icono = IconoAmb.LUPA,
                color = EcoColores.MoradoLupa,
                modifier = Modifier.weight(1f),
                onClick = { vm.mostrarPista() }
            )
        }
        BotonEcoSuave(
            texto = "Seguir explorando",
            color = EcoColores.CarbonSuave,
            modifier = Modifier.weight(1f),
            onClick = { vm.cerrarPanel() }
        )
    }
}

// ---------------------------------------------------------- FALSO POSITIVO

@Composable
private fun PanelFalsoPositivo(vm: AuditoriaViewModel, estado: EstadoAuditoria) {
    val situacion = estado.situacion ?: return
    CabeceraPaso("Revisión", situacion.nombre, situacion.icono, EcoColores.AmbarObservacion)
    estado.feedback?.let { TarjetaFeedback(it) }

    Spacer(Modifier.height(12.dp))
    Text(
        "Un auditor puede corregir su acta. ¿Qué prefieres hacer?",
        style = MaterialTheme.typography.bodyMedium
    )
    Spacer(Modifier.height(12.dp))
    if (estado.retiradasDisponibles > 0) {
        BotonEco(
            texto = "Retirar del acta (te queda " + estado.retiradasDisponibles + ")",
            icono = IconoAmb.CUADERNO,
            colorFondo = EcoColores.VerdeHoja,
            modifier = Modifier.fillMaxWidth(),
            onClick = { vm.retirarHallazgo() }
        )
        Spacer(Modifier.height(8.dp))
    } else {
        Text(
            "Ya usaste tu corrección de esta misión.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(8.dp))
    }
    BotonEcoSuave(
        texto = "Mantener el hallazgo",
        color = EcoColores.CarbonSuave,
        modifier = Modifier.fillMaxWidth(),
        onClick = { vm.mantenerHallazgo() }
    )
}

// ------------------------------------------------------------ CLASIFICAR

@Composable
private fun PanelClasificar(vm: AuditoriaViewModel, estado: EstadoAuditoria) {
    val situacion = estado.situacion ?: return
    CabeceraPaso("Paso 2 · Clasificar", situacion.nombre, situacion.icono, EcoColores.MoradoLupa)

    Text("¿De qué tipo es el problema?", style = MaterialTheme.typography.titleMedium)
    Spacer(Modifier.height(8.dp))
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Categoria.entries.chunked(2).forEach { fila ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                fila.forEach { categoria ->
                    OpcionCategoria(
                        categoria = categoria,
                        seleccionada = estado.categoriaElegida == categoria,
                        modifier = Modifier.weight(1f),
                        onClick = { vm.elegirCategoria(categoria) }
                    )
                }
                if (fila.size == 1) Spacer(Modifier.weight(1f))
            }
        }
    }

    Spacer(Modifier.height(14.dp))
    Text("¿Qué tan serio es?", style = MaterialTheme.typography.titleMedium)
    Spacer(Modifier.height(8.dp))
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Gravedad.entries.forEach { gravedad ->
            OpcionGravedad(
                gravedad = gravedad,
                seleccionada = estado.gravedadElegida == gravedad,
                onClick = { vm.elegirGravedad(gravedad) }
            )
        }
    }

    if (estado.feedback != null) {
        Spacer(Modifier.height(12.dp))
        TarjetaFeedback(estado.feedback)
    }

    Spacer(Modifier.height(14.dp))
    BotonEco(
        texto = "Confirmar clasificación",
        icono = IconoAmb.PORTAPAPELES,
        habilitado = estado.categoriaElegida != null && estado.gravedadElegida != null,
        modifier = Modifier.fillMaxWidth(),
        onClick = { vm.confirmarClasificacion() }
    )
}

@Composable
private fun OpcionCategoria(
    categoria: Categoria,
    seleccionada: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val color = categoria.colorHex.aColor()
    Row(
        modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (seleccionada) color.copy(alpha = 0.22f) else Color.White)
            .border(
                BorderStroke(if (seleccionada) 3.dp else 2.dp, color.copy(alpha = if (seleccionada) 1f else 0.35f)),
                RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(categoria.simbolo, style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.width(6.dp))
        Text(
            categoria.etiqueta,
            style = MaterialTheme.typography.labelMedium,
            color = if (seleccionada) color else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun OpcionGravedad(
    gravedad: Gravedad,
    seleccionada: Boolean,
    onClick: () -> Unit
) {
    val color = gravedad.colorHex.aColor()
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(if (seleccionada) color.copy(alpha = 0.18f) else Color.White)
            .border(
                BorderStroke(if (seleccionada) 3.dp else 2.dp, color.copy(alpha = if (seleccionada) 1f else 0.35f)),
                RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier.size(28.dp).clip(CircleShape).background(color),
            contentAlignment = Alignment.Center
        ) {
            Text(gravedad.simbolo, style = MaterialTheme.typography.titleSmall, color = Color.White)
        }
        Spacer(Modifier.width(10.dp))
        Column {
            Text(gravedad.etiqueta, style = MaterialTheme.typography.titleSmall, color = color)
            Text(
                gravedad.descripcion,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// ----------------------------------------------------------------- REGLA

@Composable
private fun PanelRegla(vm: AuditoriaViewModel, estado: EstadoAuditoria) {
    val regla = estado.regla ?: return
    CabeceraPaso("Paso 3 · La regla", regla.titulo, IconoAmb.LIBRO, EcoColores.VerdeSelva)
    estado.feedback?.let {
        TarjetaFeedback(it)
        Spacer(Modifier.height(10.dp))
    }
    FichaRegla(regla)
    Spacer(Modifier.height(14.dp))
    BotonEco(
        texto = "Proponer una acción",
        icono = IconoAmb.MOCHILA,
        modifier = Modifier.fillMaxWidth(),
        onClick = { vm.continuarDesdeRegla() }
    )
}

/** Ficha de una regla ambiental, con la referencia normativa bien separada. */
@Composable
fun FichaRegla(regla: ReglaAmbiental, modifier: Modifier = Modifier) {
    TarjetaEco(
        modifier = modifier.fillMaxWidth(),
        color = Color.White,
        borde = regla.categoria.colorHex.aColor().copy(alpha = 0.45f)
    ) {
        ChipCategoria(regla.categoria, compacto = true)
        Spacer(Modifier.height(8.dp))
        Text("Regla EcoGuardián", style = MaterialTheme.typography.labelMedium, color = EcoColores.VerdeSelva)
        Text(regla.reglaSimple, style = MaterialTheme.typography.titleMedium)

        Spacer(Modifier.height(10.dp))
        Text("¿Por qué importa?", style = MaterialTheme.typography.titleSmall)
        Text(regla.explicacion, style = MaterialTheme.typography.bodyMedium)

        Spacer(Modifier.height(8.dp))
        Text("Ejemplo", style = MaterialTheme.typography.titleSmall)
        Text(regla.ejemplo, style = MaterialTheme.typography.bodyMedium)

        Spacer(Modifier.height(8.dp))
        Text("Qué hacer", style = MaterialTheme.typography.titleSmall)
        Text(regla.accionCorrecta, style = MaterialTheme.typography.bodyMedium)

        if (regla.referencia != null) {
            Spacer(Modifier.height(12.dp))
            SeparadorEco()
            Spacer(Modifier.height(10.dp))
            Column(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(EcoColores.AzulNiebla)
                    .padding(12.dp)
            ) {
                Text(
                    "REFERENCIA NORMATIVA DEL PERÚ",
                    style = MaterialTheme.typography.labelSmall,
                    color = EcoColores.AzulProfundo
                )
                Spacer(Modifier.height(4.dp))
                Text(regla.referencia.norma, style = MaterialTheme.typography.titleSmall)
                Text(regla.referencia.finalidad, style = MaterialTheme.typography.bodySmall)
                Spacer(Modifier.height(4.dp))
                Text(
                    regla.referencia.emisor,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    "Esto es material educativo, no asesoría legal.",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// ---------------------------------------------------------------- ACCIÓN

@Composable
private fun PanelAccion(vm: AuditoriaViewModel, estado: EstadoAuditoria) {
    val situacion = estado.situacion ?: return
    CabeceraPaso(
        "Paso 4 · Acción correctiva",
        "¿Qué soluciona el problema?",
        IconoAmb.MOCHILA,
        EcoColores.NaranjaFuego
    )
    Text(situacion.observacion, style = MaterialTheme.typography.bodyMedium)
    Spacer(Modifier.height(12.dp))

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        estado.acciones.forEach { accion ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color.White)
                    .border(
                        BorderStroke(2.dp, EcoColores.VerdeHoja.copy(alpha = 0.35f)),
                        RoundedCornerShape(18.dp)
                    )
                    .clickable { vm.elegirAccion(accion.id) }
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                EcoIcono(IconoAmb.HUELLA, tam = 24.dp)
                Spacer(Modifier.width(10.dp))
                Text(accion.texto, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }

    if (estado.feedback != null) {
        Spacer(Modifier.height(12.dp))
        TarjetaFeedback(estado.feedback)
    }
}

// ------------------------------------------------------------------ RETO

@Composable
private fun PanelReto(vm: AuditoriaViewModel, estado: EstadoAuditoria) {
    val reto = estado.reto ?: return
    CabeceraPaso("Paso 5 · Manos a la obra", reto.tipo.etiqueta, IconoAmb.CASCO, EcoColores.SolAmarillo)
    RetoInteractivo(
        reto = reto,
        solucion = estado.solucionReto,
        onColocar = vm::colocarPieza,
        onQuitar = vm::quitarPieza
    )
    if (estado.feedback != null) {
        Spacer(Modifier.height(12.dp))
        TarjetaFeedback(estado.feedback)
    }
    Spacer(Modifier.height(14.dp))
    BotonEco(
        texto = "Comprobar",
        icono = IconoAmb.ESCUDO,
        habilitado = estado.solucionReto.size == reto.piezas.size,
        modifier = Modifier.fillMaxWidth(),
        onClick = { vm.verificarReto() }
    )
}

// -------------------------------------------------------------- RESUELTO

@Composable
private fun PanelResuelto(vm: AuditoriaViewModel, estado: EstadoAuditoria) {
    val situacion = estado.situacion
    CabeceraPaso(
        "Hallazgo cerrado",
        situacion?.nombre ?: "Listo",
        situacion?.icono ?: IconoAmb.ESCUDO,
        EcoColores.VerdeHoja
    )
    estado.feedback?.let { TarjetaFeedback(it) }

    if (situacion != null && situacion.esProblema) {
        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ChipCategoria(situacion.categoria, compacto = true)
            ChipGravedad(situacion.gravedad)
        }
    }

    Spacer(Modifier.height(16.dp))
    BotonEco(
        texto = "Seguir explorando",
        icono = IconoAmb.LUPA,
        modifier = Modifier.fillMaxWidth(),
        onClick = { vm.terminarSituacion() }
    )
}

// ---------------------------------------------------------------- CIERRE

@Composable
private fun PanelCierre(
    vm: AuditoriaViewModel,
    estado: EstadoAuditoria,
    alResultado: (Long) -> Unit
) {
    CabeceraPaso("Acta de auditoría", "Lo que llevas anotado", IconoAmb.PORTAPAPELES, EcoColores.AzulProfundo)

    val registrados = estado.marcas.values.filter { it.marcadaComoProblema }
    if (registrados.isEmpty()) {
        Text(
            "Todavía no has registrado ningún hallazgo. Sigue explorando el escenario.",
            style = MaterialTheme.typography.bodyLarge
        )
    } else {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            registrados.forEach { marca ->
                val situacion = CatalogoEscenarios.situacion(marca.situacionId) ?: return@forEach
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    EcoIcono(situacion.icono, tam = 28.dp)
                    Spacer(Modifier.width(10.dp))
                    Column(Modifier.weight(1f)) {
                        Text(situacion.nombre, style = MaterialTheme.typography.titleSmall)
                        Text(
                            if (marca.accionAcertada) {
                                "Acción correctiva propuesta"
                            } else if (marca.categoriaElegida != null) {
                                "Clasificado, falta la acción"
                            } else {
                                "Registrado"
                            },
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    if (marca.retoSuperado) Text("🛠️", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }

    Spacer(Modifier.height(10.dp))
    Text(
        "Faltan por revisar " + estado.situacionesRestantes + " puntos del escenario.",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Start
    )

    Spacer(Modifier.height(16.dp))
    BotonEco(
        texto = if (estado.puedeCerrar) "Cerrar la auditoría" else "Aún faltan hallazgos",
        icono = IconoAmb.MEDALLA,
        habilitado = estado.puedeCerrar && !estado.guardando,
        modifier = Modifier.fillMaxWidth(),
        onClick = { vm.cerrarAuditoria(alResultado) }
    )
    Spacer(Modifier.height(8.dp))
    BotonEcoSuave(
        texto = "Seguir investigando",
        color = EcoColores.CarbonSuave,
        modifier = Modifier.fillMaxWidth(),
        onClick = { vm.volverAExplorar() }
    )
}
