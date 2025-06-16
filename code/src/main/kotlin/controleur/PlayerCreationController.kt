package controleur

import vue.PlayerCreationVue
import javafx.scene.Parent
import state.PlayerCreation

class PlayerCreationController: StateController<PlayerCreation> {

    private val vue = PlayerCreationVue()

    override fun getVue(): Parent {
        TODO("Not yet implemented")
    }

    override fun update(state: PlayerCreation) {
        TODO("Not yet implemented")
    }
}