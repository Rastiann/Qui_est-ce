package controleur

import vue.GameInitVue
import javafx.scene.Parent
import state.GameInit

class GameInitController: StateController<GameInit> {

    private val vue = GameInitVue()

    override fun getVue(): Parent {
        TODO("Not yet implemented")
    }

    override fun update(state: GameInit) {
        TODO("Not yet implemented")
    }
}