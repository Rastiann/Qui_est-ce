package controleur.game

import javafx.scene.Parent
import javafx.scene.control.Alert
import javafx.scene.control.ButtonBar
import javafx.scene.control.ButtonType
import javafx.scene.layout.VBox
import state.game.Answering

class AnsweringController: GameController<Answering> {


    override fun getVue(): Parent {
        return VBox()
    }

    override fun update(gameState: Answering) {
        val dialog = Alert(Alert.AlertType.CONFIRMATION)
        dialog.title= "Réponse"
        dialog.headerText="Choisisez votre réponse"
        dialog.contentText= gameState.question
        val okButtonType = ButtonType("oui", ButtonBar.ButtonData.OK_DONE)
        val cancelButtonType = ButtonType("non", ButtonBar.ButtonData.CANCEL_CLOSE)

        // Remplacer les boutons par défaut
        dialog.buttonTypes.setAll(okButtonType, cancelButtonType)

        val answer =  dialog.showAndWait()
        val question = answer.get().text
        gameState.answer(question)
    }
}