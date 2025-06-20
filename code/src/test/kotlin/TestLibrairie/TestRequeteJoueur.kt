import info.but1.sae2025.QuiEstCeClient
import info.but1.sae2025.data.IdentificationJoueur
import info.but1.sae2025.exceptions.QuiEstCeException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertEquals

class TestRequeteJoueur {

    val client: QuiEstCeClient = ConfigTest.client
    val joueur1 = ConfigTest.joueur1
    val partieId = client.requeteCreationPartie(joueur1.id, joueur1.cle)
    val joueur2 = ConfigTest.joueur2

    companion object {
        @JvmStatic
        fun joueurProvider(): Stream<Arguments?>? {
            return Stream.of(
                Arguments.of("COCHARD", "Bastian"),
                Arguments.of("CHELLI", "Enzo"),
                Arguments.of("GRANDI", "Matheo"),
                Arguments.of("BACHELIER", "Victor"),
                Arguments.of("LOPEZ", "Elwan")
            )
        }
    }

    @Test
    fun testRequeteCreationJoueur_Exceptions() {
        // Test avec un ID invalide
        assertThrows<IllegalArgumentException> {
            client.requeteJoueur(-joueur1.id)
        }

        // Test avec un ID conforme mais inexistant
        assertThrows<QuiEstCeException> {
            client.requeteJoueur(12334)
        }
    }

    @ParameterizedTest
    @MethodSource("joueurProvider")
    fun testRequeteCreationJoueur_Success(nom: String, prenom: String) {
        // Création du joueur
        val joueurCree = client.requeteCreationJoueur(nom, prenom)

        // Récupération du joueur
        val joueurRecupere = client.requeteJoueur(joueurCree.id)

        // Assertions
        assertEquals(nom, joueurRecupere.nom, "Le nom du joueur récupéré est incorrect. Attendu: $nom, trouvé: ${joueurRecupere.nom}")
        assertEquals(prenom, joueurRecupere.prenom, "Le prénom du joueur récupéré est incorrect. Attendu: $prenom, trouvé: ${joueurRecupere.prenom}")
    }
}
