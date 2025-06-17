package controleur.game

import javafx.scene.Parent
import state.game.UserTurn
import vue.game.UserTurnVue

class UserTurnController : GameController<UserTurn> {

    private val vue = UserTurnVue()

    override fun getVue(): Parent {
        return vue.root
    }

    override fun update(gameInitState: UserTurn) {
        vue.passBtn.setOnAction {
            gameInitState.skipTurn()
        }

        vue.guessBtn.setOnAction {

        }
    }
}