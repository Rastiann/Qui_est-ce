package state.gameinit

import grid.Grid
import grid.Person
import info.but1.sae2025.data.ETAPE
import state.Game
import state.GameInit
import state.game.PeerTurn
import state.game.UserTurn

class ChoosingCharacter(
    val otherPlayerId: Int,
    val selfGrid: Grid,
    val otherGrid: Grid
): GameInitState() {

    override fun onAttached() {}

    fun choose(pers: Person) {

        var persX = -1
        var persY = -1

        searchLoop@ for (x in 0 until 4) {
            for (y in 0 until 6) {
                if (selfGrid.grid[x][y].person == pers) {
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

                val apiGameState = apiClient.requeteChoixPersonnage(
                    gameInit.gameId,
                    gameInit.selfPlayer.id,
                    gameInit.selfPlayer.key,
                    persX, persY
                )

                // if other player has already chosen his pers,
                // go directly to game, otherwise, wait for player to choose
                val newGameState = if (apiGameState.etape == ETAPE.INITIALISATION) {

                    GameInit(
                        gameInit,
                        OtherPlayerChoosingCharacter(
                            otherPlayerId,
                            selfGrid,
                            otherGrid,
                            pers
                        )
                    )

                }else {

                    Game(
                        apiClient,
                        apiThread,
                        stateChangeHandler,
                        gameInit.selfPlayer,
                        gameInit.selfIsPlayer1,
                        otherPlayerId,
                        gameInit.gameId,
                        selfGrid,
                        otherGrid,
                        pers,
                        if (gameInit.selfIsPlayer1) {
                            UserTurn()
                        }else {
                            PeerTurn(null)
                        }
                    )

                }

                // send new state
                stateChangeHandler.handle(newGameState)

            }catch(e: Throwable) {
                stateChangeHandler.handle(gameInit, e)
                apiThread.stop()
            }
        }
    }

}