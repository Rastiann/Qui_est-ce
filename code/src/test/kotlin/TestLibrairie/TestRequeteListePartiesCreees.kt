import info.but1.sae2025.QuiEstCeClient
import info.but1.sae2025.data.ETAPE
import info.but1.sae2025.data.IdentificationJoueur
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TestRequeteListePartiesCreees {

    val client: QuiEstCeClient = QuiEstCeClient("localhost", 8080)
    val playerProvider = PlayerProvider(client)
    val joueur1 : IdentificationJoueur = playerProvider.get()
    val joueur2 : IdentificationJoueur = playerProvider.get()
    val gameTestHelper = GameStateHelper(client)

    @Test
    fun testRequeteListePartiesCreees() {
        val partiesCreeesIds = mutableListOf<Int>()

        repeat(3) {
            val partieId = client.requeteCreationPartie(joueur1.id, joueur1.cle)
            partiesCreeesIds.add(partieId)
        }


        val partiesCreeesIdsServer = client.requeteListePartiesCreees()

        for (idPartie in partiesCreeesIdsServer) {
            val etat = client.requeteEtatPartie(idPartie)
            assertEquals(ETAPE.CREEE, etat.etape, "La partie $idPartie devrait être à l'étape CREEE")
        }

        for (idPartie in partiesCreeesIds) {
            assert(partiesCreeesIdsServer.contains(idPartie)) {
                "La partie $idPartie devrait apparaître dans la liste des parties CREEE"
            }
        }
    }
}