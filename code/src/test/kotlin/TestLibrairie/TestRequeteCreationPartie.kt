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
    val joueur : IdentificationJoueur = playerProvider.get()

    @Test
    fun testRequeteCreationPartie_Exceptions() {
        assertThrows<IllegalArgumentException> {
            client.requeteCreationPartie(-1, joueur.cle)
        }

        assertThrows<IllegalArgumentException> {
            client.requeteCreationPartie(joueur.id, "short_key")
        }

        assertThrows<IllegalArgumentException> {
            client.requeteCreationPartie(joueur.id, "a".repeat(33))
        }

        assertThrows<QuiEstCeException> {
            val id_inexistant = 123
            client.requeteCreationPartie(id_inexistant, joueur.cle)
        }

        assertThrows<QuiEstCeException> {
            val cle_inexistante = "a".repeat(32)
            client.requeteCreationPartie(joueur.id, cle_inexistante)
        }

        assertThrows<QuiEstCeException> {
            val cle_inexistante = "a".repeat(32)
            val id_inexistant = 123
            client.requeteCreationPartie(id_inexistant, cle_inexistante)
        }
    }

    @Test
    fun testRequeteCreationPartie_Success() {


        // Game 1

        var partieId = client.requeteCreationPartie(joueur.id, joueur.cle)
        var etat = client.requeteEtatPartie(partieId)
        assertEquals(joueur.id, etat.idJoueur1)
        assert(etat.etape.name == "CREEE") {
            "L'étape de la partie devrait être 'CREEE', trouvée: ${etat.etape}"
        }

        // Game 2

        partieId = client.requeteCreationPartie(joueur.id, joueur.cle)
        etat = client.requeteEtatPartie(partieId)
        assertEquals(joueur.id, etat.idJoueur1)
        assert(etat.etape.name == "CREEE") {
            "L'étape de la partie devrait être 'CREEE', trouvée: ${etat.etape}"
        }

        // Game 3

        partieId = client.requeteCreationPartie(joueur.id, joueur.cle)
        etat = client.requeteEtatPartie(partieId)
        assertEquals(joueur.id, etat.idJoueur1)
        assert(etat.etape.name == "CREEE") {
            "L'étape de la partie devrait être 'CREEE', trouvée: ${etat.etape}"
        }

        // Game 4

        partieId = client.requeteCreationPartie(joueur.id, joueur.cle)
        etat = client.requeteEtatPartie(partieId)
        assertEquals(joueur.id, etat.idJoueur1)
        assert(etat.etape.name == "CREEE") {
            "L'étape de la partie devrait être 'CREEE', trouvée: ${etat.etape}"
        }
    }
}