package pe.ecoguardianes

import android.app.Application
import android.content.Context
import pe.ecoguardianes.data.local.EcoDatabase
import pe.ecoguardianes.data.repo.EcoRepositorio

/**
 * Contenedor de dependencias sencillo: sin librerias de inyeccion,
 * porque la aplicacion es pequena y todo vive en el dispositivo.
 */
class Contenedor(context: Context) {
    private val base: EcoDatabase = EcoDatabase.obtener(context)
    val repositorio: EcoRepositorio = EcoRepositorio(base)
}

class EcoGuardianesApp : Application() {
    lateinit var contenedor: Contenedor
        private set

    override fun onCreate() {
        super.onCreate()
        contenedor = Contenedor(this)
    }
}
