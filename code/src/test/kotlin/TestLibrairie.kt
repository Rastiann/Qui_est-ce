import info.but1.sae2025.QuiEstCeClient
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertEquals


class TestLibrairie {

    var client: QuiEstCeClient = QuiEstCeClient("localhost", 80)
    var identification_joueur1 = client.requeteCreationJoueur("Bastian", "COCHARD")
    var identification_joueur2 = client.requeteCreationJoueur("Enzo", "CHELLI")



    // ****** RequeteCreationJoueur() ******** //

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
    }

    @ParameterizedTest
    @MethodSource("joueurProvider")
    fun testRequeteCreationJoueur(nom : String, prenom : String, message : String) {
        val joueurCree = client.requeteCreationJoueur(nom, prenom)
        val joueurRecupere = client.requeteJoueur(joueurCree.id)
        assertEquals(nom, joueurRecupere.nom, "Le nom n'est pas le bon")
        assertEquals(prenom, joueurRecupere.prenom, "Le prénom n'est pas le meme")
    }
    companion object {
        @JvmStatic
        fun joueurProvider(): Stream<Arguments?>? {
            return Stream.of(
                Arguments.of("Bastian", "COCHARD", "le nom n'est pas le meme"),
                Arguments.of("Enzo", "CHELLI", "le nom n'est pas le meme"),
                Arguments.of("Matheo", "GRANDI", "le nom n'est pas le meme"),
                Arguments.of("Victor", "BACHELIER", "le nom n'est pas le meme"),
                Arguments.of("Elwan", "LOPEZ", "le nom n'est pas le meme"),
                )
        }
    }



    // ****** RequeteCreationPartie() ******** //

    @Test
    fun testRequeteCreationPartie_Exceptions() {
        assertThrows<IllegalArgumentException> {
            client.requeteCreationPartie(-1, "a".repeat(32))
        }

        assertThrows<IllegalArgumentException> {
            client.requeteCreationPartie(identification_joueur1.id, "short_key")
        }

        assertThrows<IllegalArgumentException> {
            client.requeteCreationPartie(identification_joueur1.id, "a".repeat(33))
        }
    }

    @Test
    fun testRequeteCreationPartie_Success() {
        val cleValide = "a".repeat(32)
        val joueur = client.requeteCreationJoueur("Test", "Test")
        val partieId = client.requeteCreationPartie(joueur.id, cleValide)
        assert(partieId >= 0)
    }

    // ****** requeteEssai() ******** //



}