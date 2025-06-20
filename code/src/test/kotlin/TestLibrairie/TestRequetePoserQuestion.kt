import info.but1.sae2025.QuiEstCeClient
import info.but1.sae2025.data.ETAPE
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
        val client: QuiEstCeClient = ConfigTest.client
        val joueur1 = ConfigTest.joueur1
        val partieId = client.requeteCreationPartie(joueur1.id, joueur1.cle)
        val joueur2 = ConfigTest.joueur2
        val gameTestHelper = ConfigTest.gameTestHelper

        @JvmStatic
        fun argumentsInvalidesProvider_requeteChoixPersonnage(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(-1, joueur1.id, joueur1.cle, "Est ce qu'il est roux"), // idPartie invalide
                Arguments.of(partieId, -1, joueur1.cle, "Est ce qu'il est roux"), // idJoueur invalide
                Arguments.of(partieId, joueur1.id, "a".repeat(33), "Est ce qu'il est roux"), // cle trop longue
                Arguments.of(partieId, joueur1.id, "a".repeat(31), "Est ce qu'il est roux"), // cle trop courte
                Arguments.of(partieId, joueur1.id, joueur1.cle, "") // question vide
            )
        }
    }

    @ParameterizedTest
    @MethodSource("argumentsInvalidesProvider_requeteChoixPersonnage")
    fun testRequetePoserQuestion_Exception(idPartie: Int, idJoueur: Int, cleJoueur: String, question: String) {
        assertThrows<IllegalArgumentException>("Les paramètres sont invalides pour la partie $idPartie et joueur $idJoueur") {
            client.requetePoserQuestion(idPartie, idJoueur, cleJoueur, question)
        }
    }

    @Test
    fun testRequetePoserQuestion_StateAndStepVerification() {
        // Création d'une partie et passage à l'étape INITIALISATION
        val partieId = client.requeteCreationPartie(joueur1.id, joueur1.cle)
        gameTestHelper.advanceGameTo(joueur1, joueur2, partieId, GameStateHelper.GameStep.INITIALISATION)

        // Vérifier que l'étape de la partie est bien INITIALISATION
        val etatInitial = client.requeteEtatPartie(partieId)
        assertEquals(ETAPE.INITIALISATION, etatInitial.etape, "La partie $partieId devrait être à l'étape INITIALISATION avant de poser une question.")

        // Essayer de poser une question au mauvais moment (avant que la partie soit prête)
        assertThrows<QuiEstCeException>("La question ne peut pas être posée avant le bon moment dans l'étape actuelle") {
            client.requetePoserQuestion(partieId, joueur1.id, joueur1.cle, "Est ce qu'il est roux")
        }
    }

    @Test
    fun testRequetePoserQuestion_Success() {
        // Création de la partie et passage à l'étape ATTENTE_QUESTION
        val partieId = client.requeteCreationPartie(joueur1.id, joueur1.cle)
        gameTestHelper.advanceGameTo(joueur1, joueur2, partieId, GameStateHelper.GameStep.WAIT_QUESTION)

        // Poser une question après que la partie soit dans l'étape correcte
        val question = "Est ce qu'il est roux"
        client.requetePoserQuestion(partieId, joueur1.id, joueur1.cle, question)

        // Vérifier l'état de la partie
        val etatPartie = client.requeteEtatPartie(partieId)

        // Vérifier que l'étape est maintenant "WAIT_REFLEXION"
        assertEquals(ETAPE.ATTENTE_REPONSE, etatPartie.etape, "L'étape de la partie $partieId devrait être WAIT_REFLEXION après avoir posé une question.")

        // Vérifier que la question posée est bien enregistrée dans l'état de la partie
        assertEquals(question, etatPartie.questionCourante, "La question posée devrait être '$question', mais elle est '${etatPartie.questionCourante}'.")
    }
}
