package controleur.game

import javafx.scene.Parent
import state.game.PeerTurn
import vue.GameVueWaiting

class PeerTurnController : GameController<PeerTurn> {
    private val vue = GameVueWaiting()
    override fun getVue(): Parent {
        return vue.root
    }

    override fun update(gameInitState: PeerTurn) {
        TODO("Not yet implemented")
    }
}