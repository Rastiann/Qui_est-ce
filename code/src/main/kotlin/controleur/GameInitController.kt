package controleur

import controleur.gameinit.ChoosingCharacterController
import controleur.gameinit.WaitingForOtherPlayerController
import controleur.gameinit.OtherPlayerChoosingCharacterController
import javafx.scene.Parent
import state.GameInit
import state.gameinit.ChoosingCharacter
import state.gameinit.OtherPlayerChoosingCharacter
import state.gameinit.WaitingForOtherPlayer

class GameInitController: StateController<GameInit> {
    
    private var waitingForOtherPlayerController = WaitingForOtherPlayerController()
    private var choosingCharacterController: ChoosingCharacterController? = null
    private var otherPlayerChoosingCharacterController: OtherPlayerChoosingCharacterController? = null

    private var currentVue = waitingForOtherPlayerController.getVue()
        
    override fun getVue(): Parent {
        return currentVue
    }

    override fun update(state: GameInit) {
        when (state.gameInitState) {
            is WaitingForOtherPlayer -> {

                // update
                waitingForOtherPlayerController.update(state.gameInitState)

                // remember vue
                currentVue = waitingForOtherPlayerController.getVue()

            }
            is ChoosingCharacter -> {

                // lazy creation of vue
                if (choosingCharacterController == null) {
                    choosingCharacterController = ChoosingCharacterController()
                }

                // update
                choosingCharacterController!!.update(state.gameInitState)

                // remember vue
                currentVue = choosingCharacterController!!.getVue()

            }
            is OtherPlayerChoosingCharacter -> {

                // lazy creation of vue
                if (otherPlayerChoosingCharacterController == null) {
                    otherPlayerChoosingCharacterController = OtherPlayerChoosingCharacterController()
                }

                // update
                otherPlayerChoosingCharacterController!!.update(state.gameInitState)

                // remember vue
                currentVue = otherPlayerChoosingCharacterController!!.getVue()

            }
        }
    }
}