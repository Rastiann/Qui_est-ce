package controleur.gameinit

import SpinnerVue
import javafx.scene.Parent
import state.gameinit.OtherPlayerChoosingCharacter
import state.gameinit.WaitingForOtherPlayer

class OtherPlayerChoosingCharacterController: GameInitController<OtherPlayerChoosingCharacter> {

    private val vue = SpinnerVue("L'adversaire choisit son personnage ...", SpinnerVue.Direction.VERTICAL)

    override fun getVue(): Parent {
        return vue
    }

    override fun update(gameInitState: OtherPlayerChoosingCharacter) { }
}