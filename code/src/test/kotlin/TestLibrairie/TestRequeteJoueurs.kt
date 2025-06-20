import info.but1.sae2025.QuiEstCeClient
import info.but1.sae2025.data.IdentificationJoueur
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class TestRequeteJoueurs {

    val client: QuiEstCeClient = ConfigTest.client
    val playerProvider = PlayerProvider(client)

    @Test
    fun testJoueurCree1EstDansListe() {
        val joueur = playerProvider.get()
        val joueursServeur = client.requeteJoueurs()

        assert(joueursServeur.contains(joueur.id)) {
            "Le joueur ${joueur.id} devrait apparaître dans la liste des joueurs récupérés"
        }
    }

    @Test
    fun testJoueurCree2EstDansListe() {
        val joueur = playerProvider.get()
        val joueursServeur = client.requeteJoueurs()

        assert(joueursServeur.contains(joueur.id)) {
            "Le joueur ${joueur.id} devrait apparaître dans la liste des joueurs récupérés"
        }
    }

    @Test
    fun testJoueurCree3EstDansListe() {
        val joueur = playerProvider.get()
        val joueursServeur = client.requeteJoueurs()

        assert(joueursServeur.contains(joueur.id)) {
            "Le joueur ${joueur.id} devrait apparaître dans la liste des joueurs récupérés"
        }
    }

    @Test
    fun testTousLesJoueursOntUnIdValide() {
        val joueursServeur = client.requeteJoueurs()

        assert(joueursServeur.all { it > 0 }) {
            "Tous les ID de joueurs récupérés doivent être strictement supérieurs à 0"
        }
    }
}
