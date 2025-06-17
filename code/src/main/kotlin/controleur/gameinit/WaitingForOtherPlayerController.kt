package controleur.gameinit

import javafx.scene.Parent
import state.gameinit.WaitingForOtherPlayer
import vue.gameinit.WaitingForOtherPlayerVue

class WaitingForOtherPlayerController: GameInitController<WaitingForOtherPlayer> {

    private val vue = WaitingForOtherPlayerVue()

    override fun getVue(): Parent { return vue }

    override fun update(gameInitState: WaitingForOtherPlayer) {}
}