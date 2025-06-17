package vue

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.text.Font

class GameVue() {
    val main = VBox(30.0)
    val root = GamePane("", main)
    var title = Label("Votre tour")

    init {

        title.font = Font.font(50.0)
        title.textFill = Color.LIGHTGRAY
        title.style = "-fx-background-color: #444; -fx-padding: 10px 20px;-fx-background-radius: 30px;"
        title.maxWidth = Double.MAX_VALUE
        title.padding = Insets(0.0, 0.0, 40.0, 0.0)

        val titleBox = HBox(title)
        titleBox.alignment = Pos.CENTER
        titleBox.maxWidth = Double.MAX_VALUE
        main.children.addAll(titleBox)
    }

}