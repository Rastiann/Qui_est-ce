
import controleur.AppController
import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage
import vue.game.EndVue

class MainApp : Application() {
    override fun start(stage: Stage) {

        AppController(
            "localhost",
            8080,
            stage
        )

        stage.show()
    }
}

fun main() {
    Application.launch(MainApp::class.java)
}
