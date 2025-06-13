package state

import ConnectedPlayer
import info.but1.sae2025.QuiEstCeClient
import state.gameinit.GameInitState

class GameInit(
    apiClient: QuiEstCeClient,
    apiThread: ApiThread,
    stateChangeHandler: StateChangeHandler,
    val selfPlayer: ConnectedPlayer,
    val selfIsPlayer1: Boolean,
    val gameId: Int,
    val gameInitState: GameInitState
): AppState(apiClient, apiThread, stateChangeHandler) {

    // constructor used to create a clone, changing gameInitState
    constructor(gameInit: GameInit, gameInitState: GameInitState):
            this(
                gameInit.apiClient,
                gameInit.apiThread,
                gameInit.stateChangeHandler,
                gameInit.selfPlayer,
                gameInit.selfIsPlayer1,
                gameInit.gameId,
                gameInitState
            )

    init {

        // to simplify, fill lateinit here
        gameInitState.attachToGameInit(
            this, apiClient, apiThread, stateChangeHandler
        )
    }

}