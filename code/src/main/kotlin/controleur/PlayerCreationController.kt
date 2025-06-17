package controleur

import ConnectedPlayer
import Player
import vue.PlayerCreationVue
import javafx.scene.Parent
import state.PlayerCreation

class PlayerCreationController(
    val storedPlayer: ConnectedPlayer? = null
): StateController<PlayerCreation> {

    private val vue = PlayerCreationVue(
        if (storedPlayer != null) {
            "${storedPlayer.firstName} ${storedPlayer.name}"
        } else { null }
    )

    override fun getVue(): Parent {
        return vue.root
    }

    override fun update(state: PlayerCreation) {

        vue.createButton.setOnAction {
            state.tryCreate(
                vue.lastNameField.text,
                vue.firstNameField.text
            )
        }

        vue.connectButton.setOnAction {
            if (storedPlayer == null) { return@setOnAction }

            state.tryUsePlayer(storedPlayer)
        }
    }
}