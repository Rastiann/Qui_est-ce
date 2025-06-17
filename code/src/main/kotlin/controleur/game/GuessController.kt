package controleur.game

import javafx.scene.Parent
import state.game.Guess
import vue.game.GuessVue

class GuessController: GameController<Guess> {

    val vue = GuessVue()

    override fun getVue(): Parent {
        return vue.root
    }

    override fun update(gameInitState: Guess) {
        vue.update(gameInitState.otherGrid, 1.0) { x, y ->

            // update vue
            gameInitState.otherGrid.setGrey(x, y, !gameInitState.otherGrid.grid[x][y].isGray)
            update(gameInitState)

        }

        vue.okBtn.setOnAction {

        }
    }

}