package controleur

import vue.GameVue
import javafx.scene.Parent
import state.Home

class HomeController: StateController<Home> {

    private val vue = GameVue()

    override fun getVue(): Parent {
        TODO("Not yet implemented")
    }

    override fun update(state: Home) {
        TODO("Not yet implemented")
    }
}