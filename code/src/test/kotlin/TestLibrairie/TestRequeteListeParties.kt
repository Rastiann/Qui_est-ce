import info.but1.sae2025.QuiEstCeClient
import info.but1.sae2025.data.IdentificationJoueur
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class TestRequeteListeParties {

    val client = ConfigTest.client
    val gameTestHelper = ConfigTest.gameTestHelper
    val joueur1 = ConfigTest.joueur1
    val joueur2 = ConfigTest.joueur2

    @Test
    fun testPartieCreeeEstDansListe() {
        val partieId = client.requeteCreationPartie(joueur1.id, joueur1.cle)

        val partiesIdsServer = client.requeteListeParties()

        assert(partiesIdsServer.contains(partieId)) {
            "La partie $partieId (état création) ne figure pas dans la liste récupérée"
        }
    }

    @Test
    fun testPartieInitialisationEstDansListe() {
        val partieId = client.requeteCreationPartie(joueur1.id, joueur1.cle)
        gameTestHelper.advanceGameTo(joueur1, joueur2, partieId, GameStateHelper.GameStep.INITIALISATION)

        val partiesIdsServer = client.requeteListeParties()

        assert(partiesIdsServer.contains(partieId)) {
            "La partie $partieId (état initialisation) ne figure pas dans la liste récupérée"
        }
    }

    @Test
    fun testPartieWaitQuestionEstDansListe() {
        val partieId = client.requeteCreationPartie(joueur1.id, joueur1.cle)
        gameTestHelper.advanceGameTo(joueur1, joueur2, partieId, GameStateHelper.GameStep.WAIT_QUESTION)

        val partiesIdsServer = client.requeteListeParties()

        assert(partiesIdsServer.contains(partieId)) {
            "La partie $partieId (état attente question) ne figure pas dans la liste récupérée"
        }
    }

    @Test
    fun testPartieWaitResponseEstDansListe() {
        val partieId = client.requeteCreationPartie(joueur1.id, joueur1.cle)
        gameTestHelper.advanceGameTo(joueur1, joueur2, partieId, GameStateHelper.GameStep.WAIT_RESPONSE)

        val partiesIdsServer = client.requeteListeParties()

        assert(partiesIdsServer.contains(partieId)) {
            "La partie $partieId (état attente réponse) ne figure pas dans la liste récupérée"
        }
    }

    @Test
    fun testPartieTermineeEstDansListe() {
        val partieId = client.requeteCreationPartie(joueur1.id, joueur1.cle)
        gameTestHelper.advanceGameTo(joueur1, joueur2, partieId, GameStateHelper.GameStep.END)

        val partiesIdsServer = client.requeteListeParties()

        assert(partiesIdsServer.contains(partieId)) {
            "La partie $partieId (état terminée) ne figure pas dans la liste récupérée"
        }
    }
}
