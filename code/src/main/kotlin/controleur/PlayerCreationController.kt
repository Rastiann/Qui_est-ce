package controleur

import ConnectedPlayer
import vue.PlayerCreationVue
import javafx.scene.Parent
import state.PlayerCreation

class PlayerCreationController(
    storedPlayer: ConnectedPlayer? = null
): StateController<PlayerCreation> {

    private val vue = PlayerCreationVue()

    override fun getVue(): Parent {
        TODO("Not yet implemented")
    }

    override fun update(state: PlayerCreation) {
        TODO("Not yet implemented")
    }
}