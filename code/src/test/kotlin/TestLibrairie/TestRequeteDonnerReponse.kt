import info.but1.sae2025.QuiEstCeClient
import info.but1.sae2025.data.IdentificationJoueur
import info.but1.sae2025.exceptions.QuiEstCeException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class TestRequeteDonnerReponse {
    companion object {

        val client: QuiEstCeClient = QuiEstCeClient("172.26.69.145", 8080)
        val playerProvider = PlayerProvider(client)
        val joueur1 : IdentificationJoueur = playerProvider.get()
        val partieId = client.requeteCreationPartie(joueur1.id, joueur1.cle)
        val joueur2 : IdentificationJoueur = playerProvider.get()
        val gameTestHelper = GameStateHelper(client)

        @JvmStatic
        fun argumentsInvalidesProvider_requeteChoixPersonnage(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(-1, joueur1.id, joueur1.cle, "non"),                                // idPartie invalide
                Arguments.of(partieId, -1, joueur1.cle, "non"),                                           // idJoueur invalide
                Arguments.of(partieId, joueur1.id, "a".repeat(33), "non"),         // cle trop longue
                Arguments.of(partieId, joueur1.id, "a".repeat(31), "non"),        // cle trop courte
                Arguments.of(partieId, joueur1.id, joueur1.cle, ""),    // réponse vide
                Arguments.of(partieId, joueur1.id, joueur1.cle, "ouinon")                            // réponse invalide
            )
        }
    }

    @ParameterizedTest
    @MethodSource("argumentsInvalidesProvider_requeteChoixPersonnage")
    fun testRequeteChoixPersonnage_Exception(idPartie: Int, idJoueur: Int, cleJoueur: String, reponse : String) {
        assertThrows<IllegalArgumentException> {
            client.requeteDonnerReponse(
                idPartie,
                idJoueur,
                cleJoueur,
                reponse
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