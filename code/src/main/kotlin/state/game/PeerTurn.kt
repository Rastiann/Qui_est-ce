package state.game

import info.but1.sae2025.data.ETAPE
import state.Game
import state.Message

class PeerTurn(
    val showWrongGuess: Boolean = false
): GameState() {

    override fun onAttached() {

        // check every 500ms if other player has chosen his question
        apiThread.setPeriodicTask({

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

                if (apiGameState.etape == ETAPE.ATTENTE_REPONSE) {

                    val question = apiGameState.questionCourante

                    // add message to discussion
                    synchronized(discussionLock) {
                        discussion.add(
                            Message(
                                question,
                                false
                            )
                        )
                    }

                    // safety : remove all periodic task to be sure
                    // they don't change state after this change
                    apiThread.setPeriodicTask(null)

                    // set state
                    stateChangeHandler.handle(Game(game, Answering(question)))

                }

                if (apiGameState.etape == ETAPE.ATTENTE_QUESTION && apiGameState.idJoueurQuestionCourante == selfPlayer.id) {
                    // safety : remove all periodic task to be sure
                    // they don't change state after this change
                    apiThread.setPeriodicTask(null)

                    // set state
                    stateChangeHandler.handle(Game(game, UserTurn()))
                }

            }catch (e: Throwable) {
                stateChangeHandler.handle(game, e)
                apiThread.stop()
            }

        }, 500)
    }

}