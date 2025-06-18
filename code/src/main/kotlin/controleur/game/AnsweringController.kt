package controleur.game

import javafx.scene.Parent
import javafx.scene.control.Alert
import javafx.scene.control.ButtonBar
import javafx.scene.control.ButtonType
import javafx.scene.control.TextInputDialog
import javafx.scene.layout.VBox
import state.game.Answering

class AnsweringController: GameController<Answering> {


    override fun getVue(): Parent {
        return VBox()
    }

    override fun update(gameInitState: Answering) {
        """val dialog = TextInputDialog("Répondre")
        dialog.headerText = "Répondre à votre adversaire"
        dialog.contentText = gameInitState.question

        val dialogPane = dialog.dialogPane
        val okButton = dialogPane.lookupButton(ButtonType.OK)
        val cancelButton = dialogPane.lookupButton(ButtonType.CANCEL)

        val buttonStyle =
            -fx-background-color: orange;
            -fx-text-fill: black;
            -fx-font-weight: bold;
        .trimIndent()

        okButton.style = buttonStyle
        cancelButton.style = buttonStyle

        val result = dialog.showAndWait()
        if (!result.isPresent) { return }

        val question = result.get()
        gameInitState.answer(question)"""

        val dialog = Alert(Alert.AlertType.CONFIRMATION)
        dialog.title= "Réponse"
        dialog.headerText="Choisisez votre réponse"
        dialog.contentText= gameInitState.question
        val okButtonType = ButtonType("oui", ButtonBar.ButtonData.OK_DONE)
        val cancelButtonType = ButtonType("non", ButtonBar.ButtonData.CANCEL_CLOSE)

        // Remplacer les boutons par défaut
        dialog.buttonTypes.setAll(okButtonType, cancelButtonType)

        val réponse =  dialog.showAndWait()

        val question = réponse.get().text
        gameInitState.answer(question)

    }
}