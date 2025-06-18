import info.but1.sae2025.QuiEstCeClient
import info.but1.sae2025.data.ETAPE
import info.but1.sae2025.data.IdentificationJoueur
import info.but1.sae2025.data.Joueur
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TestRequeteJoueurs {

    @Test
    fun testRequeteListePartiesCreees() {
        val client : QuiEstCeClient = QuiEstCeClient("172.26.69.145", 8080)
        val playerProvider = PlayerProvider(client)
        val joueursCreees = mutableListOf<IdentificationJoueur>()

        var joueur : IdentificationJoueur = playerProvider.get()
        joueursCreees.add(joueur)


        joueur = playerProvider.get()
        joueursCreees.add(joueur)


        joueur = playerProvider.get()
        joueursCreees.add(joueur)


        val joueursCreeesServer = client.requeteJoueurs()

        // joueursCreeesServer.forEach { println(it) }

        assert(joueursCreeesServer.all { it > 0 }) {
            "Tous les id des joueurs doivent être supérieurs à 0"
        }

        for (joueur in joueursCreees) {
            val trouve = joueursCreeesServer.any { it == joueur.id }
            assert(trouve) {
                "Le joueur $joueur devrait apparaître dans la liste des parties terminées"
            }
        }
    }
}