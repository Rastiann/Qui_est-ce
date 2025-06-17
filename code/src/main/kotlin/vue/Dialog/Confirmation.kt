package vue.Dialog

import javafx.application.Platform
import javafx.scene.control.Alert
import javafx.scene.control.ButtonBar
import javafx.scene.control.ButtonType
import javafx.scene.control.TextInputDialog

class Confirmation() {

    private val alert = Alert(Alert.AlertType.CONFIRMATION)

    init {
        alert.headerText = ""
        alert.contentText = "Etes-vous sûr de vouloir guess ce personnage"
        alert.title = "CONFIRMATION"

        val dialogPane = alert.dialogPane
        val okButton = ButtonType("Guess", ButtonBar.ButtonData.OK_DONE)
        val cancelButton = ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE)

        alert.buttonTypes.setAll(okButton, cancelButton)

        val buttonOk = dialogPane.lookupButton(okButton)
        val buttonCancel = dialogPane.lookupButton(cancelButton)


        val buttonStyle = """
            -fx-background-color: orange;
            -fx-text-fill: black;
            -fx-font-weight: bold;
        """.trimIndent()

        buttonOk.style = buttonStyle
        buttonCancel.style = buttonStyle

        val resultat = alert.showAndWait()

        var question: String? = null

        if (resultat.get() == ButtonType.OK) {
            // TODO()
        }
    }

    fun show() {
        alert.showAndWait()
        Platform.exit()
    }
}