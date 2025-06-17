package state.game

import ConnectedPlayer
import info.but1.sae2025.QuiEstCeClient
import state.ApiThread
import state.Game
import state.Message
import state.StateChangeHandler

sealed class GameState {

    protected lateinit var game: Game
    protected lateinit var apiClient: QuiEstCeClient
    protected lateinit var apiThread: ApiThread
    protected lateinit var stateChangeHandler: StateChangeHandler
    protected lateinit var selfPlayer: ConnectedPlayer
    protected lateinit var discussionLock: Any
    protected lateinit var discussion: MutableList<Message>

    abstract fun onAttached()

    fun attachToGame(
        game: Game,
        apiClient: QuiEstCeClient,
        apiThread: ApiThread,
        stateChangeHandler: StateChangeHandler,
        selfPlayer: ConnectedPlayer,
        discussionLock: Any,
        discussion: MutableList<Message>
    ) {
        this.game = game
        this.apiClient = apiClient
        this.apiThread = apiThread
        this.stateChangeHandler = stateChangeHandler
        this.selfPlayer = selfPlayer
        this.discussionLock = discussionLock
        this.discussion = discussion
        this.onAttached()
    }
}