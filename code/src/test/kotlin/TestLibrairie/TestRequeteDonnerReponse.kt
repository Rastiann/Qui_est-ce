import info.but1.sae2025.QuiEstCeClient
import info.but1.sae2025.exceptions.QuiEstCeException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertEquals

class TestRequeteDonnerReponse {

    companion object {
        val client: QuiEstCeClient = ConfigTest.client
        val joueur1 = ConfigTest.joueur1
        val partieId = client.requeteCreationPartie(joueur1.id, joueur1.cle)
        val joueur2 = ConfigTest.joueur2
        val gameTestHelper = ConfigTest.gameTestHelper

        @JvmStatic
        fun argumentsInvalidesProvider_requeteChoixPersonnage(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(-1, joueur1.id, joueur1.cle, "non"),  // idPartie invalide
                Arguments.of(partieId, -1, joueur1.cle, "non"),  // idJoueur invalide
                Arguments.of(partieId, joueur1.id, "a".repeat(33), "non"), // cle trop longue
                Arguments.of(partieId, joueur1.id, "a".repeat(31), "non"), // cle trop courte
                Arguments.of(partieId, joueur1.id, joueur1.cle, ""), // réponse vide
                Arguments.of(partieId, joueur1.id, joueur1.cle, "ouinon") // réponse invalide
            )
        }
    }

    @ParameterizedTest
    @MethodSource("argumentsInvalidesProvider_requeteChoixPersonnage")
    fun testRequeteDonnerReponse_Exception(idPartie: Int, idJoueur: Int, cleJoueur: String, reponse: String) {
        assertThrows<IllegalArgumentException> {
            client.requeteDonnerReponse(idPartie, idJoueur, cleJoueur, reponse)
        }
    }

    @Test
    fun testRequeteReponse() {

        // Création de la partie et préparation du jeu
        var partieId = client.requeteCreationPartie(joueur1.id, joueur1.cle)
        val reponse = "non"
        val etat = gameTestHelper.advanceGameTo(joueur1, joueur2, partieId, GameStateHelper.GameStep.WAIT_REFLEXION)

        // Tentative de donner une réponse au mauvais moment
        assertThrows<QuiEstCeException> {
            client.requeteDonnerReponse(partieId, joueur2.id, joueur2.cle, reponse)
        }

        // Vérification de l'état courant de la partie, la réponse ne doit pas être donnée avant le bon moment
        assertEquals(reponse, etat.reponseCourante, "La reponse courante devrait etre '${reponse}' à la place ---> ${etat.reponseCourante}")

        // Tentative de réponse au mauvais moment
        assertThrows<QuiEstCeException> {
            client.requeteDonnerReponse(partieId, joueur1.id, joueur1.cle, reponse)
        }
    }
}
