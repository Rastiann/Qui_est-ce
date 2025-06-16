package state.game

import state.Game

class Answering(
    question: String
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

                stateChangeHandler.handle(Game(game, UserTurn()))

            }catch(e: Throwable) {
                stateChangeHandler.handle(game, e)
                apiThread.stop()
            }
        }
    }

}