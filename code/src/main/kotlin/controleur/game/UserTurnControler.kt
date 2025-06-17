package controleur.game

import javafx.scene.Parent
import state.game.UserTurn
import vue.GameVue

class UserTurnControler : GameController<UserTurn> {
    private val vue = GameVue()
    override fun getVue(): Parent {
        return vue.root
    }

    override fun update(gameInitState: UserTurn) {
        TODO("Not yet implemented")
    }
}