package controleur

import ConnectedPlayer
import javafx.application.Platform
import javafx.scene.Scene
import javafx.stage.Stage
import state.*
import vue.ErrorDialog
import java.nio.file.Paths

class AppController(
    host: String,
    port: Int,
    stage: Stage
): StateChangeHandler {

    private val scene: Scene

    private var appState: AppState? = null
    private var storedPlayer: ConnectedPlayer? = null

    private var connectingController = ConnectingController()
    private var playerCreationController: PlayerCreationController? = null
    private var homeController: HomeController? = null
    private var gameInitController: GameInitController? = null
    private var gameController: GameController? = null

    init {

        // init windows
        scene = Scene(connectingController.getVue(), 900.0, 600.0)
        stage.isResizable = true
        stage.title = "Qui est-ce ?"
        stage.scene = scene

        // Unfortunately, the connection is made in the constructor,
        // which makes the creation of a state concurrent.
        // And makes error handling more complex
        Thread {

            try {

                // appState is not read until the handle function is called
                // which makes this assignment safe
                appState = Connecting(host, port, this)

            }catch (_: Throwable) {
                showConnectionError()
            }

            try {

                val configPath = Paths.get(System.getProperty("user.home"), ".config", "quiestce")
                val playerFilePath =  configPath.resolve("player")

                // playerStored is not read until the handle function is called
                // which makes this assignment safe
                storedPlayer = ConnectedPlayer.readFrom(playerFilePath.toString())

            }catch(_: Throwable) {
                // do nothing is cache, if we cannot read player from file,
                // functionality will just not be display
            }

        }.start()
    }

    private fun showConnectionError() {
        Platform.runLater {
            ErrorDialog(
                "Erreur de connection",
                "Serveur injoignable",
                "Impossible de se connecter au serveur"
            ).show()
        }
    }

    override fun handle(newState: AppState, error: Throwable?) {

        Platform.runLater {

            if (error != null) {
                ErrorDialog(
                    "Erreur",
                    "Un erreur est survenu, veuillez réessayer plus tard",
                    error.message.toString()
                ).show()
            }

            when (newState) {
                is Connecting -> {

                    val controller = connectingController

                    // update vue (pass it to controller)
                    controller.update(newState)

                    // change root if stage as changed
                    if (appState !is Connecting) {
                        scene.root = controller.getVue()
                    }

                }
                is PlayerCreation -> {

                    // lazy creation of vue
                    if (playerCreationController == null) { playerCreationController = PlayerCreationController(
                        storedPlayer
                    )}

                    val controller = playerCreationController!!

                    // update vue (pass it to controller)
                    controller.update(newState)

                    // change root if stage as changed
                    if (appState !is PlayerCreation) {
                        scene.root = controller.getVue()
                    }

                }
                is Home -> {

                    // lazy creation of vue
                    if (homeController == null) { homeController = HomeController() }
                    val controller = homeController!!

                    // update vue (pass it to controller)
                    controller.update(newState)

                    // change root if stage as changed
                    if (appState !is Home) {
                        scene.root = controller.getVue()
                    }

                }
                is GameInit -> {

                    // lazy creation of vue
                    if (gameInitController == null) { gameInitController = GameInitController() }
                    val controller = gameInitController!!

                    // update vue (pass it to controller)
                    controller.update(newState)

                    // change root if stage as changed
                    if (appState !is GameInit) {
                        scene.root = controller.getVue()
                    }

                }
                is Game -> {

                    // lazy creation of vue
                    if (gameController == null) { gameController = GameController() }
                    val controller = gameController!!

                    // update vue (pass it to controller)
                    controller.update(newState)

                    // change root if stage as changed
                    if (appState !is Game) {
                        scene.root = controller.getVue()
                    }

                }
            }

        }
    }

}