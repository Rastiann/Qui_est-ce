package controleur.gameinit

import handlers.ImgHandler
import javafx.scene.Parent
import state.gameinit.ChoosingCharacter
import vue.ChoosingCharacterVue

class ChoosingCharacterController: GameInitController<ChoosingCharacter> {

    private var vue = ChoosingCharacterVue()

    override fun getVue(): Parent {
        return vue.root
    }

    override fun update(gameInitState: ChoosingCharacter) {
        vue.update(
            gameInitState.selfGrid, { x, y ->
                gameInitState.choose(gameInitState.selfGrid.grid[x][y].person)
            },
            gameInitState.otherGrid, {_, _ -> }
        )
    }

}