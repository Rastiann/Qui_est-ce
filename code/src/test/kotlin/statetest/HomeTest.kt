package statetest

import ConnectedPlayer
import Player
import info.but1.sae2025.QuiEstCeClient
import info.but1.sae2025.data.ETAPE
import info.but1.sae2025.data.EtatPartie
import info.but1.sae2025.data.Joueur
import info.but1.sae2025.data.Personnage
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import state.*
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.TimeUnit
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class HomeTest {

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

        every {
            apiClient.requeteJoueurs()
        } returns listOf(2, 3)

        every { apiClient.requeteJoueur(2) } returns Joueur("2", "2")
        every { apiClient.requeteJoueur(3) } returns Joueur("3", "3")

        every { apiClient.requeteListePartiesCreees() } returns listOf(10)
        every { apiClient.requeteEtatPartie(10) } returns EtatPartie(
            2, -1, ETAPE.CREEE, 2, "", -1, ""
        )
    }

    @Test
    fun testHomeCreateGame() {

        // create state
        val prevState = Home(
            apiClient,
            apiThread,
            stateChangeHandler,
            ConnectedPlayer("Dujardin", "Jean", 1, "cle")
        )

        // wait for new state to arrive (periodic tasks)
        var newState = newStateQueue.poll(10, TimeUnit.SECONDS)

        assertNotNull(newState) { "newsState must not be null" }
        assert(newState.state is Home) { "after creating home, newState must be of type Home" }
        assertNull(newState.error, "after creating Home, error must not be null (${newState.error})")

        val newHome = newState.state as Home

        assert(newHome.registeredGames == listOf(
            CreatedGame(10, Player("2", "2", 2))
        )) { "wrong value of registeredGames : ${newHome.registeredGames}" }

        assert(newHome.registeredPlayers == listOf(
            Player("2", "2", 2),
            Player("3", "3", 3)
        )) { "wrong value of registeredPlayers : ${newHome.registeredPlayers}" }

        // create new game
        every { apiClient.requeteCreationPartie(1, "cle") } returns 11
        prevState.createNewGame()

        // wait for new state to arrive (new game)
        newState = newStateQueue.poll(10, TimeUnit.SECONDS)

        assertNotNull(newState) { "newsState must not be null" }
        assert(newState.state is GameInit) { "after creating home, newState must be of type GameInit" }
        assertNull(newState.error, "after creating Home, error must not be null (${newState.error})")

        assert((newState.state as GameInit).selfIsPlayer1) { "self player must be player 1" }
    }

    @Test
    fun testHomeGameJoin() {

        println("heuu ok ? ")

        // join game
        every { apiClient.requeteRejoindrePartie(10, 1, "cle") } returns EtatPartie(
            2, 1, ETAPE.CREEE, 2, "", 1, ""
        )

        every { apiClient.requeteEtatPartie(11) } returns EtatPartie(
            2, -1, ETAPE.CREEE, 2, "", -1, ""
        )

        val pers = Personnage("nom", "prenom", "url")
        val grid = listOf(
            listOf(pers, pers, pers, pers, pers, pers),
            listOf(pers, pers, pers, pers, pers, pers),
            listOf(pers, pers, pers, pers, pers, pers),
            listOf(pers, pers, pers, pers, pers, pers),
        )

        every { apiClient.requeteGrilleJoueur(10, 1) } returns grid
        every { apiClient.requeteGrilleJoueur(10, 2) } returns grid

        // create state
        Home(
            apiClient,
            apiThread,
            stateChangeHandler,
            ConnectedPlayer("Dujardin", "Jean", 1, "cle")
        )

        // wait for new state to arrive (periodic tasks)
        var newState = newStateQueue.poll(10, TimeUnit.SECONDS)
        assertNotNull(newState, "newState must not be null")

        // join game
        (newState.state as Home).joinGame(10)

        // wait for new state to arrive (new game)
        newState = newStateQueue.poll(10, TimeUnit.SECONDS)

        assertNotNull(newState) { "newsState must not be null" }
        assert(newState.state is GameInit) { "after creating home, newState must be of type GameInit" }
        assertNull(newState.error, "after creating Home, error must not be null (${newState.error})")

        assert(!(newState.state as GameInit).selfIsPlayer1) { "self player must not be player 1" }
    }

}