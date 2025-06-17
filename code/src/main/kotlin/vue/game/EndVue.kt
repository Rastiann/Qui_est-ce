package vue.game

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Cursor
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.text.Font
import javafx.scene.control.Button
import javafx.scene.text.TextAlignment

class EndVue(endState: EndState) : BorderPane() {

    enum class EndState {
        WIN,
        LOSE
    }

    val pane = VBox()
    val pane2 = HBox()
    val menuButton = Button("Menu Principale")


    init {
        pane.alignment = Pos.CENTER
        pane.style = "-fx-background-color: #1e1e1e;"

        pane2.alignment = Pos.CENTER
        pane2.style = "-fx-background-color: #1e1e1e;"
        pane2.padding = Insets(20.0)

        val label = Label()

        if (endState == EndState.WIN) {
            label.text = "Bravo ! Tu as deviné le personnage ! \uD83C\uDFC6\uD83C\uDFC6\uD83C\uDF89\uD83C\uDF89"
        } else {
            label.text = "Perdu ! L’adversaire a mis la main sur le bon visage. \uD83D\uDC94\uD83D\uDC94\uD83E\uDD40\uD83E\uDD40"
        }

        label.maxWidth = Double.MAX_VALUE // permet d'étirer le label à max
        label.isWrapText = true // pour que le texte passe à la ligne si trop long

        label.font = Font.font(50.0)
        label.alignment = Pos.CENTER
        label.style = "-fx-text-fill: white"
        label.padding = Insets(20.0)
        label.textAlignment = TextAlignment.CENTER
        label.alignment = Pos.CENTER

        menuButton.prefWidth = 300.0
        menuButton.prefHeight = 70.0

        menuButton.style = """
        -fx-background-color: orange;
        -fx-border-color: black;
        -fx-border-width: 2;
        -fx-background-radius: 15;
        -fx-border-radius: 15;
        -fx-text-fill: black;
        -fx-font-size: 24px;
        """.trimIndent()

        menuButton.setOnMouseEntered {
            menuButton.cursor = Cursor.HAND
        }
        menuButton.setOnMouseExited {
            menuButton.cursor = Cursor.DEFAULT
        }

        pane2.children.add(label)

        pane.children.addAll(pane2, menuButton)
        this.center = pane
    }
}