package pe.ecoguardianes.ui.pantallas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import pe.ecoguardianes.data.local.PerfilEntity
import pe.ecoguardianes.data.repo.EcoRepositorio
import pe.ecoguardianes.domain.audit.EstadoJuego
import pe.ecoguardianes.ui.theme.PreferenciasEco

data class EstadoApp(
    val cargando: Boolean = true,
    val perfil: PerfilEntity? = null,
    val juego: EstadoJuego = EstadoJuego(),
    val preferencias: PreferenciasEco = PreferenciasEco()
) {
    val necesitaOnboarding: Boolean
        get() = perfil == null || !perfil.onboardingCompletado || perfil.alias.isBlank()
}

/** Estado global: perfil, preferencias y progreso, siempre desde la base de datos. */
class AppViewModel(private val repo: EcoRepositorio) : ViewModel() {

    val estado: StateFlow<EstadoApp> = combine(
        repo.perfil,
        repo.estadoJuego
    ) { perfil, juego ->
        EstadoApp(
            cargando = false,
            perfil = perfil,
            juego = juego,
            preferencias = PreferenciasEco(
                sonido = perfil?.sonidoActivado ?: true,
                haptica = perfil?.hapticaActivada ?: true,
                textoGrande = perfil?.textoGrande ?: false,
                pistas = perfil?.pistasAutomaticas ?: true
            )
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), EstadoApp())

    init {
        viewModelScope.launch { repo.asegurarPerfil() }
    }

    fun crearPerfil(alias: String, avatarId: String) {
        viewModelScope.launch { repo.crearPerfil(alias, avatarId) }
    }

    fun actualizarAjustes(
        sonido: Boolean? = null,
        haptica: Boolean? = null,
        textoGrande: Boolean? = null,
        pistas: Boolean? = null,
        alias: String? = null,
        avatarId: String? = null
    ) {
        viewModelScope.launch {
            repo.actualizarAjustes(sonido, haptica, textoGrande, pistas, alias, avatarId)
        }
    }

    fun reiniciarProgreso() {
        viewModelScope.launch { repo.reiniciarProgreso() }
    }
}
