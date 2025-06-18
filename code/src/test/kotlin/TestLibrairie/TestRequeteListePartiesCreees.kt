import info.but1.sae2025.QuiEstCeClient
import info.but1.sae2025.data.ETAPE
import info.but1.sae2025.data.IdentificationJoueur
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TestRequeteListePartiesCreees {

    val client: QuiEstCeClient = QuiEstCeClient("172.26.69.145", 8080)
    val playerProvider = PlayerProvider(client)
    val joueur1: IdentificationJoueur = playerProvider.get()
    val joueur2: IdentificationJoueur = playerProvider.get()
    val gameTestHelper = GameStateHelper(client)

    @Test
    fun testRequeteListePartiesCreees() {

        val partiesCreeesIds = mutableListOf<Int>()

        // Création de 3 parties
        repeat(3) {
            val partieId = client.requeteCreationPartie(joueur1.id, joueur1.cle)
            partiesCreeesIds.add(partieId)
        }

        // Récupération de la liste des parties créées depuis le serveur
        val partiesCreeesIdsServer = client.requeteListePartiesCreees()

        // Vérification de l'état des parties créées, elles doivent être à l'étape "CREEE"
        for (idPartie in partiesCreeesIdsServer) {
            val etat = client.requeteEtatPartie(idPartie)
            assertEquals(
                ETAPE.CREEE,
                etat.etape,
                "La partie $idPartie devrait être à l'étape CREEE, mais l'étape actuelle est ${etat.etape}"
            )
        }

        // Vérification que toutes les parties créées sont dans la liste des parties créées récupérées du serveur
        for (idPartie in partiesCreeesIds) {
            assert(partiesCreeesIdsServer.contains(idPartie)) {
                "La partie $idPartie ne figure pas dans la liste des parties créées récupérées du serveur."
            }
        }
    }
}
