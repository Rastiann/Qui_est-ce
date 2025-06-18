import info.but1.sae2025.QuiEstCeClient
import info.but1.sae2025.data.ETAPE
import info.but1.sae2025.data.IdentificationJoueur
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TestRequeteListePartiesTerminees {

    val client: QuiEstCeClient = QuiEstCeClient("172.26.69.145", 8080)
    val playerProvider = PlayerProvider(client)
    val gameTestHelper = GameStateHelper(client)
    val joueur1 : IdentificationJoueur = playerProvider.get()
    val joueur2 : IdentificationJoueur = playerProvider.get()

    @Test
    fun testRequeteListePartiesTerminees() {

        val partiesTermineesIds = mutableListOf<Int>()

        // Création de parties et passage à l'étape "TERMINEE"
        repeat(3) {
            val partieId = client.requeteCreationPartie(joueur1.id, joueur1.cle)
            gameTestHelper.advanceGameTo(joueur1, joueur2, partieId, GameStateHelper.GameStep.END)
            partiesTermineesIds.add(partieId)
        }

        // Récupération des parties terminées depuis le serveur
        val partiesTermineesServeur = client.requeteListePartiesTerminees()

        // Vérification que chaque partie terminée est bien présente dans la liste des parties terminées
        for (idPartie in partiesTermineesIds) {
            assert(partiesTermineesServeur.contains(idPartie)) {
                "La partie $idPartie ne figure pas dans la liste des parties terminées renvoyée par le serveur."
            }
        }

        // Vérification que l'étape de chaque partie renvoyée par le serveur est bien "TERMINEE"
        for (idPartie in partiesTermineesServeur) {
            val etat = client.requeteEtatPartie(idPartie)
            assertEquals(
                ETAPE.TERMINEE,
                etat.etape,
                "L'étape de la partie $idPartie devrait être TERMINEE, mais l'étape actuelle est ${etat.etape}."
            )
        }
    }
}
