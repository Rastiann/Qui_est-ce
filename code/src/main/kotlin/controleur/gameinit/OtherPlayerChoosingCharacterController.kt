package controleur.gameinit

import javafx.scene.Parent
import state.gameinit.OtherPlayerChoosingCharacter
import vue.gameinit.OtherPlayerChoosingCharacterVue

class OtherPlayerChoosingCharacterController: GameInitController<OtherPlayerChoosingCharacter> {

    private val vue = OtherPlayerChoosingCharacterVue()

    override fun getVue(): Parent {
        return vue
    }

    override fun update(gameInitState: OtherPlayerChoosingCharacter) { }
}