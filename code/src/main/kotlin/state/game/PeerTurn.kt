package state.game

import info.but1.sae2025.data.ETAPE
import state.Game

class PeerTurn(
    val prevResponse: String?
): GameState() {

    override fun onAttached() {

        // check every 500ms if other player has chosen his question
        apiThread.setPeriodicTask(Runnable {

            try {

                // check if peer has chosen his question
                val apiGameState = apiClient.requeteEtatPartie(game.gameId)
                if (apiGameState.etape != ETAPE.ATTENTE_REPONSE) {
                    return@Runnable
                }

                val question = apiGameState.questionCourante

                // set state
                stateChangeHandler.handle(Game(game, Answering(question)))

            }catch (e: Error) {
                stateChangeHandler.handle(game, e)
            }
        }, 500)
    }

}