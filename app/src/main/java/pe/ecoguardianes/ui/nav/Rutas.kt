package pe.ecoguardianes.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import pe.ecoguardianes.EcoGuardianesApp
import pe.ecoguardianes.data.repo.EcoRepositorio

/** Rutas de navegación de EcoGuardianes. */
object Rutas {
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"
    const val PERFIL_CREAR = "perfil_crear"
    const val MAPA = "mapa"
    const val MISION = "mision"
    const val ESCENARIO = "escenario"
    const val RESULTADO = "resultado"
    const val BIBLIOTECA = "biblioteca"
    const val COLECCION = "coleccion"
    const val INSIGNIAS = "insignias"
    const val PERFIL = "perfil"
    const val AJUSTES = "ajustes"

    const val ARG_ZONA = "zona"
    const val ARG_AUDITORIA = "auditoriaId"

    fun mision(zona: String) = MISION + "/" + zona
    fun escenario(zona: String) = ESCENARIO + "/" + zona
    fun resultado(id: Long) = RESULTADO + "/" + id
}

/** Acceso al repositorio desde cualquier composable. */
@Composable
fun recordarRepositorio(): EcoRepositorio {
    val contexto = LocalContext.current.applicationContext
    return (contexto as EcoGuardianesApp).contenedor.repositorio
}

/** Fábrica genérica de ViewModels sin librerías de inyección. */
fun <VM : ViewModel> factoria(crear: () -> VM): ViewModelProvider.Factory =
    object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T =
            crear() as T
    }
