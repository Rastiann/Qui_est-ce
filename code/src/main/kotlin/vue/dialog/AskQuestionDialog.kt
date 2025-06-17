package vue.dialog

import javafx.application.Platform
import javafx.scene.control.ButtonType
import javafx.scene.control.TextInputDialog

class AskQuestionDialog() {

    private val dialog = TextInputDialog("Poser une Question")

    init {
        dialog.headerText = "Poser votre question"
        dialog.contentText = "Question :"

        val dialogPane = dialog.dialogPane
        val okButton = dialogPane.lookupButton(ButtonType.OK)
        val cancelButton = dialogPane.lookupButton(ButtonType.CANCEL)

        val buttonStyle = """
            -fx-background-color: orange;
            -fx-text-fill: black;
            -fx-font-weight: bold;
        """.trimIndent()

        okButton.style = buttonStyle
        cancelButton.style = buttonStyle
    }

    fun show(): String? {
        val result = dialog.showAndWait()

        var question: String? = null
        if (result.isPresent) {
            question = result.get()
        }
        return question
    }
}