import info.but1.sae2025.QuiEstCeClient
import info.but1.sae2025.data.IdentificationJoueur
import info.but1.sae2025.exceptions.QuiEstCeException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertEquals

class TestRequeteTrouve {

    companion object {

        val client: QuiEstCeClient = QuiEstCeClient("localhost", 8080)
        val playerProvider = PlayerProvider(client)
        val joueur1 : IdentificationJoueur = playerProvider.get()
        val partieId = client.requeteCreationPartie(joueur1.id, joueur1.cle)
        val joueur2 : IdentificationJoueur = playerProvider.get()
        val gameTestHelper = GameStateHelper(client)

        @JvmStatic
        fun argumentsInvalidesProvider_requeteChoixPersonnage(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(-1, joueur1.id, joueur1.cle, 2, 2),               // idPartie invalide
                Arguments.of(partieId, -1, joueur1.cle, 2, 2),                         // idJoueur invalide
                Arguments.of(partieId, joueur1.id, "a".repeat(33), 2, 2),         // cle trop longue
                Arguments.of(partieId, joueur1.id, "a".repeat(31), 2, 2),        // cle trop courte
                Arguments.of(partieId, joueur1.id, joueur1.cle, 2, -1),             // colonne negatif
                Arguments.of(partieId, joueur1.id, joueur1.cle, 2, 6),             // colonne out of range
                Arguments.of(partieId, joueur1.id, joueur1.cle, -1, 2),           // ligne negatif
                Arguments.of(partieId, joueur1.id, joueur1.cle, 4, 2)            // ligne out of range
            )
        }
    }

    @ParameterizedTest
    @MethodSource("argumentsInvalidesProvider_requeteChoixPersonnage")
    fun testRequeteChoixPersonnage_Exception(idPartie: Int, idJoueur: Int, cleJoueur: String, ligne : Int, colonne : Int) {
        assertThrows<IllegalArgumentException> {
            client.requeteTrouve(
                idPartie,
                idJoueur,
                cleJoueur,
                ligne,
                colonne
            )
        }
    }

    @Test
    fun testRequeteTrouve() {

        // La réponse ici est toujours 2 2

        // Bon guess
        var partieId = client.requeteCreationPartie(joueur1.id, joueur1.cle)
        gameTestHelper.advanceGameTo(joueur1, joueur2, partieId, GameStateHelper.GameStep.WAIT_REFLEXION)
        var trouve = client.requeteTrouve(partieId, joueur1.id, joueur1.cle, 2, 2)

        assertEquals(true, trouve, "la fonction devrait renvoyer true car le guess est bon")

        // Mauvais guess
        partieId = client.requeteCreationPartie(joueur1.id, joueur1.cle)
        gameTestHelper.advanceGameTo(joueur1, joueur2, partieId, GameStateHelper.GameStep.WAIT_REFLEXION)
        trouve = client.requeteTrouve(partieId, joueur1.id, joueur1.cle, 1, 2)

        assertEquals(false, trouve, "la fonction devrait renvoyer true car le guess est bon")

        partieId = client.requeteCreationPartie(joueur1.id, joueur1.cle)
        gameTestHelper.advanceGameTo(joueur1, joueur2, partieId, GameStateHelper.GameStep.INITIALISATION)

        // Guess au mauvais moment
        assertThrows<QuiEstCeException> {
            client.requeteTrouve(partieId, joueur1.id, joueur1.cle, 2, 2)

        }

    }

}