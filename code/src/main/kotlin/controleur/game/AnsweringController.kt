package controleur.game

import javafx.scene.Parent
import javafx.scene.control.ButtonType
import javafx.scene.control.TextInputDialog
import javafx.scene.layout.VBox
import state.game.Answering

class AnsweringController: GameController<Answering> {


    override fun getVue(): Parent {
        return VBox()
    }

    override fun update(gameInitState: Answering) {
        val dialog = TextInputDialog("Répondre")
        dialog.headerText = "Répondre à votre adversaire"
        dialog.contentText = gameInitState.question

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
        gameInitState.answer(question)
    }

}