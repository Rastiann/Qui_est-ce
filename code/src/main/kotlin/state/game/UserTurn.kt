package state.game

import grid.Person
import state.Game

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

                // send new state
                stateChangeHandler.handle(Game(game, WaitingForResponse()))

            }catch(e: Error) {
                stateChangeHandler.handle(game, e)
            }
        }
    }

    fun skipTurn() {
        apiThread.executeImmediately {
            try {

                apiClient.requeteChercherEncore(
                    game.gameId,
                    game.selfPlayer.id,
                    game.selfPlayer.key
                )

                // send new state
                stateChangeHandler.handle(Game(game, PeerTurn(null)))

            }catch(e: Error) {
                stateChangeHandler.handle(game, e)
            }
        }
    }

    fun takeAGuess(pers: Person) {

        var persX = -1
        var persY = -1

        searchLoop@ for (x in 0 until 4) {
            for (y in 0 until 6) {
                if (game.otherGrid.grid[x][y].person == pers) {
                    persX = x
                    persY = y
                    break@searchLoop
                }
            }
        }

        if (persX == -1 || persY == -1) {
            throw Exception("Person is not in Grid")
        }

        apiThread.executeImmediately {

            try {

                val hasWin = apiClient.requeteTrouve(
                    game.gameId,
                    game.selfPlayer.id,
                    game.selfPlayer.key,
                    persX,
                    persY
                )

                val nextState = Game(
                    game,
                    if (hasWin) { Win() }else { PeerTurn(null, true) }
                )

                stateChangeHandler.handle(nextState, null)

            }catch(e: Error) {
                stateChangeHandler.handle(game, e)
            }
        }
    }

}