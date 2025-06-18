import info.but1.sae2025.QuiEstCeClient
import info.but1.sae2025.data.EtatPartie
import info.but1.sae2025.data.IdentificationJoueur
import info.but1.sae2025.exceptions.QuiEstCeException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class TestRequeteGrilleJoueur {

    val client: QuiEstCeClient = QuiEstCeClient("172.26.69.145", 8080)
    val playerProvider = PlayerProvider(client)
    val joueur1 : IdentificationJoueur = playerProvider.get()
    val joueur2 : IdentificationJoueur = playerProvider.get()
    val partieId = client.requeteCreationPartie(joueur1.id, joueur1.cle)
    val etat = client.requeteRejoindrePartie(partieId, joueur2.id, joueur2.cle)


    @Test
    fun testRequeteGrilleJoueur_Exceptions() {
        assertThrows<IllegalArgumentException> {
            client.requeteGrilleJoueur(-partieId, joueur1.id)
        }

        assertThrows<IllegalArgumentException> {
            client.requeteGrilleJoueur(partieId, -joueur1.id)
        }

        assertThrows<IllegalArgumentException> {
            client.requeteGrilleJoueur(-partieId, -joueur1.id)
        }


        assertThrows<QuiEstCeException> {
            val id_partie_inexistant = 123
            client.requeteGrilleJoueur(id_partie_inexistant, joueur1.id)
        }

        assertThrows<QuiEstCeException> {
            val id_joueur_inexistant = 123
            client.requeteGrilleJoueur(partieId, id_joueur_inexistant)
        }
    }

    @Test
    fun testRequeteGrilleJoueur_Success() {

        val grille1 = client.requeteGrilleJoueur(partieId, joueur1.id)
        val grille2 = client.requeteGrilleJoueur(partieId, joueur2.id)

        assertEquals(4, grille1.size, "La grille du joueur 1 doit contenir 4 lignes")
        assertEquals(4, grille2.size, "La grille du joueur 2 doit contenir 4 lignes")

        grille1.forEachIndexed { index, ligne ->
            assertEquals(6, ligne.size, "Ligne ${index + 1} de la grille 1 doit contenir 6 personnages")
        }
        grille2.forEachIndexed { index, ligne ->
            assertEquals(6, ligne.size, "Ligne ${index + 1} de la grille 2 doit contenir 6 personnages")
        }
    }
}