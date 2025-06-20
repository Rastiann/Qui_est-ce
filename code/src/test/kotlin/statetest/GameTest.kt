package statetest

import ConnectedPlayer
import Player
import grid.Grid
import grid.Person
import grid.PersonItem
import info.but1.sae2025.QuiEstCeClient
import info.but1.sae2025.data.ETAPE
import info.but1.sae2025.data.EtatPartie
import info.but1.sae2025.data.Joueur
import info.but1.sae2025.data.Personnage
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import state.*
import state.game.*
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.TimeUnit
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class GameTest {

    private lateinit var apiClient: QuiEstCeClient
    private val apiThread = ApiThread()
    private lateinit var newStateQueue: ArrayBlockingQueue<NewState>
    private lateinit var stateChangeHandler: StateChangeHandler

    private val choosenPersApi = Personnage("nom", "prenom", "url")
    private val choosenPers = Person(choosenPersApi.prenom, choosenPersApi.nom, choosenPersApi.url)

    private val apiGrid = listOf(
        listOf(choosenPersApi, choosenPersApi, choosenPersApi, choosenPersApi, choosenPersApi, choosenPersApi),
        listOf(choosenPersApi, choosenPersApi, choosenPersApi, choosenPersApi, choosenPersApi, choosenPersApi),
        listOf(choosenPersApi, choosenPersApi, choosenPersApi, choosenPersApi, choosenPersApi, choosenPersApi),
        listOf(choosenPersApi, choosenPersApi, choosenPersApi, choosenPersApi, choosenPersApi, choosenPersApi),
    )
    private val grid = Grid(apiGrid.map { array -> array.map { pers ->
        PersonItem(
            false,
            Person(pers.prenom, pers.nom, pers.url)
        )
    }})


    private val selfPlayer = ConnectedPlayer("Dujardin", "Jean", 1, "cle")
    private val otherPlayer = Player("2", "2", 2)

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

        every { apiClient.requeteGrilleJoueur(11, 1) } returns apiGrid
        every { apiClient.requeteGrilleJoueur(11, 2) } returns apiGrid

        every { apiClient.requeteJoueur(1) } returns Joueur(selfPlayer.name, selfPlayer.firstName)
        every { apiClient.requeteJoueur(2) } returns Joueur(otherPlayer.name, otherPlayer.firstName)
    }

    @Test
    fun userTurn() {

        every {
            apiClient.requetePoserQuestion(
                10,
                selfPlayer.id,
                selfPlayer.key,
                "Some question")
        } returns EtatPartie(1, 2, ETAPE.ATTENTE_REPONSE, 1, "Some Question", 2, "")

        val subState = UserTurn()
        Game(
            apiClient,
            apiThread,
            stateChangeHandler,
            selfPlayer,
            true,
            otherPlayer,
            10,
            grid,
            grid,
            choosenPers,
            subState
        )

        // ask
        subState.ask("Some question")

        // wait for new state to arrive
        val newState = newStateQueue.poll(10, TimeUnit.SECONDS)

        assertNotNull(newState) { "newsState must not be null" }
        assert(newState.state is Game) { "after asking question, newState must be of type Game" }
        assertNull(newState.error, "after asking question, error must not be null (${newState.error})")

        val actualState = newState.state as Game
        assert(actualState.gameState is WaitingForResponse) { "Game state must be WaitingForResponse" }
    }

    @Test
    fun waitingForResponse() {
        every {
            apiClient.requeteEtatPartie(10)
        } returns EtatPartie(1, 2, ETAPE.ATTENTE_REFLEXION, 1, "Some Question", 2, "")

        val subState = WaitingForResponse()
        Game(
            apiClient,
            apiThread,
            stateChangeHandler,
            selfPlayer,
            true,
            otherPlayer,
            10,
            grid,
            grid,
            choosenPers,
            subState
        )

        // wait for new state to arrive
        val newState = newStateQueue.poll(10, TimeUnit.SECONDS)

        assertNotNull(newState) { "newsState must not be null" }
        assert(newState.state is Game) { "after a question, newState must be of type Game" }
        assertNull(newState.error, "after a question, error must not be null (${newState.error})")

        val actualState = newState.state as Game
        assert(actualState.gameState is Guess) { "Game state must be WaitingForResponse" }
    }

    @Test
    fun answering() {

        every {
            apiClient.requeteDonnerReponse(
                10,
                selfPlayer.id,
                selfPlayer.key,
                "Some response")
        } returns EtatPartie(1, 2, ETAPE.ATTENTE_REFLEXION, 1, "Some question", 2, "Some response")

        val subState = Answering(
            "Some question"
        )

        Game(
            apiClient,
            apiThread,
            stateChangeHandler,
            selfPlayer,
            true,
            otherPlayer,
            10,
            grid,
            grid,
            choosenPers,
            subState
        )

        // answer
        subState.answer("Some response")

        // wait for new state to arrive
        val newState = newStateQueue.poll(10, TimeUnit.SECONDS)

        assertNotNull(newState) { "newsState must not be null" }
        assert(newState.state is Game) { "after answering question, newState must be of type Game" }
        assertNull(newState.error, "after answering question, error must not be null (${newState.error})")

        val actualState = newState.state as Game
        assert(actualState.gameState is PeerTurn) { "Game state must be PeerTurn" }
    }

    @Test
    fun guess() {
        every {
            apiClient.requeteChercherEncore(10, selfPlayer.id, selfPlayer.key)
        } returns EtatPartie(1, 2, ETAPE.ATTENTE_QUESTION, 2, "Some question", 1, "Some response")

        val subState = Guess()
        Game(
            apiClient,
            apiThread,
            stateChangeHandler,
            selfPlayer,
            true,
            otherPlayer,
            10,
            grid,
            grid,
            choosenPers,
            subState
        )

        // answer
        subState.pass()

        // wait for new state to arrive
        val newState = newStateQueue.poll(10, TimeUnit.SECONDS)

        assertNotNull(newState) { "newsState must not be null" }
        assert(newState.state is Game) { "after passing, newState must be of type Game" }
        assertNull(newState.error, "after passing, error must not be null (${newState.error})")

        val actualState = newState.state as Game
        assert(actualState.gameState is PeerTurn) { "Game state must be PeerTurn" }
    }

    @Test
    fun peerTurn() {
        every { apiClient.requeteEtatPartie(10) } returns
                EtatPartie(1, 2, ETAPE.ATTENTE_REPONSE, 2, "Some Question", 1, "")

        val subState = PeerTurn()
        Game(
            apiClient,
            apiThread,
            stateChangeHandler,
            selfPlayer,
            true,
            otherPlayer,
            10,
            grid,
            grid,
            choosenPers,
            subState
        )

        // wait for new state to arrive (periodic tasks)
        val newState = newStateQueue.poll(10, TimeUnit.SECONDS)

        assertNotNull(newState) { "newsState must not be null" }
        assert(newState.state is Game) { "newState must be of type Game" }
        assertNull(newState.error, "error must not be null (${newState.error})")

        val actualState = newState.state as Game
        assert(actualState.gameState is Answering) { "Game state must be Answering" }
    }


}