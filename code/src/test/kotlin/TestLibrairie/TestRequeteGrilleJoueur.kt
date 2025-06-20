import info.but1.sae2025.QuiEstCeClient
import info.but1.sae2025.data.IdentificationJoueur
import info.but1.sae2025.exceptions.QuiEstCeException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class TestRequeteGrilleJoueur {

    val client: QuiEstCeClient = ConfigTest.client
    val joueur1 = ConfigTest.joueur1
    val joueur2 = ConfigTest.joueur2
    val partieId = client.requeteCreationPartie(joueur1.id, joueur1.cle)
    val etat = client.requeteRejoindrePartie(partieId, joueur2.id, joueur2.cle)


    @Test
    fun testRequeteGrilleJoueur_Exceptions() {
        // Test avec des arguments invalides
        assertThrows<IllegalArgumentException> {
            client.requeteGrilleJoueur(-partieId, joueur1.id)
        }
        println("Test: Partie avec ID invalide")

        assertThrows<IllegalArgumentException> {
            client.requeteGrilleJoueur(partieId, -joueur1.id)
        }
        println("Test: Joueur avec ID invalide")

        assertThrows<IllegalArgumentException> {
            client.requeteGrilleJoueur(-partieId, -joueur1.id)
        }
        println("Test: Partie et joueur avec ID invalides")

        // Test avec des parties et joueurs inexistants
        assertThrows<QuiEstCeException> {
            val idPartieInexistant = 123
            client.requeteGrilleJoueur(idPartieInexistant, joueur1.id)
        }
        println("Test: Partie inexistante")

        assertThrows<QuiEstCeException> {
            val idPartieInexistant = 123
            client.requeteGrilleJoueur(partieId, idPartieInexistant)
        }
        println("Test: Joueur inexistant")
    }

    @Test
    fun testRequeteGrilleJoueur_Success() {

        val grille1 = client.requeteGrilleJoueur(partieId, joueur1.id)
        val grille2 = client.requeteGrilleJoueur(partieId, joueur2.id)

        // Vérification de la taille de la grille pour chaque joueur
        assertEquals(4, grille1.size, "La grille du joueur 1 devrait contenir 4 lignes, trouvée: ${grille1.size}")
        assertEquals(4, grille2.size, "La grille du joueur 2 devrait contenir 4 lignes, trouvée: ${grille2.size}")

        // Vérification de la taille des lignes dans chaque grille
        grille1.forEachIndexed { index, ligne ->
            assertEquals(6, ligne.size, "La ligne ${index + 1} de la grille 1 devrait contenir 6 personnages, trouvée: ${ligne.size}")
        }

        grille2.forEachIndexed { index, ligne ->
            assertEquals(6, ligne.size, "La ligne ${index + 1} de la grille 2 devrait contenir 6 personnages, trouvée: ${ligne.size}")
        }
    }
}
