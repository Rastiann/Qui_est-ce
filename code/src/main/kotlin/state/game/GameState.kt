package state.game

import ConnectedPlayer
import grid.Grid
import grid.Person
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
    lateinit var selfPlayer: ConnectedPlayer
    lateinit var selfGrid: Grid
    lateinit var otherGrid: Grid
    lateinit var discussionLock: Any
    lateinit var discussion: MutableList<Message>

    abstract fun onAttached()

    fun attachToGame(
        game: Game,
        apiClient: QuiEstCeClient,
        apiThread: ApiThread,
        stateChangeHandler: StateChangeHandler,
        selfPlayer: ConnectedPlayer,
        selfGrid: Grid,
        otherGrid: Grid,
        discussionLock: Any,
        discussion: MutableList<Message>
    ) {
        this.game = game
        this.apiClient = apiClient
        this.apiThread = apiThread
        this.stateChangeHandler = stateChangeHandler
        this.selfPlayer = selfPlayer
        this.selfGrid = selfGrid
        this.otherGrid = otherGrid
        this.discussionLock = discussionLock
        this.discussion = discussion
        this.onAttached()
    }
}