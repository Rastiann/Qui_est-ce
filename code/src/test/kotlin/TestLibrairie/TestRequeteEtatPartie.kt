import TestLibrairieOld.Companion.partie_id
import info.but1.sae2025.QuiEstCeClient
import info.but1.sae2025.data.ETAPE
import info.but1.sae2025.data.IdentificationJoueur
import info.but1.sae2025.exceptions.QuiEstCeException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class TestRequeteEtatPartie {
    val client: QuiEstCeClient = QuiEstCeClient("localhost", 8080)
    val playerProvider = PlayerProvider(client)
    val gameTestHelper = GameStateHelper(client)
    val joueur1 : IdentificationJoueur = playerProvider.get()
    val joueur2 : IdentificationJoueur = playerProvider.get()

    @Test
    fun testRequeteEtatPartie() {

        // ************* Rajouter des parametized test pour tester les entrées ************* //

        // Création + état initial
        var partieId = client.requeteCreationPartie(joueur1.id, joueur1.cle)
        var etat = client.requeteEtatPartie(partieId)
        assertEquals(ETAPE.CREEE, etat.etape)
        assertEquals(-1, etat.idJoueurQuestionCourante)
        assertEquals("", etat.questionCourante)
        assertEquals(-1, etat.idJoueurReponseCourante)
        assertEquals("", etat.reponseCourante)

        // Initialisation
        partieId = client.requeteCreationPartie(joueur1.id, joueur1.cle)
        gameTestHelper.advanceGameTo(joueur1, joueur2, partieId, GameStateHelper.GameStep.INITIALISATION)
        etat = client.requeteEtatPartie(partieId)
        assertEquals(ETAPE.INITIALISATION, etat.etape)
        assertEquals(-1, etat.idJoueurQuestionCourante)
        assertEquals("", etat.questionCourante)
        assertEquals(-1, etat.idJoueurReponseCourante)
        assertEquals("", etat.reponseCourante)

        // Attente question
        partieId = client.requeteCreationPartie(joueur1.id, joueur1.cle)
        gameTestHelper.advanceGameTo(joueur1, joueur2, partieId, GameStateHelper.GameStep.WAIT_QUESTION)
        etat = client.requeteEtatPartie(partieId)
        assertEquals(ETAPE.ATTENTE_QUESTION, etat.etape)
        assertEquals(joueur1.id, etat.idJoueurQuestionCourante)
        assertEquals("", etat.questionCourante)
        assertEquals(joueur2.id, etat.idJoueurReponseCourante)
        assertEquals("", etat.reponseCourante)

        // Attente réponse
        partieId = client.requeteCreationPartie(joueur1.id, joueur1.cle)
        gameTestHelper.advanceGameTo(joueur1, joueur2, partieId, GameStateHelper.GameStep.WAIT_RESPONSE)
        etat = client.requeteEtatPartie(partieId)
        assertEquals(ETAPE.ATTENTE_REPONSE, etat.etape)
        assertEquals(joueur1.id, etat.idJoueurQuestionCourante)
        assertEquals("Est ce qu'il est roux", etat.questionCourante)
        assertEquals(joueur2.id, etat.idJoueurReponseCourante)
        assertEquals("", etat.reponseCourante)


        // id néga
        assertThrows<IllegalArgumentException> {
            client.requeteEtatPartie(-partie_id)
        }

        // id conforme mais inexistant
        assertThrows<QuiEstCeException> {
            client.requeteEtatPartie(1234)
        }
    }

}