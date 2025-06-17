package controleur

import controleur.game.*
import vue.GameVue
import javafx.scene.Parent
import state.Game
import state.game.*

class GameController: StateController<Game> {
    
    private var userTurnController: UserTurnController? = null
    private var waitingForResponseController: WaitingForResponseController? = null
    private var guessController: GuessController? = null
    private var peerTurnController: PeerTurnController? = null
    private var answeringController: AnsweringController? = null
    private var loseController: LoseController? = null
    private var winController: WinController? = null

    private val vue = GameVue()
    private var currentVue: Parent = vue.root

    override fun getVue(): Parent {
        return currentVue
    }

    private fun setSubVue(parent: Parent) {
        if (vue.main.children.size == 1) {
            vue.main.children.add(parent)
        }else {
            vue.main.children[1] = parent
        }
    }
    
    override fun update(state: Game) {

        // game vue
        // update top label
        vue.root.topLabel.text = "Partie avec ${state.otherPlayer.firstName} ${state.otherPlayer.name}"
        vue.root.updateDiscussion(state.discussion)
        vue.root.setBottomPers(state.persChoosen)
        currentVue = vue.root

        when (state.gameState){
            is UserTurn -> {

                vue.title.text = "Posez votre question ..."

                if (userTurnController == null) { userTurnController = UserTurnController() }
                val controller = userTurnController!!

                // controller update
                controller.update(state.gameState)
                
                // vue update
                setSubVue(controller.getVue())
                
            }
            is WaitingForResponse -> {

                vue.title.text = "Attends de la réponse ..."

                if (waitingForResponseController == null) {
                    waitingForResponseController = WaitingForResponseController()
                }

                val controller = waitingForResponseController!!

                // controller update
                controller.update(state.gameState)

                // vue update
                setSubVue(controller.getVue())

            }
            is Guess -> {

                vue.title.text = "Grisez vos personnages"

                if (guessController == null) { guessController = GuessController() }
                val controller = guessController!!

                // controller update
                controller.update(state.gameState)

                // vue update
                setSubVue(controller.getVue())

            }
            is PeerTurn -> {

                vue.title.text = "Tour de l'adversaire"
                
                if (peerTurnController == null) { peerTurnController = PeerTurnController() }
                val controller = peerTurnController!!

                // controller update
                controller.update(state.gameState)

                // vue update
                setSubVue(controller.getVue())
                
            }
            is Answering -> {

                vue.title.text = "Répondre à la question"

                if (answeringController == null) { answeringController = AnsweringController() }
                val controller = answeringController!!

                // controller update
                controller.update(state.gameState)

                // vue update
                setSubVue(controller.getVue())
                
            }
            is Win -> {

                if (winController == null) { winController = WinController() }
                val controller = winController!!

                // controller update
                controller.update(state.gameState)

                // vue update
                currentVue = controller.getVue()
                
            }
            is Lose -> {

                if (loseController == null) { loseController = LoseController() }
                val controller = loseController!!

                // controller update
                controller.update(state.gameState)

                // vue update
                currentVue = controller.getVue()

            }
            
        }

    }
}