package state.game

import grid.Person
import state.Game
import state.Message

class UserTurn: GameState() {

    override fun onAttached() {}

    fun ask(question: String) {
        apiThread.executeImmediately {
            try {

                // ask question
                apiClient.requetePoserQuestion(
                    game.gameId,
                    game.selfPlayer.id,
                    game.selfPlayer.key,
                    question
                )

                // add message to discussion
                synchronized(discussionLock) {
                    discussion.add(Message(
                        question,
                        true
                    ))
                }

                // send new state
                stateChangeHandler.handle(Game(game, WaitingForResponse()))

            }catch(e: Throwable) {
                stateChangeHandler.handle(game, e)
                apiThread.stop()
            }
        }
    }

}