package state.game

import info.but1.sae2025.data.ETAPE
import state.Game

class PeerTurn(
    val prevResponse: String?,
    val showWrongGuess: Boolean = false
): GameState() {

    override fun onAttached() {

        // check every 500ms if other player has chosen his question
        apiThread.setPeriodicTask(Runnable {

            try {

                // check if peer has chosen his question
                val apiGameState = apiClient.requeteEtatPartie(game.gameId)

                // check if peer has won
                if (apiGameState.etape == ETAPE.TERMINEE) {

                    // safety : remove all periodic task to be sure
                    // they don't change state after this change
                    apiThread.setPeriodicTask(null)

                    // send lose state
                    stateChangeHandler.handle(Game(game, Lose()))
                }

                if (apiGameState.etape != ETAPE.ATTENTE_REPONSE) {
                    return@Runnable
                }

                val question = apiGameState.questionCourante

                // safety : remove all periodic task to be sure
                // they don't change state after this change
                apiThread.setPeriodicTask(null)

                // set state
                stateChangeHandler.handle(Game(game, Answering(question)))

            }catch (e: Error) {
                stateChangeHandler.handle(game, e)
            }

        }, 500)
    }

}