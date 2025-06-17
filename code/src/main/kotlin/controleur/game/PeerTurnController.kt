package controleur.game

import SpinnerVue
import javafx.scene.Parent
import state.game.PeerTurn
import vue.GameVue
import vue.game.PeerTurnVue
import vue.game.UserTurnVue

class PeerTurnController : GameController<PeerTurn> {
    private val vue = PeerTurnVue()

    override fun getVue(): Parent {
        return vue
    }

    override fun update(gameInitState: PeerTurn) { }
}