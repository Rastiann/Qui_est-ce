package state.game

import info.but1.sae2025.QuiEstCeClient
import state.ApiThread
import state.Game
import state.StateChangeHandler

sealed class GameState {

    protected lateinit var game: Game
    protected lateinit var apiClient: QuiEstCeClient
    protected lateinit var apiThread: ApiThread
    protected lateinit var stateChangeHandler: StateChangeHandler

    abstract fun onAttached()

    fun attachToGame(
        game: Game,
        apiClient: QuiEstCeClient,
        apiThread: ApiThread,
        stateChangeHandler: StateChangeHandler
    ) {
        this.game = game
        this.apiClient = apiClient
        this.apiThread = apiThread
        this.stateChangeHandler = stateChangeHandler
        this.onAttached()
    }
}