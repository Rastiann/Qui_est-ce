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

class TestRequetePoserQuestion {
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
                Arguments.of(-1, joueur1.id, joueur1.cle, "Est ce qu'il est roux"),               // idPartie invalide
                Arguments.of(partieId, -1, joueur1.cle, "Est ce qu'il est roux"),                         // idJoueur invalide
                Arguments.of(partieId, joueur1.id, "a".repeat(33), "Est ce qu'il est roux"),         // cle trop longue
                Arguments.of(partieId, joueur1.id, "a".repeat(31), "Est ce qu'il est roux"),        // cle trop courte
                Arguments.of(partieId, joueur1.id, joueur1.cle, ""),             // colonne negatif
            )
        }
    }

    @ParameterizedTest
    @MethodSource("argumentsInvalidesProvider_requeteChoixPersonnage")
    fun testRequeteChoixPersonnage_Exception(idPartie: Int, idJoueur: Int, cleJoueur: String, question : String) {
        assertThrows<IllegalArgumentException> {
            client.requetePoserQuestion(
                idPartie,
                idJoueur,
                cleJoueur,
                question
            )
        }
    }

    @Test
    fun testRequeteTrouve() {

        // ********** Rajouter des tests ou je vérifie bien que la question est dans le state et
        // que l'étape de la partie est WAIT_REFLEXION

        var partieId = client.requeteCreationPartie(joueur1.id, joueur1.cle)
        gameTestHelper.advanceGameTo(joueur1, joueur2, partieId, GameStateHelper.GameStep.INITIALISATION)

        // Question au mauvais moment
        assertThrows<QuiEstCeException> {
            client.requetePoserQuestion(partieId, joueur1.id, joueur1.cle, "Est ce qu'il est roux")

        }

    }
}