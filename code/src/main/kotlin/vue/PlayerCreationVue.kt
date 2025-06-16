package vue

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.text.Font

class PlayerCreationVue() {
    val root = VBox(20.0)

    val firstNameField = TextField()
    val lastNameField = TextField()
    val createButton = Button("Créer le joueur")
    val connectButton = Button("Se connecter")

    init {
        root.padding = Insets(30.0)
        root.alignment = Pos.TOP_CENTER
        root.style = "-fx-background-color: #1e1e1e;"
        firstNameField.style = "-fx-background-color: #383838;-fx-text-fill: white"
        lastNameField.style = "-fx-background-color: #383838;;-fx-text-fill: white"

        val title = Label("Créer un joueur")
        title.font = Font.font(20.0)
        title.alignment = Pos.CENTER
        title.style = "-fx-text-fill: white"

        val form = VBox(10.0)
        form.alignment = Pos.CENTER
        val ButtonBox = HBox(10.0)
        ButtonBox.alignment = Pos.CENTER

        val firstNameBox = HBox(10.0)
        val lastNameBox = HBox(10.0)
        val firstnametitle = Label("FirstName :")
        val lastnametitle = Label("LastName :")
        firstNameBox.alignment = Pos.CENTER
        firstNameBox.style = "-fx-text-fill: white"
        firstnametitle.style = "-fx-text-fill: white"
        lastnametitle.style = "-fx-text-fill: white"
        lastNameBox.alignment = Pos.CENTER
        firstNameBox.children.addAll(firstnametitle,firstNameField)
        lastNameBox.children.addAll(lastnametitle,lastNameField)
        createButton.style = "-fx-background-color: orange"
        connectButton.style = "-fx-background-color: orange"
        ButtonBox.children.addAll(createButton,connectButton)
        form.children.addAll(firstNameBox, lastNameBox, ButtonBox)

        root.children.addAll(title, form)
    }


}