import info.but1.sae2025.QuiEstCeClient
import info.but1.sae2025.data.ETAPE
import info.but1.sae2025.data.IdentificationJoueur
import info.but1.sae2025.exceptions.QuiEstCeException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class TestRequeteRejoindrePartie {

    val client: QuiEstCeClient = QuiEstCeClient("172.26.69.145", 8080)
    val playerProvider = PlayerProvider(client)
    val joueur1 : IdentificationJoueur = playerProvider.get()
    val partieId = client.requeteCreationPartie(joueur1.id, joueur1.cle)
    val joueur2 : IdentificationJoueur = playerProvider.get()

    @Test
    fun testRequeteRejoindrePartie_Exceptions() {

        assertThrows<IllegalArgumentException> {
            client.requeteRejoindrePartie(-partieId, joueur2.id, joueur2.cle)
        }

        assertThrows<IllegalArgumentException> {
            client.requeteRejoindrePartie(partieId, -joueur2.id, joueur2.cle)
        }

        assertThrows<IllegalArgumentException> {
            val shortKey = "a".repeat(31)
            client.requeteRejoindrePartie(partieId, joueur2.id, shortKey)
        }

        assertThrows<IllegalArgumentException> {
            val longKey = "a".repeat(31)
            client.requeteRejoindrePartie(partieId, joueur2.id, longKey)
        }

        assertThrows<QuiEstCeException> {
            val cleInexistante = "a".repeat(32)
            client.requeteRejoindrePartie(partieId, joueur2.id, cleInexistante)
        }

        assertThrows<QuiEstCeException> {
            client.requeteRejoindrePartie(123456, joueur2.id, joueur2.cle)
        }

        assertThrows<QuiEstCeException> {
            client.requeteRejoindrePartie(partieId, 123456, joueur2.cle)
        }
    }


    @Test
    fun testRequeteRejoindrePartie_EtatValide() {

        val partieId = client.requeteCreationPartie(joueur1.id, joueur1.cle)
        val etat = client.requeteRejoindrePartie(partieId, joueur2.id, joueur2.cle)


        assertEquals(joueur2.id, etat.idJoueur2, "idJoueur2 incorrect dans l'état de la partie")

        assert(etat.etape == ETAPE.INITIALISATION ) {
            "L'étape de la partie devrait être 'INITIALISATION', trouvée: ${etat.etape}"
        }
    }
}