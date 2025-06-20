package state

import info.but1.sae2025.QuiEstCeClient
import info.but1.sae2025.data.IdentificationJoueur
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.TimeUnit
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class PlayerCreationTest {

    private lateinit var apiClient: QuiEstCeClient
    private val apiThread = ApiThread()
    private lateinit var newStateQueue: ArrayBlockingQueue<NewState>
    private lateinit var stateChangeHandler: StateChangeHandler

    init {
        apiThread.start()
    }

    @BeforeEach
    fun setup() {
        Provider.setup()
    }

    @Test
    fun testPlayerCreation() {

        every { apiClient.requeteCreationJoueur("Name", "Firstname") } returns
                IdentificationJoueur(
                    10, "cle"
                )

        // create state
        val fromState = PlayerCreation(
            apiClient,
            apiThread,
            stateChangeHandler
        )

        // create player
        fromState.tryCreate("Name", "Firstname")

        // wait for new state to arrive
        val newState = newStateQueue.poll(10, TimeUnit.SECONDS)

        assertNotNull(newState) { "newsState must not be null" }
        assert(newState.state is Home) { "after player creation, newState must be of type Home" }
        assertNull(newState.error, "after player creation, error must not be null (${newState.error})")

        val selfPlayer = (newState.state as Home).selfPlayer
        assert(selfPlayer == ConnectedPlayer(
            "Name", "Firstname", 10, "cle"
        )
        ) { "wrong home selfPlayer" }
    }
}