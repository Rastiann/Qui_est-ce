package controleur

import controleur.game.PeerTurnController
import controleur.game.UserTurnControler
import vue.GameVue
import javafx.scene.Parent
import state.Game
import state.game.PeerTurn
import state.game.UserTurn

class GameController: StateController<Game> {
    private var UserTurn = UserTurnControler()
    private var PeerTurn: PeerTurnController? = null

    private var currentVue = UserTurn.getVue()
    private val vue = GameVue()

    override fun getVue(): Parent {
        return currentVue
    }

    override fun update(state: Game) {
        when (state.gameState){
            is UserTurn -> {
                UserTurn.update(state.gameState)

                currentVue = UserTurn.getVue()
            }
            is PeerTurn -> {
                if (PeerTurn == null){
                    PeerTurn = PeerTurnController()
                }

                PeerTurn.update(state.gameState)

                currentVue = PeerTurn.getVue()
            }
        }

    }
}