package controleur.game

import javafx.scene.Parent
import state.game.Lose
import state.game.Win
import vue.game.EndVue

class LoseController: GameController<Lose> {
    val vue = EndVue(EndVue.EndState.LOSE)

    override fun getVue(): Parent {
        return vue
    }

    override fun update(gameInitState: Lose) {
        vue.menuButton.setOnAction {
//            gameInitState.backHome()
        }
    }
}