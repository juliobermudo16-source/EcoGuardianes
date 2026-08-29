package pe.ecoguardianes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import pe.ecoguardianes.ui.nav.EcoApp

/**
 * Única Activity de EcoGuardianes.
 * La rotación y los cambios de configuración los absorbe Compose, así que el
 * progreso de la misión no se pierde al girar el dispositivo.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent { EcoApp() }
    }
}
