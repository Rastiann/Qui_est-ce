import info.but1.sae2025.QuiEstCeClient
import info.but1.sae2025.data.ETAPE
import info.but1.sae2025.data.IdentificationJoueur
import info.but1.sae2025.exceptions.QuiEstCeException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class TestRequeteCreationPartie {

    val client: QuiEstCeClient = QuiEstCeClient("172.26.69.145", 8080)
    val playerProvider = PlayerProvider(client)
    val joueur: IdentificationJoueur = playerProvider.get()

    // Teste les exceptions lors de la création de partie
    @Test
    fun testRequeteCreationPartie_Exceptions() {

        // Test pour ID joueur invalide
        assertThrows<IllegalArgumentException> {
            client.requeteCreationPartie(-1, joueur.cle)
        }

        // Test pour clé trop courte
        assertThrows<IllegalArgumentException> {
            client.requeteCreationPartie(joueur.id, "short_key")
        }

        // Test pour clé trop longue
        assertThrows<IllegalArgumentException> {
            client.requeteCreationPartie(joueur.id, "a".repeat(33))
        }

        // Test pour ID joueur inexistant
        assertThrows<QuiEstCeException> {
            val id_inexistant = 123
            client.requeteCreationPartie(id_inexistant, joueur.cle)
        }

        // Test pour clé inexistante
        assertThrows<QuiEstCeException> {
            val cle_inexistante = "a".repeat(32)
            client.requeteCreationPartie(joueur.id, cle_inexistante)
        }

        // Test pour ID joueur et clé inexistants
        assertThrows<QuiEstCeException> {
            val cle_inexistante = "a".repeat(32)
            val id_inexistant = 123
            client.requeteCreationPartie(id_inexistant, cle_inexistante)
        }
    }

    // Test de création de parties réussies
    @Test
    fun testRequeteCreationPartie_Success() {

        repeat(2) {
            val partieId = client.requeteCreationPartie(joueur.id, joueur.cle)
            val etat = client.requeteEtatPartie(partieId)
            assertEquals(joueur.id, etat.idJoueur1, "Le joueur1 n'a pas été correctement enregistré dans la partie. ça devrait etre ${joueur.id} à la place --> ${etat.idJoueur1}")
            assert(etat.etape.name == "CREEE") {
                "L'étape de la partie devrait être 'CREEE', trouvée: ${etat.etape}"
            }
        }
    }
}
