import info.but1.sae2025.QuiEstCeClient
import info.but1.sae2025.exceptions.QuiEstCeException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertEquals

class TestRequeteCreationJoueur {

    val client: QuiEstCeClient = ConfigTest.client


    companion object {
        @JvmStatic
        fun joueurProvider(): Stream<Arguments?>? {
            return Stream.of(
                Arguments.of("Bastien", "COCHARD"),
                Arguments.of("Enzo", "CHELLI"),
                Arguments.of("Matheo", "GRANDI"),
                Arguments.of("Victor", "BACHELIER"),
                Arguments.of("Elwan", "LOPEZ"),
            )
        }
    }

    // Tests d'exception lors de la création du joueur
    @Test
    fun testRequeteCreationJoueur_Exceptions() {

        assertThrows<IllegalArgumentException> {
            client.requeteCreationJoueur("", "")
        }

        assertThrows<IllegalArgumentException> {
            client.requeteCreationJoueur("Bastian", "")
        }

        assertThrows<IllegalArgumentException> {
            client.requeteCreationJoueur("", "COCHARD")
        }

        assertThrows<QuiEstCeException> {
            client.requeteCreationJoueur("Bastian", "COCHARD")
            client.requeteCreationJoueur("Bastian", "COCHARD")
        }
    }

    // Test réussi pour la création d'un joueur avec des noms et prénoms valides
    @ParameterizedTest
    @MethodSource("joueurProvider")
    fun testRequeteCreationJoueur_Success(nom: String, prenom: String) {
        val joueurCree = client.requeteCreationJoueur(nom, prenom)

        assert(joueurCree.id > 0) {
            "Erreur : L'id du joueur créé doit être positif, mais c'était : ${joueurCree.id}"
        }
        assert(joueurCree.cle.length == 32) {
            "Erreur : La clé du joueur créé doit faire 32 caractères, mais elle fait ${joueurCree.cle.length}"
        }

        val joueurRecupere = client.requeteJoueur(joueurCree.id)
        assertEquals(nom, joueurRecupere.nom, "Erreur : Le nom récupéré ne correspond pas au nom attendu.")
        assertEquals(prenom, joueurRecupere.prenom, "Erreur : Le prénom récupéré ne correspond pas au prénom attendu.")
    }
}
