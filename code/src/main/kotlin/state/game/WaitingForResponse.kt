package state.game

import info.but1.sae2025.data.ETAPE
import state.Game
import state.Message

class WaitingForResponse: GameState() {

    override fun onAttached() {

        // check every 500ms if other player has answered
        apiThread.setPeriodicTask(Runnable {

            try {

                // check if peer has chosen his question
                val apiGameState = apiClient.requeteEtatPartie(game.gameId)
                if (apiGameState.etape != ETAPE.ATTENTE_QUESTION) {
                    return@Runnable
                }

                val response = apiGameState.reponseCourante

                // add message to discussion
                synchronized(discussionLock) {
                    discussion.add(
                        Message(
                            response,
                            true
                        )
                    )
                }

                // safety : remove all periodic task to be sure
                // they don't change state after this change
                apiThread.setPeriodicTask(null)

                // set state
                stateChangeHandler.handle(Game(game, PeerTurn()))

            }catch (e: Throwable) {
                stateChangeHandler.handle(game, e)
                apiThread.stop()
            }

        }, 500)

    }
}