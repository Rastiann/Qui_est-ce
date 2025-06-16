package controleur

import javafx.scene.Parent
import state.AppState

interface StateController<T: AppState> {
    fun getVue(): Parent
    fun update(state: T)
}