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
import state.game.WaitingForResponse
import state.gameinit.ChoosingCharacter
import state.gameinit.OtherPlayerChoosingCharacter
import state.gameinit.WaitingForOtherPlayer
import java.util.concurrent.TimeUnit
import kotlin.test.assertNotNull
import kotlin.test.assertNull

data class NewState(val state: AppState, val error: Error?)

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
            override fun handle(newState: AppState, error: Error?) {
                newStateQueue.put(NewState(newState, error))
            }
        }
    }

    @Test
    fun testConnection() {
        mockkConstructor(QuiEstCeClient::class)
        every { anyConstructed<QuiEstCeClient>().requeteEssai() } returns "ok"

        // create state
        Connecting("localhost", 8080, stateChangeHandler)

        // wait for new state to arrive
        val newState = newStateQueue.poll(10, TimeUnit.SECONDS)

        assertNotNull(newState) { "newsState must not be null" }
        assert(newState.state is PlayerCreation) { "after connecting, newState must be of type PlayerCreation" }
        assertNull(newState.error, "after connecting, newState must be null (${newState.error})")

        unmockkConstructor(QuiEstCeClient::class)
    }

    @Test
    fun testConnection2() {
        assertThrows<UnresolvedAddressException> {
            Connecting("some-none-existing-url", 8080, stateChangeHandler)
        }

        assertThrows<ConnectTimeoutException> {
            Connecting("127.0.1.67", 8080, stateChangeHandler)
        }
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
        fromState.tryCreate(Player("Name", "Firstname"))

        // wait for new state to arrive
        val newState = newStateQueue.poll(10, TimeUnit.SECONDS)

        assertNotNull(newState) { "newsState must not be null" }
        assert(newState.state is Home) { "after player creation, newState must be of type Home" }
        assertNull(newState.error, "after player creation, error must not be null (${newState.error})")

        val selfPlayer = (newState.state as Home).selfPlayer
        assert((newState.state as Home).selfPlayer == ConnectedPlayer(
            "Firstname", "Name", 10, "cle"
        )) { "wrong home selfPlayer" }
    }


    @Test
    fun testHomeCreateGame() {
        every {
            apiClient.requeteJoueurs()
        } returns listOf(2, 3)

        every { apiClient.requeteJoueur(2) } returns Joueur("2", "2")
        every { apiClient.requeteJoueur(3) } returns Joueur("3", "3")

        every { apiClient.requeteListePartiesCreees() } returns listOf(10)
        every { apiClient.requeteEtatPartie(10) } returns EtatPartie(
            2, -1, ETAPE.CREEE, 2, "", -1, ""
        )

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
            CreatedGame(10, Player("2", "2"))
        )) { "wrong value of registeredGames : ${newHome.registeredGames}" }

        assert(newHome.registeredPlayers == listOf(
            Player("2", "2"),
            Player("3", "3")
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
        every {
            apiClient.requeteJoueurs()
        } returns listOf(2, 3)

        every { apiClient.requeteJoueur(2) } returns Joueur("2", "2")
        every { apiClient.requeteJoueur(3) } returns Joueur("3", "3")

        every { apiClient.requeteListePartiesCreees() } returns listOf(10)
        every { apiClient.requeteEtatPartie(10) } returns EtatPartie(
            2, -1, ETAPE.CREEE, 2, "", -1, ""
        )

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

    @Test
    fun waitingForOtherPlayer() {

        every { apiClient.requeteEtatPartie(11) } returns EtatPartie(
            2, 1, ETAPE.INITIALISATION, 2, "", 1, ""
        )

        val pers = Personnage("nom", "prenom", "url")
        val grid = listOf(
            listOf(pers, pers, pers, pers, pers, pers),
            listOf(pers, pers, pers, pers, pers, pers),
            listOf(pers, pers, pers, pers, pers, pers),
            listOf(pers, pers, pers, pers, pers, pers),
        )

        every { apiClient.requeteGrilleJoueur(11, 1) } returns grid
        every { apiClient.requeteGrilleJoueur(11, 2) } returns grid

        // create state
        GameInit(
            apiClient,
            apiThread,
            stateChangeHandler,
            ConnectedPlayer("Dujardin", "Jean", 1, "cle"),
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

        val otherPlayerId = 2

        val pers = Personnage("nom", "prenom", "url")
        val grid = listOf(
            listOf(pers, pers, pers, pers, pers, pers),
            listOf(pers, pers, pers, pers, pers, pers),
            listOf(pers, pers, pers, pers, pers, pers),
            listOf(pers, pers, pers, pers, pers, pers),
        )

        // create state
        val prevState = GameInit(
            apiClient,
            apiThread,
            stateChangeHandler,
            ConnectedPlayer("Dujardin", "Jean", 1, "cle"),
            true,
            11,
            ChoosingCharacter(
                otherPlayerId,
                Grid(grid.map { array -> array.map { pers -> PersonItem(false, Person(pers.nom, pers.prenom, pers.url)) } }),
                Grid(grid.map { array -> array.map { pers -> PersonItem(false, Person(pers.nom, pers.prenom, pers.url)) } }),
            )
        )

        // choosing
        (prevState.gameInitState as ChoosingCharacter).choose(Person(pers.nom, pers.prenom, pers.url))

        // wait for new state to arrive
        val newState = newStateQueue.poll(10, TimeUnit.SECONDS)

        assertNotNull(newState, "newState must not be null")
        assert(newState.state is GameInit) { "new state must be GameInit" }

        val actualState = newState.state as GameInit
        assert(actualState.gameInitState is OtherPlayerChoosingCharacter) { "GameInit state must be OtherPlayerChoosingCharacter" }
    }
}