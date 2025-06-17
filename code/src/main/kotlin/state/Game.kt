package state

import ConnectedPlayer
import Player
import grid.Grid
import grid.Person
import info.but1.sae2025.QuiEstCeClient
import state.game.GameState

data class Message(
    val message: String,
    val isSelf: Boolean
)

class Game(
    apiClient: QuiEstCeClient,
    apiThread: ApiThread,
    stateChangeHandler: StateChangeHandler,
    val selfPlayer: ConnectedPlayer,
    val selfIsPlayer1: Boolean,
    val otherPlayer: Player,
    val gameId: Int,
    val selfGrid: Grid,
    val otherGrid: Grid,
    val persChoosen: Person,
    val gameState: GameState,
    discussion: MutableList<Message> = mutableListOf()
): AppState(apiClient, apiThread, stateChangeHandler) {

    private val discussionLock = Any()
    private var _discussion = discussion

    val discussion
        get() = synchronized(discussionLock) { _discussion.toList() }

    // constructor used to create a clone, changing gameState
    constructor(game: Game, gameState: GameState):
            this(
                game.apiClient,
                game.apiThread,
                game.stateChangeHandler,
                game.selfPlayer,
                game.selfIsPlayer1,
                game.otherPlayer,
                game.gameId,
                game.selfGrid,
                game.otherGrid,
                game.persChoosen,
                gameState,
                game._discussion
            )

    init {

        // to simplify, fill lateinit here
        gameState.attachToGame(
            this,
            apiClient,
            apiThread,
            stateChangeHandler,
            selfPlayer,
            selfGrid,
            otherGrid,
            discussionLock,
            discussion
        )
    }

}