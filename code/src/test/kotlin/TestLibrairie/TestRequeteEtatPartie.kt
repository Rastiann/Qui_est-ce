import info.but1.sae2025.QuiEstCeClient
import info.but1.sae2025.data.ETAPE
import info.but1.sae2025.data.EtatPartie
import info.but1.sae2025.data.IdentificationJoueur
import info.but1.sae2025.exceptions.QuiEstCeException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class TestRequeteEtatPartie {
    private val client: QuiEstCeClient = QuiEstCeClient("172.26.69.145", 8080)
    private val playerProvider = PlayerProvider(client)
    private val gameTestHelper = GameStateHelper(client)
    private val joueur1 = playerProvider.get()
    private val joueur2 = playerProvider.get()


    @Test
    fun testRequeteEtatPartie() {
        // Création + état initial
        var partieId = client.requeteCreationPartie(joueur1.id, joueur1.cle)
        var etat = client.requeteEtatPartie(partieId)
        assertEtatInitial(etat)

        // Initialisation
        partieId = client.requeteCreationPartie(joueur1.id, joueur1.cle)
        gameTestHelper.advanceGameTo(joueur1, joueur2, partieId, GameStateHelper.GameStep.INITIALISATION)
        etat = client.requeteEtatPartie(partieId)
        assertEtatInitialisation(etat)

        // Attente question
        partieId = client.requeteCreationPartie(joueur1.id, joueur1.cle)
        gameTestHelper.advanceGameTo(joueur1, joueur2, partieId, GameStateHelper.GameStep.WAIT_QUESTION)
        etat = client.requeteEtatPartie(partieId)
        assertEtatAttenteQuestion(etat)

        // Attente réponse
        partieId = client.requeteCreationPartie(joueur1.id, joueur1.cle)
        gameTestHelper.advanceGameTo(joueur1, joueur2, partieId, GameStateHelper.GameStep.WAIT_RESPONSE)
        etat = client.requeteEtatPartie(partieId)
        assertEtatAttenteReponse(etat)

        // Test d'id négatif
        assertThrows<IllegalArgumentException> {
            client.requeteEtatPartie(-partie_id)
        }

        // Test d'id conforme mais inexistant
        assertThrows<QuiEstCeException> {
            client.requeteEtatPartie(1234)
        }
    }

    private fun assertEtatInitial(etat: EtatPartie) {
        assertEquals(ETAPE.CREEE, etat.etape, "L'étape de la partie devrait être 'CREEE', mais trouvée: ${etat.etape}")
        assertEquals(-1, etat.idJoueurQuestionCourante, "L'id du joueur de la question devrait être -1, mais trouvé: ${etat.idJoueurQuestionCourante}")
        assertEquals("", etat.questionCourante, "La question courante devrait être vide, mais trouvée: '${etat.questionCourante}'")
        assertEquals(-1, etat.idJoueurReponseCourante, "L'id du joueur de la réponse devrait être -1, mais trouvé: ${etat.idJoueurReponseCourante}")
        assertEquals("", etat.reponseCourante, "La réponse courante devrait être vide, mais trouvée: '${etat.reponseCourante}'")
    }

    private fun assertEtatInitialisation(etat: EtatPartie) {
        assertEquals(ETAPE.INITIALISATION, etat.etape, "L'étape de la partie devrait être 'INITIALISATION', mais trouvée: ${etat.etape}")
        assertEquals(-1, etat.idJoueurQuestionCourante, "L'id du joueur de la question devrait être -1, mais trouvé: ${etat.idJoueurQuestionCourante}")
        assertEquals("", etat.questionCourante, "La question courante devrait être vide, mais trouvée: '${etat.questionCourante}'")
        assertEquals(-1, etat.idJoueurReponseCourante, "L'id du joueur de la réponse devrait être -1, mais trouvé: ${etat.idJoueurReponseCourante}")
        assertEquals("", etat.reponseCourante, "La réponse courante devrait être vide, mais trouvée: '${etat.reponseCourante}'")
    }

    private fun assertEtatAttenteQuestion(etat: EtatPartie) {
        assertEquals(ETAPE.ATTENTE_QUESTION, etat.etape, "L'étape de la partie devrait être 'ATTENTE_QUESTION', mais trouvée: ${etat.etape}")
        assertEquals(joueur1.id, etat.idJoueurQuestionCourante, "L'id du joueur de la question devrait être celui de joueur1 (${joueur1.id}), mais trouvé: ${etat.idJoueurQuestionCourante}")
        assertEquals("", etat.questionCourante, "La question courante devrait être vide, mais trouvée: '${etat.questionCourante}'")
        assertEquals(joueur2.id, etat.idJoueurReponseCourante, "L'id du joueur de la réponse devrait être celui de joueur2 (${joueur2.id}), mais trouvé: ${etat.idJoueurReponseCourante}")
        assertEquals("", etat.reponseCourante, "La réponse courante devrait être vide, mais trouvée: '${etat.reponseCourante}'")
    }

    private fun assertEtatAttenteReponse(etat: EtatPartie) {
        assertEquals(ETAPE.ATTENTE_REPONSE, etat.etape, "L'étape de la partie devrait être 'ATTENTE_REPONSE', mais trouvée: ${etat.etape}")
        assertEquals(joueur1.id, etat.idJoueurQuestionCourante, "L'id du joueur de la question devrait être celui de joueur1 (${joueur1.id}), mais trouvé: ${etat.idJoueurQuestionCourante}")
        assertEquals("Est ce qu'il est roux", etat.questionCourante, "La question courante devrait être 'Est ce qu'il est roux', mais trouvée: '${etat.questionCourante}'")
        assertEquals(joueur2.id, etat.idJoueurReponseCourante, "L'id du joueur de la réponse devrait être celui de joueur2 (${joueur2.id}), mais trouvé: ${etat.idJoueurReponseCourante}")
        assertEquals("", etat.reponseCourante, "La réponse courante devrait être vide, mais trouvée: '${etat.reponseCourante}'")
    }

}
