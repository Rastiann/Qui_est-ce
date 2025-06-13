package Vue

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.text.Font

class VueCreationJoueur() {
    val root = VBox(20.0)

    val firstNameField = TextField()
    val lastNameField = TextField()
    val createButton = Button("Créer le joueur")

    init {
        root.padding = Insets(30.0)
        root.alignment = Pos.TOP_CENTER
        root.style = "-fx-background-color: #1e1e1e;"
        firstNameField.style = "-fx-background-color: #383838"
        lastNameField.style = "-fx-background-color: #383838"

        val title = Label("Créer un joueur")
        title.font = Font.font(20.0)
        title.alignment = Pos.CENTER

        val form = VBox(10.0)
        form.alignment = Pos.CENTER

        val firstNameBox = HBox(10.0, Label("firstname :"), firstNameField)
        val lastNameBox = HBox(10.0, Label("lastname :"), lastNameField)
        firstNameBox.alignment = Pos.CENTER
        lastNameBox.alignment = Pos.CENTER

        form.children.addAll(firstNameBox, lastNameBox, createButton)

        root.children.addAll(title, form)
    }


}