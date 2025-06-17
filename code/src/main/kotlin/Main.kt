
import controleur.AppController
import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage
import vue.Dialog.Confirmation
import vue.game.EndVue

class MainApp : Application() {
    override fun start(stage: Stage) {

        val root = Confirmation()
//        val scene = Scene(root, 600.0, 400.0)
//
//        stage.scene = scene
//        stage.title = "Fin de partie"
//        stage.show()
    }
}

fun main() {
    Application.launch(MainApp::class.java)
}
