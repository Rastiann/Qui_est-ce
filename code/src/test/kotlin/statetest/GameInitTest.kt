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
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import state.*
import state.game.UserTurn
import state.gameinit.ChoosingCharacter
import state.gameinit.OtherPlayerChoosingCharacter
import state.gameinit.WaitingForOtherPlayer
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.TimeUnit
import kotlin.test.assertNotNull

class GameInitTest {

    private lateinit var apiClient: QuiEstCeClient
    private val apiThread = ApiThread()
    private lateinit var newStateQueue: ArrayBlockingQueue<NewState>
    private lateinit var stateChangeHandler: StateChangeHandler

    private val choosenPers = Personnage("nom", "prenom", "url")
    private val apiGrid = listOf(
        listOf(choosenPers, choosenPers, choosenPers, choosenPers, choosenPers, choosenPers),
        listOf(choosenPers, choosenPers, choosenPers, choosenPers, choosenPers, choosenPers),
        listOf(choosenPers, choosenPers, choosenPers, choosenPers, choosenPers, choosenPers),
        listOf(choosenPers, choosenPers, choosenPers, choosenPers, choosenPers, choosenPers),
    )

    private val grid = Grid(apiGrid.map { array -> array.map { pers ->
        PersonItem(
            false,
            Person(pers.prenom, pers.nom, pers.url)
        )
    }})

    private val connectedPlayer = ConnectedPlayer("Dujardin", "Jean", 1, "cle")
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

        every { apiClient.requeteJoueur(1) } returns Joueur(connectedPlayer.name, connectedPlayer.firstName)
        every { apiClient.requeteJoueur(2) } returns Joueur(otherPlayer.name, otherPlayer.firstName)
    }

    @Test
    fun waitingForOtherPlayer() {

        every { apiClient.requeteEtatPartie(11) } returns EtatPartie(
            2, 1, ETAPE.INITIALISATION, 2, "", 1, ""
        )

        // create state
        GameInit(
            apiClient,
            apiThread,
            stateChangeHandler,
            connectedPlayer,
            true,
            11,
            WaitingForOtherPlayer()
        )

        // wait for new state to arrive (periodic tasks)
        val newState = newStateQueue.poll(10, TimeUnit.SECONDS)

        assertNotNull(newState, "newState must not be null")
        assert(newState.state is GameInit) { "new state must be GameInit" }

        val actualState = newState.state as GameInit
        assert(actualState.gameInitState is ChoosingCharacter) { "GameInit state must be ChoosingCharacter" }
    }

    @Test
    fun choosingCharacter() {

        every { apiClient.requeteChoixPersonnage(11, 1, "cle", 0, 0) } returns EtatPartie(
            2, 1, ETAPE.INITIALISATION, 2, "", 1, ""
        )

        // create state
        val prevState = GameInit(
            apiClient,
            apiThread,
            stateChangeHandler,
            connectedPlayer,
            true,
            11,
            ChoosingCharacter(
                otherPlayer,
                grid,
                grid,
            )
        )

        // choosing
        (prevState.gameInitState as ChoosingCharacter).choose(
            Person(choosenPers.prenom, choosenPers.nom, choosenPers.url)
        )

        // wait for new state to arrive
        val newState = newStateQueue.poll(10, TimeUnit.SECONDS)

        assertNotNull(newState, "newState must not be null")
        assert(newState.state is GameInit) { "new state must be GameInit" }

        val actualState = newState.state as GameInit
        assert(actualState.gameInitState is OtherPlayerChoosingCharacter) { "GameInit state must be OtherPlayerChoosingCharacter" }
    }

    @Test
    fun otherPlayerChoosingCharacter() {

        // when other player choose character, newState is ATTENTE_QUESTION
        every { apiClient.requeteEtatPartie(11) } returns EtatPartie(
            1, 2, ETAPE.ATTENTE_QUESTION, 1, "", 2, ""
        )

        // create state
        GameInit(
            apiClient,
            apiThread,
            stateChangeHandler,
            connectedPlayer,
            true,
            11,
            OtherPlayerChoosingCharacter(
                otherPlayer,
                grid,
                grid,
                Person(choosenPers.prenom, choosenPers.nom, choosenPers.url)
            )
        )

        // wait for new state to arrive (periodic tasks)
        val newState = newStateQueue.poll(10, TimeUnit.SECONDS)

        assertNotNull(newState, "newState must not be null")
        assert(newState.state is Game) { "new state must be Game" }

        val actualState = newState.state as Game
        assert(actualState.gameState is UserTurn) { "Game state must be UserTurn" }
    }

}