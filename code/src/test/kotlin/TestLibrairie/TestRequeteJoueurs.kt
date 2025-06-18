import info.but1.sae2025.QuiEstCeClient
import info.but1.sae2025.data.IdentificationJoueur
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TestRequeteJoueurs {

    @Test
    fun testRequeteListePartiesCreees() {
        val client: QuiEstCeClient = QuiEstCeClient("172.26.69.145", 8080)
        val playerProvider = PlayerProvider(client)
        val joueursCreees = mutableListOf<IdentificationJoueur>()

        // Création de joueurs et ajout à la liste
        var joueur: IdentificationJoueur = playerProvider.get()
        joueursCreees.add(joueur)

        joueur = playerProvider.get()
        joueursCreees.add(joueur)

        joueur = playerProvider.get()
        joueursCreees.add(joueur)

        // Récupération des joueurs depuis le serveur
        val joueursCreeesServer = client.requeteJoueurs()

        // Vérification que tous les ids des joueurs sont supérieurs à 0
        assert(joueursCreeesServer.all { it > 0 }) {
            "Tous les id des joueurs doivent être supérieurs à 0"
        }

        // Vérification que chaque joueur créé se trouve dans la liste récupérée du serveur
        joueursCreees.forEach { joueur ->
            val trouve = joueursCreeesServer.any { it == joueur.id }
            assert(trouve) {
                "Le joueur ${joueur.id} devrait apparaître dans la liste des joueurs récupérés"
            }
        }
    }
}
