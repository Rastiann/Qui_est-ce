package controleur

import vue.ConnectingVue
import javafx.scene.Parent
import state.Connecting

class ConnectingController: StateController<Connecting> {

    private val vue = ConnectingVue()

    override fun getVue(): Parent {
        return vue
    }

    override fun update(state: Connecting) {
        // nothing to do ...
    }

}