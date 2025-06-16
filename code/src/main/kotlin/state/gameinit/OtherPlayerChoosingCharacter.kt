package state.gameinit

import grid.Grid
import grid.Person
import info.but1.sae2025.data.ETAPE
import state.Game
import state.game.PeerTurn
import state.game.UserTurn

class OtherPlayerChoosingCharacter(
    val otherPlayerId: Int,
    val selfGrid: Grid,
    val otherGrid: Grid,
    val persChoosen: Person
): GameInitState() {

    override fun onAttached() {

        // check every 500ms if another player has chosen his pers
        apiThread.setPeriodicTask(Runnable {
            try {

                // check
                val apiGameState = apiClient.requeteEtatPartie(gameInit.gameId)
                if (apiGameState.etape == ETAPE.INITIALISATION) {
                    return@Runnable
                }

                // safety : remove all periodic task to be sure
                // they don't change state after this change
                apiThread.setPeriodicTask(null)

                // send new state
                stateChangeHandler.handle(
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
                        persChoosen,
                        if (gameInit.selfIsPlayer1) {
                            UserTurn()
                        }else {
                            PeerTurn(null)
                        }
                    )
                )

            }catch(e: Throwable) {
                stateChangeHandler.handle(gameInit, e)
                apiThread.stop()
            }
        }, 500)
    }
}