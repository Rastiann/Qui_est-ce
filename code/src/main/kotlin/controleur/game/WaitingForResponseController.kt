package controleur.game

import javafx.scene.Parent
import state.game.WaitingForResponse
import vue.game.WaitingForResponseVue

class WaitingForResponseController: GameController<WaitingForResponse> {

    private val vue = WaitingForResponseVue()

    override fun getVue(): Parent {
        return vue
    }

    override fun update(gameInitState: WaitingForResponse) {}
}