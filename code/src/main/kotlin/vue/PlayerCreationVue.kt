package vue

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.text.Font

class PlayerCreationVue(
    prevPlayerName: String?
) {
    val root = VBox(20.0)

    val firstNameField = TextField()
    val lastNameField = TextField()
    val createButton = Button("Créer le joueur")
    val connectButton = Button("Réutiliser $prevPlayerName")

    init {
        root.padding = Insets(30.0)
        root.alignment = Pos.CENTER
        root.style = "-fx-background-color: #1e1e1e;"
        firstNameField.style = "-fx-background-color: #383838;-fx-text-fill: white"
        lastNameField.style = "-fx-background-color: #383838;;-fx-text-fill: white"

        val title = Label("Créer un joueur")
        title.font = Font.font(20.0)
        title.alignment = Pos.CENTER
        title.style = "-fx-text-fill: white"

        val form = VBox(10.0)
        form.alignment = Pos.CENTER
        val buttonBox = HBox(10.0)
        buttonBox.alignment = Pos.CENTER

        val firstNameBox = HBox(10.0)
        val lastNameBox = HBox(10.0)
        val firstNameTitle = Label("FirstName :")
        val lastNameTitle = Label("LastName :")

        firstNameBox.alignment = Pos.CENTER
        firstNameBox.style = "-fx-text-fill: white"
        firstNameTitle.style = "-fx-text-fill: white"

        lastNameTitle.style = "-fx-text-fill: white"
        lastNameBox.alignment = Pos.CENTER

        firstNameBox.children.addAll(firstNameTitle,firstNameField)
        lastNameBox.children.addAll(lastNameTitle,lastNameField)

        createButton.style = "-fx-background-color: orange"
        connectButton.style = "-fx-background-color: orange"

        buttonBox.children.add(createButton)
        if (prevPlayerName != null) {
            buttonBox.children.add(connectButton)
        }

        form.children.addAll(firstNameBox, lastNameBox, buttonBox)

        root.children.addAll(title, form)
    }


}