package controleur

import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.control.ButtonType
import javafx.scene.control.TextInputDialog
import state.game.UserTurn

class AskQuestionDialogController(
    val state: UserTurn
) : EventHandler<ActionEvent> {

    override fun  handle(p0: ActionEvent?) {
        val dialog = TextInputDialog("Poser une Question")
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

        val result = dialog.showAndWait()
        if (!result.isPresent) { return }

        val question = result.get()
        state.ask(question)
    }
}
