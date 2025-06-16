package controleur

import vue.GameVue
import javafx.scene.Parent
import state.Game

class GameController: StateController<Game> {

    private val vue = GameVue()

    override fun getVue(): Parent {
        TODO("Not yet implemented")
    }

    override fun update(state: Game) {
        TODO("Not yet implemented")
    }
}