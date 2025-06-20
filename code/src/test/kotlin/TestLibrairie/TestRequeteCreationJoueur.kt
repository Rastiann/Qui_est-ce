import info.but1.sae2025.exceptions.QuiEstCeException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.UUID
import java.util.stream.Stream
import kotlin.test.assertEquals

class TestRequeteCreationJoueur {

    val client = ConfigTest.client

    companion object {
        @JvmStatic
        fun joueurProvider(): Stream<Arguments> = Stream.of(
            Arguments.of("Bastien", "COCHARD"),
            Arguments.of("Enzo", "CHELLI"),
            Arguments.of("Matheo", "GRANDI"),
            Arguments.of("Victor", "BACHELIER"),
            Arguments.of("Elwan", "LOPEZ"),
        )

        @JvmStatic
        fun illegalProvider(): Stream<Arguments> = Stream.of(
            Arguments.of("", ""),
            Arguments.of("Bastian", ""),
            Arguments.of("", "COCHARD")
        )
    }

    // Test des valeurs illégales (paramétré)
    @ParameterizedTest
    @MethodSource("illegalProvider")
    fun testRequeteCreationJoueur_Illegal(prenom: String, nom: String) {
        assertThrows<IllegalArgumentException> {
            client.requeteCreationJoueur(prenom, nom)
        }
    }

    // Test d'exception pour un joueur déjà existant
    @Test
    fun testRequeteCreationJoueur_QuiEstCe() {
        val prenom = "Noan"
        val nom = "MAHE"
        client.requeteCreationJoueur(prenom, nom) // création initiale
        assertThrows<QuiEstCeException> {
            client.requeteCreationJoueur(prenom, nom) // tentative de recréation
        }
    }

    // Test succès : on ajoute un identifiant unique pour éviter les doublons
    @ParameterizedTest
    @MethodSource("joueurProvider")
    fun testRequeteCreationJoueur_Success(nom: String, prenom: String) {
        // On ajoute un UUID pour rendre chaque nom/prénom unique à chaque exécution
        val uniquePrenom = "${prenom}_${UUID.randomUUID()}"
        val uniqueNom = "${nom}_${UUID.randomUUID()}"

        val joueurCree = client.requeteCreationJoueur(uniquePrenom, uniqueNom)

        assert(joueurCree.id > 0) {
            "Erreur : L'id du joueur créé doit être positif, mais c'était : ${joueurCree.id}"
        }
        assert(joueurCree.cle.length == 32) {
            "Erreur : La clé du joueur créé doit faire 32 caractères, mais elle fait ${joueurCree.cle.length}"
        }

        val joueurRecupere = client.requeteJoueur(joueurCree.id)
        assertEquals(uniquePrenom, joueurRecupere.nom, "Erreur : Le nom récupéré ne correspond pas.")
        assertEquals(uniqueNom, joueurRecupere.prenom, "Erreur : Le prénom récupéré ne correspond pas.")
    }
}
