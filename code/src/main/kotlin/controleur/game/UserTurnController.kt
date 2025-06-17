package controleur.game

import vue.dialog.AskQuestionDialog
import javafx.scene.Parent
import state.game.UserTurn
import vue.game.UserTurnVue

class UserTurnController : GameController<UserTurn> {

    private val vue = UserTurnVue()

    override fun getVue(): Parent {
        return vue.root
    }

    override fun update(gameInitState: UserTurn) {
        vue.questionBtn.setOnAction {
            val question = AskQuestionDialog().show()
            if (question != null) {
                gameInitState.ask(question)
            }
        }
    }
}