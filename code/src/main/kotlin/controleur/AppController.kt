package controleur

import javafx.application.Platform
import javafx.scene.Scene
import state.*

class AppController(
    host: String,
    port: Int,
    var scene: Scene
): StateChangeHandler {

    private var appState: AppState? = null

    private var connectingController = ConnectingController()
    private var playerCreationController: PlayerCreationController? = null
    private var homeController: HomeController? = null
    private var gameInitController: GameInitController? = null
    private var gameController: GameController? = null

    init {

        scene.root = connectingController.getVue()

        // Unfortunately, the connection is made in the constructor,
        // which makes the creation of a state concurrent.
        // And makes error handling more complex
        Thread {

            try {

                // appState is not read until the handle function is called
                // which makes this assignment safe
                appState = Connecting(host, port, this)

            }catch (e: Error) {
                showConnectionError()
            }

        }.start()

    }

    private fun showConnectionError() {
        Platform.runLater {
            TODO("show error dialog")
        }
    }

    override fun handle(newState: AppState, error: Error?) {
        Platform.runLater {

            if (error != null) {
                // TODO: show dialog
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
                    if (playerCreationController == null) { playerCreationController = PlayerCreationController() }
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