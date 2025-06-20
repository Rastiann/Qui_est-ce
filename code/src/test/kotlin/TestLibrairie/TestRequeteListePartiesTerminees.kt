import info.but1.sae2025.QuiEstCeClient
import info.but1.sae2025.data.ETAPE
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TestRequeteListePartiesTerminees {

    val client: QuiEstCeClient = ConfigTest.client
    val gameTestHelper =ConfigTest.gameTestHelper
    val joueur1 = ConfigTest.joueur1
    val joueur2 = ConfigTest.joueur2

    @Test
    fun testPartieTerminee1EstDansListeEtapeCorrecte() {
        val partieId = client.requeteCreationPartie(joueur1.id, joueur1.cle)
        gameTestHelper.advanceGameTo(joueur1, joueur2, partieId, GameStateHelper.GameStep.END)

        val partiesTerminees = client.requeteListePartiesTerminees()
        val etat = client.requeteEtatPartie(partieId)

        assert(partiesTerminees.contains(partieId)) {
            "La partie $partieId ne figure pas dans la liste des parties terminées renvoyée par le serveur."
        }
        assertEquals(ETAPE.TERMINEE, etat.etape,
            "La partie $partieId devrait être à l'étape TERMINEE, mais est à l'étape ${etat.etape}."
        )
    }

    @Test
    fun testPartieTerminee2EstDansListeEtapeCorrecte() {
        val partieId = client.requeteCreationPartie(joueur1.id, joueur1.cle)
        gameTestHelper.advanceGameTo(joueur1, joueur2, partieId, GameStateHelper.GameStep.END)

        val partiesTerminees = client.requeteListePartiesTerminees()
        val etat = client.requeteEtatPartie(partieId)

        assert(partiesTerminees.contains(partieId)) {
            "La partie $partieId ne figure pas dans la liste des parties terminées renvoyée par le serveur."
        }
        assertEquals(ETAPE.TERMINEE, etat.etape,
            "La partie $partieId devrait être à l'étape TERMINEE, mais est à l'étape ${etat.etape}."
        )
    }

    @Test
    fun testPartieTerminee3EstDansListeEtapeCorrecte() {
        val partieId = client.requeteCreationPartie(joueur1.id, joueur1.cle)
        gameTestHelper.advanceGameTo(joueur1, joueur2, partieId, GameStateHelper.GameStep.END)

        val partiesTerminees = client.requeteListePartiesTerminees()
        val etat = client.requeteEtatPartie(partieId)

        assert(partiesTerminees.contains(partieId)) {
            "La partie $partieId ne figure pas dans la liste des parties terminées renvoyée par le serveur."
        }
        assertEquals(ETAPE.TERMINEE, etat.etape,
            "La partie $partieId devrait être à l'étape TERMINEE, mais est à l'étape ${etat.etape}."
        )
    }

    @Test
    fun testToutesLesPartiesTermineesSontEnEtatTERMINEE() {
        val partiesTerminees = client.requeteListePartiesTerminees()

        for (partieId in partiesTerminees) {
            val etat = client.requeteEtatPartie(partieId)
            assertEquals(
                ETAPE.TERMINEE,
                etat.etape,
                "L'étape de la partie $partieId devrait être TERMINEE, mais l'étape actuelle est ${etat.etape}."
            )
        }
    }
}
