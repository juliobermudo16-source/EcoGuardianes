package pe.ecoguardianes.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import pe.ecoguardianes.domain.model.ZonaId
import pe.ecoguardianes.ui.pantallas.AjustesPantalla
import pe.ecoguardianes.ui.pantallas.AppViewModel
import pe.ecoguardianes.ui.pantallas.AuditoriaViewModel
import pe.ecoguardianes.ui.pantallas.BibliotecaPantalla
import pe.ecoguardianes.ui.pantallas.ColeccionPantalla
import pe.ecoguardianes.ui.pantallas.EscenarioPantalla
import pe.ecoguardianes.ui.pantallas.InsigniasPantalla
import pe.ecoguardianes.ui.pantallas.MapaPantalla
import pe.ecoguardianes.ui.pantallas.MisionPantalla
import pe.ecoguardianes.ui.pantallas.OnboardingPantalla
import pe.ecoguardianes.ui.pantallas.PerfilCrearPantalla
import pe.ecoguardianes.ui.pantallas.PerfilPantalla
import pe.ecoguardianes.ui.pantallas.ResultadoPantalla
import pe.ecoguardianes.ui.pantallas.ResultadoViewModel
import pe.ecoguardianes.ui.pantallas.SplashPantalla
import pe.ecoguardianes.ui.theme.EcoGuardianesTema

/** Punto de entrada de la interfaz: tema, navegación y estado global. */
@Composable
fun EcoApp() {
    val repositorio = recordarRepositorio()
    val appVm: AppViewModel = viewModel(factory = factoria { AppViewModel(repositorio) })
    val auditoriaVm: AuditoriaViewModel =
        viewModel(factory = factoria { AuditoriaViewModel(repositorio) })
    val resultadoVm: ResultadoViewModel =
        viewModel(factory = factoria { ResultadoViewModel(repositorio) })

    val estado by appVm.estado.collectAsStateWithLifecycle()
    val estadoAuditoria by auditoriaVm.estado.collectAsStateWithLifecycle()

    EcoGuardianesTema(preferencias = estado.preferencias) {
        val nav = rememberNavController()

        NavHost(navController = nav, startDestination = Rutas.SPLASH) {

            composable(Rutas.SPLASH) {
                SplashPantalla(
                    alTerminar = {
                        val destino = if (estado.necesitaOnboarding) {
                            Rutas.ONBOARDING
                        } else {
                            Rutas.MAPA
                        }
                        nav.navigate(destino) {
                            popUpTo(Rutas.SPLASH) { inclusive = true }
                        }
                    }
                )
            }

            composable(Rutas.ONBOARDING) {
                OnboardingPantalla(alContinuar = { nav.navigate(Rutas.PERFIL_CREAR) })
            }

            composable(Rutas.PERFIL_CREAR) {
                PerfilCrearPantalla(
                    aliasInicial = estado.perfil?.alias.orEmpty(),
                    avatarInicial = estado.perfil?.avatarId ?: "AV_HOJA",
                    alConfirmar = { alias, avatar ->
                        appVm.crearPerfil(alias, avatar)
                        nav.navigate(Rutas.MAPA) {
                            popUpTo(Rutas.ONBOARDING) { inclusive = true }
                        }
                    }
                )
            }

            composable(Rutas.MAPA) {
                MapaPantalla(
                    estado = estado,
                    alAbrirZona = { zona -> nav.navigate(Rutas.mision(zona.name)) },
                    alAbrirColeccion = { nav.navigate(Rutas.COLECCION) },
                    alAbrirInsignias = { nav.navigate(Rutas.INSIGNIAS) },
                    alAbrirBiblioteca = { nav.navigate(Rutas.BIBLIOTECA) },
                    alAbrirPerfil = { nav.navigate(Rutas.PERFIL) }
                )
            }

            composable(
                route = Rutas.MISION + "/{" + Rutas.ARG_ZONA + "}",
                arguments = listOf(navArgument(Rutas.ARG_ZONA) { type = NavType.StringType })
            ) { entrada ->
                val zona = ZonaId.porId(entrada.arguments?.getString(Rutas.ARG_ZONA).orEmpty())
                    ?: ZonaId.CASA
                MisionPantalla(
                    zona = zona,
                    juego = estado.juego,
                    alVolver = { nav.popBackStack() },
                    alEmpezar = {
                        auditoriaVm.reiniciarMision()
                        nav.navigate(Rutas.escenario(zona.name))
                    }
                )
            }

            composable(
                route = Rutas.ESCENARIO + "/{" + Rutas.ARG_ZONA + "}",
                arguments = listOf(navArgument(Rutas.ARG_ZONA) { type = NavType.StringType })
            ) { entrada ->
                val zona = ZonaId.porId(entrada.arguments?.getString(Rutas.ARG_ZONA).orEmpty())
                    ?: ZonaId.CASA
                EscenarioPantalla(
                    vm = auditoriaVm,
                    zona = zona,
                    alVolver = { nav.popBackStack() },
                    alResultado = { id ->
                        nav.navigate(Rutas.resultado(id)) {
                            popUpTo(Rutas.MAPA)
                        }
                    }
                )
            }

            composable(
                route = Rutas.RESULTADO + "/{" + Rutas.ARG_AUDITORIA + "}",
                arguments = listOf(navArgument(Rutas.ARG_AUDITORIA) { type = NavType.LongType })
            ) { entrada ->
                val id = entrada.arguments?.getLong(Rutas.ARG_AUDITORIA) ?: 0L
                ResultadoPantalla(
                    vm = resultadoVm,
                    auditoriaId = id,
                    resumen = estadoAuditoria.resumen,
                    alVolverAlMapa = {
                        nav.navigate(Rutas.MAPA) { popUpTo(Rutas.MAPA) { inclusive = true } }
                    },
                    alRepetir = { zona ->
                        auditoriaVm.reiniciarMision()
                        nav.navigate(Rutas.escenario(zona.name)) {
                            popUpTo(Rutas.MAPA)
                        }
                    }
                )
            }

            composable(Rutas.BIBLIOTECA) {
                BibliotecaPantalla(alVolver = { nav.popBackStack() })
            }

            composable(Rutas.COLECCION) {
                ColeccionPantalla(juego = estado.juego, alVolver = { nav.popBackStack() })
            }

            composable(Rutas.INSIGNIAS) {
                InsigniasPantalla(juego = estado.juego, alVolver = { nav.popBackStack() })
            }

            composable(Rutas.PERFIL) {
                PerfilPantalla(
                    estado = estado,
                    alVolver = { nav.popBackStack() },
                    alAbrirAjustes = { nav.navigate(Rutas.AJUSTES) },
                    alCambiarAvatar = { appVm.actualizarAjustes(avatarId = it) }
                )
            }

            composable(Rutas.AJUSTES) {
                AjustesPantalla(
                    estado = estado,
                    alVolver = { nav.popBackStack() },
                    alCambiarAjuste = { sonido, haptica, texto, pistas ->
                        appVm.actualizarAjustes(
                            sonido = sonido,
                            haptica = haptica,
                            textoGrande = texto,
                            pistas = pistas
                        )
                    },
                    alReiniciar = { appVm.reiniciarProgreso() }
                )
            }
        }
    }
}
