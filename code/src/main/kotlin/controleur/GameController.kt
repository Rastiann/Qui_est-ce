package controleur

import controleur.game.*
import vue.GameVue
import javafx.scene.Parent
import javafx.scene.control.Label
import state.Game
import state.game.*

class GameController: StateController<Game> {
    
    private var userTurnController: UserTurnController? = null
    private var waitingForResponseController: WaitingForResponseController? = null
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
        if (vue.rightSection.children.size == 1) {
            vue.rightSection.children.add(parent)
        }else {
            vue.rightSection.children[1] = parent
        }
    }
    
    override fun update(state: Game) {
        when (state.gameState){
            is UserTurn -> {
                
                // make sure currentVue is gameVue 
                currentVue = vue.root
                
                if (userTurnController == null) { userTurnController = UserTurnController() }
                val controller = userTurnController!!

                // controller update
                controller.update(state.gameState)
                
                // vue update
                setSubVue(controller.getVue())
                
            }
            is PeerTurn -> {

                // make sure currentVue is gameVue 
                vue.title = Label("Tour de l'adversaire")
                currentVue = vue.root
                
                if (peerTurnController == null) { peerTurnController = PeerTurnController() }
                val controller = peerTurnController!!

                // controller update
                controller.update(state.gameState)

                // vue update
                setSubVue(controller.getVue())
                
            }
            is Answering -> {

                // make sure currentVue is gameVue 
                currentVue = vue.root

                if (answeringController == null) { answeringController = AnsweringController() }
                val controller = answeringController!!

                // controller update
                controller.update(state.gameState)

                // vue update
                setSubVue(controller.getVue())
                
            }
            is WaitingForResponse -> {
                
                // make sure currentVue is gameVue 
                currentVue = vue.root

                if (waitingForResponseController == null) { 
                    waitingForResponseController = WaitingForResponseController() 
                }
                
                val controller = waitingForResponseController!!

                // controller update
                controller.update(state.gameState)

                // vue update
                setSubVue(controller.getVue())
                
            }
            is Win -> {

                if (winController == null) {
                    winController = WinController()
                }
                
                val controller = winController!!

                // controller update
                controller.update(state.gameState)

                // vue update
                currentVue = controller.getVue()
                
            }
            is Lose -> {

                if (loseController == null) {
                    loseController = LoseController()
                }

                val controller = loseController!!

                // controller update
                controller.update(state.gameState)

                // vue update
                currentVue = controller.getVue()

            }
            
        }

    }
}