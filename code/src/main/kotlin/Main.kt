import Vue.VueGameInit
import info.but1.sae2025.QuiEstCeClient
import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage
import kotlin.random.Random

class MainApp : Application() {
    override fun start(stage: Stage) {
        val view = VueGameInit()
        val left = List(24) { "👤" }
        val right = List(24) { "👤" }

        view.update(left, right)
        stage.isResizable = false

        stage.scene = Scene(view.root,800.0, 600.0)
        stage.title = "Qui est-ce ?"
        stage.show()
    }
}

fun main() {
    //println("Hello, World!")
    //var client: QuiEstCeClient = QuiEstCeClient("localhost", 8080)
    // configuration à modifier bien entendu
    //client.requeteEssai()
    Application.launch(MainApp::class.java)
}
