package controleur.game

import grid.Person
import javafx.scene.Parent
import state.game.Guess
import vue.dialog.Validation
import vue.game.GuessVue

class GuessController: GameController<Guess> {

    val vue = GuessVue()

    override fun getVue(): Parent {
        return vue.root
    }

    override fun update(gameInitState: Guess) {
        vue.update(gameInitState.otherGrid, 1.0) { x, y ->

            // update vue
            println("set gray : $x, $y")
            gameInitState.otherGrid.setGrey(x, y, !gameInitState.otherGrid.grid[x][y].isGray)
            update(gameInitState)

        }

        vue.okBtn.setOnAction {

            var person: Person = gameInitState.otherGrid.grid[0][0].person
            var numberOfNonGreyCase = 0

            big_loop@ for (x in 0 until gameInitState.otherGrid.grid.size) {
                for (y in 0 until gameInitState.otherGrid.grid[x].size) {

                    if (!gameInitState.otherGrid.grid[x][y].isGray) {
                        if (numberOfNonGreyCase > 1) { break@big_loop }
                        numberOfNonGreyCase ++
                        person = gameInitState.otherGrid.grid[x][y].person
                    }

                }
            }

            if (numberOfNonGreyCase == 1) {
                val trySend = Validation().show()
                if (trySend) {
                    gameInitState.guess(person)
                }

                return@setOnAction
            }

            gameInitState.pass()
        }
    }

}