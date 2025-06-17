package controleur.game

import javafx.scene.Parent
import state.game.GameState

interface GameController<T: GameState> {

    fun getVue(): Parent
    fun update(gameInitState: T)

}