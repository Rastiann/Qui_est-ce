import info.but1.sae2025.QuiEstCeClient
import info.but1.sae2025.data.ETAPE
import info.but1.sae2025.data.IdentificationJoueur
import info.but1.sae2025.exceptions.QuiEstCeException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class TestRequeteRejoindrePartie {

    val client: QuiEstCeClient = ConfigTest.client
    val joueur1 = ConfigTest.joueur1
    val partieId = client.requeteCreationPartie(joueur1.id, joueur1.cle)
    val joueur2 = ConfigTest.joueur2

    @Test
    fun testRequeteRejoindrePartie_Illegal() {

        // Test de l'exception pour un ID de partie invalide
        assertThrows<IllegalArgumentException> {
            client.requeteRejoindrePartie(-partieId, joueur2.id, joueur2.cle)
        }

        // Test de l'exception pour un ID de joueur invalide
        assertThrows<IllegalArgumentException> {
            client.requeteRejoindrePartie(partieId, -joueur2.id, joueur2.cle)
        }

        // Test de la clé trop courte
        assertThrows<IllegalArgumentException> {
            val shortKey = "a".repeat(31)
            client.requeteRejoindrePartie(partieId, joueur2.id, shortKey)
        }

        // Test de la clé trop longue
        assertThrows<IllegalArgumentException> {
            val longKey = "a".repeat(33)
            client.requeteRejoindrePartie(partieId, joueur2.id, longKey)
        }

    }

    @Test
    fun testRequeteRejoindrePartie_QuiEstCe() {

        // Test de la clé inexistante
        assertThrows<QuiEstCeException> {
            val cleInexistante = "a".repeat(32)
            client.requeteRejoindrePartie(partieId, joueur2.id, cleInexistante)
        }

        // Test d'une partie inexistante
        assertThrows<QuiEstCeException> {
            client.requeteRejoindrePartie(123456, joueur2.id, joueur2.cle)
        }

        // Test d'un joueur inexistant
        assertThrows<QuiEstCeException> {
            client.requeteRejoindrePartie(partieId, 123456, joueur2.cle)
        }

    }

    @Test
    fun testRequeteRejoindrePartie_EtatValide() {

        // Crée une partie et récupère son état
        val etat = client.requeteRejoindrePartie(partieId, joueur2.id, joueur2.cle)

        // Vérification que l'ID du joueur 2 dans l'état de la partie est correct
        assertEquals(joueur2.id, etat.idJoueur2, "idJoueur2 incorrect dans l'état de la partie")

        // Vérification que l'étape de la partie est INITIALISATION
        assert(etat.etape == ETAPE.INITIALISATION) {
            "L'étape de la partie devrait être 'INITIALISATION', trouvée: ${etat.etape}"
        }
    }
}
