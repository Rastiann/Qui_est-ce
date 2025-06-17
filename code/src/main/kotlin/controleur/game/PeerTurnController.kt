package controleur.game

import javafx.scene.Parent
import state.game.PeerTurn
import vue.game.PeerTurnVue

class PeerTurnController : GameController<PeerTurn> {

    private val vue = PeerTurnVue()

    override fun getVue(): Parent {
        return vue
    }

    override fun update(gameInitState: PeerTurn) { }
}