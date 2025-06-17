package vue.game

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox

class UserTurnVue {

    val root: VBox
    val passBtn = Button("Pass")
    val guessBtn = Button("Guess")
    val questionBtn = Button("Question")
    var buttonBox : HBox

    init {
        val buttons = listOf(passBtn, guessBtn, questionBtn)
        buttons.forEach {
            it.style = "-fx-background-color: orange; -fx-text-fill: black; -fx-font-weight: bold;"
            it.prefWidth = 100.0
        }

        buttonBox = HBox(20.0, passBtn, guessBtn, questionBtn) // 20.0 = espacement horizontal
        buttonBox.alignment = Pos.CENTER
        buttonBox.padding = Insets(10.0)

        root = VBox(buttonBox)
    }

}