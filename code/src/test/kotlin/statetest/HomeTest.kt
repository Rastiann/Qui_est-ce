package state

import info.but1.sae2025.data.ETAPE
import info.but1.sae2025.data.EtatPartie
import info.but1.sae2025.data.Joueur
import info.but1.sae2025.data.Personnage
import io.mockk.every
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.concurrent.TimeUnit
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class HomeTest {

    @BeforeEach
    fun setup() {
        Provider.setup()
    }

    @Test
    fun testHomeCreateGame() {
        every {
            Provider.apiClient.requeteJoueurs()
        } returns listOf(2, 3)

        every { Provider.apiClient.requeteJoueur(2) } returns Joueur("2", "2")
        every { Provider.apiClient.requeteJoueur(3) } returns Joueur("3", "3")

        every { Provider.apiClient.requeteListePartiesCreees() } returns listOf(10)
        every { Provider.apiClient.requeteEtatPartie(10) } returns EtatPartie(
            2, -1, ETAPE.CREEE, 2, "", -1, ""
        )

        // create state
        val prevState = Home(
            Provider.apiClient,
            Provider.apiThread,
            Provider.stateChangeHandler,
            ConnectedPlayer("Dujardin", "Jean", 1, "cle")
        )

        // wait for new state to arrive (periodic tasks)
        var newState = Provider.newStateQueue.poll(10, TimeUnit.SECONDS)

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

        every { Provider.apiClient.requeteCreationPartie(1, "cle") } returns 11
        prevState.createNewGame()

        // wait for new state to arrive (new game)
        newState = Provider.newStateQueue.poll(10, TimeUnit.SECONDS)

        assertNotNull(newState) { "newsState must not be null" }
        assert(newState.state is GameInit) { "after creating home, newState must be of type GameInit" }
        assertNull(newState.error, "after creating Home, error must not be null (${newState.error})")

        assert((newState.state as GameInit).selfIsPlayer1) { "self player must be player 1" }
    }

    @Test
    fun testHomeGameJoin() {
        every {
            Provider.apiClient.requeteJoueurs()
        } returns listOf(2, 3)

        every { Provider.apiClient.requeteJoueur(2) } returns Joueur("2", "2")
        every { Provider.apiClient.requeteJoueur(3) } returns Joueur("3", "3")

        every { Provider.apiClient.requeteListePartiesCreees() } returns listOf(10)
        every { Provider.apiClient.requeteEtatPartie(10) } returns EtatPartie(
            2, -1, ETAPE.CREEE, 2, "", -1, ""
        )

        // join game
        every { Provider.apiClient.requeteRejoindrePartie(10, 1, "cle") } returns EtatPartie(
            2, 1, ETAPE.CREEE, 2, "", 1, ""
        )

        every { Provider.apiClient.requeteEtatPartie(11) } returns EtatPartie(
            2, -1, ETAPE.CREEE, 2, "", -1, ""
        )

        val pers = Personnage("nom", "prenom", "url")
        val grid = listOf(
            listOf(pers, pers, pers, pers, pers, pers),
            listOf(pers, pers, pers, pers, pers, pers),
            listOf(pers, pers, pers, pers, pers, pers),
            listOf(pers, pers, pers, pers, pers, pers),
        )

        every { Provider.apiClient.requeteGrilleJoueur(10, 1) } returns grid
        every { Provider.apiClient.requeteGrilleJoueur(10, 2) } returns grid



        // create state
        Home(
            Provider.apiClient,
            Provider.apiThread,
            Provider.stateChangeHandler,
            ConnectedPlayer("Dujardin", "Jean", 1, "cle")
        )

        // wait for new state to arrive (periodic tasks)
        var newState = Provider.newStateQueue.poll(10, TimeUnit.SECONDS)
        assertNotNull(newState, "newState must not be null")

        // join game
        (newState.state as Home).joinGame(10)

        // wait for new state to arrive (new game)
        newState = Provider.newStateQueue.poll(10, TimeUnit.SECONDS)

        assertNotNull(newState) { "newsState must not be null" }
        assert(newState.state is GameInit) { "after creating home, newState must be of type GameInit" }
        assertNull(newState.error, "after creating Home, error must not be null (${newState.error})")

        assert(!(newState.state as GameInit).selfIsPlayer1) { "self player must not be player 1" }
    }

}