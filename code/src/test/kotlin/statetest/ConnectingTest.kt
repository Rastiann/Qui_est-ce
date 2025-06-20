package state

import info.but1.sae2025.QuiEstCeClient
import io.ktor.client.network.sockets.*
import io.ktor.util.network.*
import org.junit.jupiter.api.Test
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import java.util.concurrent.TimeUnit
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ConnectingTest {

    @BeforeEach
    fun setup() {
        Provider.setup()
    }

    @Test
    fun testConnection() {
        mockkConstructor(QuiEstCeClient::class)
        every { anyConstructed<QuiEstCeClient>().requeteEssai() } returns "ok"

        // create state
        Connecting("localhost", 8080, Provider.stateChangeHandler)

        // wait for new state to arrive
        val newState = Provider.newStateQueue.poll(10, TimeUnit.SECONDS)

        assertNotNull(newState) { "newsState must not be null" }
        assert(newState.state is PlayerCreation) { "after connecting, newState must be of type PlayerCreation" }
        assertNull(newState.error, "after connecting, newState must be null (${newState.error})")

        unmockkConstructor(QuiEstCeClient::class)
    }

    @Test
    fun testConnection2() {
        assertThrows<UnresolvedAddressException> {
            Connecting("some-none-existing-url", 8080, Provider.stateChangeHandler)
        }

        assertThrows<ConnectTimeoutException> {
            Connecting("127.0.1.67", 8080, Provider.stateChangeHandler)
        }
    }

}