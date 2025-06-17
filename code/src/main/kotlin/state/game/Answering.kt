package state.game

import state.Game
import state.Message

class Answering(
    val question: String
): GameState() {

    override fun onAttached() { }

    fun answer(response: String) {
        apiThread.executeImmediately {
            try {

                apiClient.requeteDonnerReponse(
                    game.gameId,
                    game.selfPlayer.id,
                    game.selfPlayer.key,
                    response
                )

                // add message to discussion
                synchronized(discussionLock) {
                    discussion.add(
                        Message(
                            response,
                            true
                        )
                    )
                }

                stateChangeHandler.handle(Game(game, PeerTurn()))

            }catch(e: Throwable) {
                stateChangeHandler.handle(game, e)
                apiThread.stop()
            }
        }
    }

}