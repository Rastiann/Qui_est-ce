package Vue

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ProgressIndicator
import javafx.scene.layout.VBox
import javafx.scene.text.Font


class ConnectionServerView {
    val root = VBox(20.0)
    val erreurButton = Button("Pop up erreur de connexion")

    init {
        root.alignment = Pos.CENTER
        root.style = "-fx-background-color: #1e1e1e;"

        val title = Label("Connexion au serveur")
        title.font = Font.font(30.0)
        title.alignment = Pos.CENTER
        title.style = "-fx-text-fill: white"
        title.padding = Insets(20.0)

        val spinner = ProgressIndicator()
        spinner.setPrefSize(100.0, 100.0)

        root.children.addAll(title, spinner, erreurButton)
    }
}