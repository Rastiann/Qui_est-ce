package controleur.gameinit

import javafx.scene.Parent
import state.gameinit.GameInitState

interface GameInitController<T: GameInitState> {

    fun getVue(): Parent
    fun update(gameInitState: T)

}