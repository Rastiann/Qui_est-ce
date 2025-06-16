package controleur

import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.control.TextInputDialog

class AskQuestionController() : EventHandler<ActionEvent> {

    override fun handle(p0: ActionEvent?) {
        val dialog = TextInputDialog("Poser une Question")
        dialog.headerText = "Poser votre question"
        dialog.contentText = "Question"

        val resultat = dialog.showAndWait()

        var question: String? = null

        if (resultat.isPresent) {
            question = resultat.get()
        }
    }
}