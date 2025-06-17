package controleur.game

import javafx.scene.Parent
import state.game.Win
import vue.game.EndVue

class WinController: GameController<Win> {

    val vue = EndVue(EndVue.EndState.WIN)

    override fun getVue(): Parent {
        return vue
    }

    override fun update(gameInitState: Win) {
        vue.menuButton.setOnAction {
//            gameInitState.backHome()
        }
    }
}