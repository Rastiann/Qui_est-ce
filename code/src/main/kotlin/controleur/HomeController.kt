package controleur

import javafx.scene.Parent
import state.Home
import vue.HomeVue

class HomeController: StateController<Home> {

    private val vue = HomeVue()

    override fun getVue(): Parent {
        return vue.root
    }

    override fun update(state: Home) {
        vue.updateParties(state.registeredGames) { gameId ->
            state.joinGame(gameId)
        }

        vue.joinBtn.setOnAction {
            state.joinGame(vue.codeField.text.toInt())
        }

        vue.createBtn.setOnAction {
            state.createNewGame()
        }
    }

}