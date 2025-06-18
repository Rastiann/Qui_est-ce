import info.but1.sae2025.QuiEstCeClient
import info.but1.sae2025.data.IdentificationJoueur
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TestRequeteListeParties {

    val client: QuiEstCeClient = QuiEstCeClient("172.26.69.145", 8080)
    val playerProvider = PlayerProvider(client)
    val gameTestHelper = GameStateHelper(client)
    val joueur1: IdentificationJoueur = playerProvider.get()
    val joueur2: IdentificationJoueur = playerProvider.get()

    @Test
    fun testRequeteChoixPersonnage() {

        val partiesIds = mutableListOf<Int>()

        // Création
        var partieId = client.requeteCreationPartie(joueur1.id, joueur1.cle)
        partiesIds.add(partieId)

        // Initialisation
        partieId = client.requeteCreationPartie(joueur1.id, joueur1.cle)
        gameTestHelper.advanceGameTo(joueur1, joueur2, partieId, GameStateHelper.GameStep.INITIALISATION)
        partiesIds.add(partieId)

        // Attente question
        partieId = client.requeteCreationPartie(joueur1.id, joueur1.cle)
        gameTestHelper.advanceGameTo(joueur1, joueur2, partieId, GameStateHelper.GameStep.WAIT_QUESTION)
        partiesIds.add(partieId)

        // Attente réponse
        partieId = client.requeteCreationPartie(joueur1.id, joueur1.cle)
        gameTestHelper.advanceGameTo(joueur1, joueur2, partieId, GameStateHelper.GameStep.WAIT_RESPONSE)
        partiesIds.add(partieId)

        // Fin
        partieId = client.requeteCreationPartie(joueur1.id, joueur1.cle)
        gameTestHelper.advanceGameTo(joueur1, joueur2, partieId, GameStateHelper.GameStep.END)
        partiesIds.add(partieId)

        val partiesIdsServer = client.requeteListeParties()

        // Vérification que chaque partie créée apparaît dans la liste récupérée du serveur
        for (idPartie in partiesIds) {
            assert(partiesIdsServer.contains(idPartie)) {
                "La partie $idPartie ne figure pas dans la liste des parties récupérées"
            }
        }
    }
}
