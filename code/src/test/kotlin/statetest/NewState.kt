package statetest

import ConnectedPlayer
import Player
import grid.Grid
import grid.Person
import grid.PersonItem
import info.but1.sae2025.QuiEstCeClient
import info.but1.sae2025.data.*
import io.ktor.client.network.sockets.*
import io.ktor.util.network.*
import org.junit.jupiter.api.Test
import java.util.concurrent.ArrayBlockingQueue
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import state.*
import state.game.UserTurn
import state.gameinit.ChoosingCharacter
import state.gameinit.OtherPlayerChoosingCharacter
import state.gameinit.WaitingForOtherPlayer
import java.util.concurrent.TimeUnit
import kotlin.test.assertNotNull
import kotlin.test.assertNull

data class NewState(val state: AppState, val error: Throwable?)

class State {

    private lateinit var apiClient: QuiEstCeClient
    private val apiThread = ApiThread()
    private lateinit var newStateQueue: ArrayBlockingQueue<NewState>
    private lateinit var stateChangeHandler: StateChangeHandler

    init {
        apiThread.start()
    }

    @BeforeEach
    fun setup() {
        apiClient = mockk()
        apiThread.setPeriodicTask(null)
        newStateQueue = ArrayBlockingQueue<NewState>(1)
        stateChangeHandler = object : StateChangeHandler {
            override fun handle(newState: AppState, error: Throwable?) {
                newStateQueue.put(NewState(newState, error))
            }
        }
    }



}