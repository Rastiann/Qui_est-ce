import info.but1.sae2025.QuiEstCeClient
import info.but1.sae2025.data.ETAPE
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TestRequeteListePartiesCreees {

    val client: QuiEstCeClient = ConfigTest.client
    val joueur1 = ConfigTest.joueur1
    val joueur2 = ConfigTest.joueur2

    @Test
    fun testPartieCreee1EstDansListeEtapeCorrecte() {
        val partieId = client.requeteCreationPartie(joueur1.id, joueur1.cle)

        val partiesCreees = client.requeteListePartiesCreees()
        val etat = client.requeteEtatPartie(partieId)

        assert(partiesCreees.contains(partieId)) {
            "La partie $partieId ne figure pas dans la liste des parties créées récupérées du serveur."
        }
        assertEquals(ETAPE.CREEE, etat.etape,
            "La partie $partieId devrait être à l'étape CREEE, mais est à l'étape ${etat.etape}"
        )
    }

    @Test
    fun testPartieCreee2EstDansListeEtapeCorrecte() {
        val partieId = client.requeteCreationPartie(joueur1.id, joueur1.cle)

        val partiesCreees = client.requeteListePartiesCreees()
        val etat = client.requeteEtatPartie(partieId)

        assert(partiesCreees.contains(partieId)) {
            "La partie $partieId ne figure pas dans la liste des parties créées récupérées du serveur."
        }
        assertEquals(ETAPE.CREEE, etat.etape,
            "La partie $partieId devrait être à l'étape CREEE, mais est à l'étape ${etat.etape}"
        )
    }

    @Test
    fun testPartieCreee3EstDansListeEtapeCorrecte() {
        val partieId = client.requeteCreationPartie(joueur1.id, joueur1.cle)

        val partiesCreees = client.requeteListePartiesCreees()
        val etat = client.requeteEtatPartie(partieId)

        assert(partiesCreees.contains(partieId)) {
            "La partie $partieId ne figure pas dans la liste des parties créées récupérées du serveur."
        }
        assertEquals(ETAPE.CREEE, etat.etape,
            "La partie $partieId devrait être à l'étape CREEE, mais est à l'étape ${etat.etape}"
        )
    }

    @Test
    fun testToutesLesPartiesCreeesSontEnEtatCREEE() {
        val partiesCreees = client.requeteListePartiesCreees()

        for (partieId in partiesCreees) {
            val etat = client.requeteEtatPartie(partieId)
            assertEquals(ETAPE.CREEE, etat.etape,
                "La partie $partieId devrait être à l'étape CREEE, mais est à l'étape ${etat.etape}"
            )
        }
    }
}
