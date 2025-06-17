package vue.game

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.text.Font
import javafx.scene.control.Button

class EndVue(endState: EndState) : BorderPane() {

    enum class EndState {
        WIN,
        LOSE
    }

    val pane = VBox()
    val pane2 = HBox()
    val menuButton = Button("Menu Principale")
    val path : String
    val image : Image
    val imageView : ImageView


    init {
        pane.alignment = Pos.CENTER
        pane.style = "-fx-background-color: #1e1e1e;"

        pane2.alignment = Pos.CENTER
        pane2.style = "-fx-background-color: #1e1e1e;"

        val label = Label()

        if (endState == EndState.WIN) {
            label.text = "Bravo ! Tu as deviné le personnage !"
            path = "image/win.png"
            image = Image(path)
        } else {
            label.text = "Échec ! L’adversaire a mis la main sur le bon visage. (Le personnage à déviné était [Perso])"
            path = "image/win.png"
            image = Image(path)
        }

        imageView = ImageView(image)

        label.font = Font.font(30.0)
        label.alignment = Pos.CENTER
        label.style = "-fx-text-fill: white"
        label.padding = Insets(20.0)

        pane2.children.addAll(label, imageView)

        pane.children.addAll(pane2, menuButton)
        this.center = pane
    }
}