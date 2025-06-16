package controleur

import vue.ConnectingVue
import javafx.scene.Parent
import state.Connecting

class ConnectingController: StateController<Connecting> {

    private val vue = ConnectingVue()

    override fun getVue(): Parent {
        TODO("return vue")
    }

    override fun update(state: Connecting) {
        TODO("Not yet implemented")
    }

}