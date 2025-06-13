package state.gameinit

import info.but1.sae2025.QuiEstCeClient
import state.ApiThread
import state.GameInit
import state.StateChangeHandler

sealed class GameInitState {

    protected lateinit var gameInit: GameInit
    protected lateinit var apiClient: QuiEstCeClient
    protected lateinit var apiThread: ApiThread
    protected lateinit var stateChangeHandler: StateChangeHandler

    abstract fun onAttached()

    fun attachToGameInit(
        gameInit: GameInit,
        apiClient: QuiEstCeClient,
        apiThread: ApiThread,
        stateChangeHandler: StateChangeHandler
    ) {
        this.gameInit = gameInit
        this.apiClient = apiClient
        this.apiThread = apiThread
        this.stateChangeHandler = stateChangeHandler
        this.onAttached()
    }
}