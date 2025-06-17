package vue.game_vue

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox

class UserTurnVue {

    val root: VBox
    val btn = Button("Pass")
    val btn2 = Button("Guess")
    val btn3 = Button("Question")

    init {
        val buttons = listOf(btn, btn2, btn3)
        buttons.forEach {
            it.style = "-fx-background-color: orange; -fx-text-fill: black; -fx-font-weight: bold;"
            it.prefWidth = 100.0
        }

        val buttonBox = HBox(20.0, btn, btn2, btn3) // 20.0 = espacement horizontal
        buttonBox.alignment = Pos.CENTER
        buttonBox.padding = Insets(10.0)

        root = VBox(buttonBox)
    }

}