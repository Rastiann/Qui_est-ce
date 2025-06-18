package controleur

import javafx.scene.Parent
import state.Home
import vue.HomeVue
import vue.dialog.ErrorDialog

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
            try {
                val id = vue.codeField.text.toInt()
                state.joinGame(id)
            }catch (e: Throwable) {
                ErrorDialog(
                    "Mauvais format de partie",
                    "Id incorrect",
                    "Veuillez uniquement des chiffres"
                ).showCancelable()
            }
        }

        vue.createBtn.setOnAction {
            state.createNewGame()
        }
    }
}