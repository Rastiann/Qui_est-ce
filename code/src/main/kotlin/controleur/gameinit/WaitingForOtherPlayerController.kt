package controleur.gameinit

import SpinnerVue
import javafx.scene.Parent
import state.gameinit.WaitingForOtherPlayer

class WaitingForOtherPlayerController: GameInitController<WaitingForOtherPlayer> {

    private val vue = SpinnerVue("En attente d'un joueur ...", SpinnerVue.Direction.VERTICAL)

    override fun getVue(): Parent { return vue }

    override fun update(gameInitState: WaitingForOtherPlayer) {}
}