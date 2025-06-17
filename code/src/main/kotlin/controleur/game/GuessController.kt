package controleur.game

import javafx.scene.Parent
import state.game.Guess
import vue.game.GuessVue

class GuessController: GameController<Guess> {

    val vue = GuessVue()

    override fun getVue(): Parent {
        return vue
    }

    override fun update(gameInitState: Guess) {

    }

}