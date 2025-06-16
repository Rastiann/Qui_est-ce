import info.but1.sae2025.QuiEstCeClient
import info.but1.sae2025.data.ETAPE
import info.but1.sae2025.data.IdentificationJoueur
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TestRequeteListePartiesTerminees {

    val client: QuiEstCeClient = QuiEstCeClient("localhost", 8080)
    val playerProvider = PlayerProvider(client)
    val gameTestHelper = GameStateHelper(client)
    val joueur1 : IdentificationJoueur = playerProvider.get()
    val joueur2 : IdentificationJoueur = playerProvider.get()

    @Test
    fun testRequeteListePartiesTerminees() {
        val partiesTermineesIds = mutableListOf<Int>()

        repeat(3) {
            val partieId = client.requeteCreationPartie(joueur1.id, joueur1.cle)
            val etatFin = gameTestHelper.advanceGameTo(joueur1, joueur2, partieId, GameStateHelper.GameStep.END)
            partiesTermineesIds.add(partieId)
        }

        val partiesTermineesServeur = client.requeteListePartiesTerminees()

        // Je vérifie que les parties que j'ai terminées sont bien présent dans la liste

        for (idPartie in partiesTermineesIds) {
            assert(partiesTermineesServeur.contains(idPartie)) {
                "La partie $idPartie devrait apparaître dans la liste des parties terminées"
            }
        }

        // Je vérifie que la liste de parties renvoyées par le serveur sont tous terminés.

        for (idPartie in partiesTermineesServeur) {
            val etat = client.requeteEtatPartie(idPartie)
            assertEquals(ETAPE.TERMINEE, etat.etape, "L'étape de la partie $idPartie devrait être TERMINEE")
        }
    }
}