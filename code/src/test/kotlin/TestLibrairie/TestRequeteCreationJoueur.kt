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

    val client: QuiEstCeClient = QuiEstCeClient("localhost", 8080)

    companion object {
        @JvmStatic
        fun joueurProvider(): Stream<Arguments?>? {
            return Stream.of(
                Arguments.of("Bastian", "COCHARD"),
                Arguments.of("Enzo", "CHELLI"),
                Arguments.of("Matheo", "GRANDI"),
                Arguments.of("Victor", "BACHELIER"),
                Arguments.of("Elwan", "LOPEZ"),
            )
        }
    }

    @Test
    fun testRequeteCreationJoueur_Exceptions() {

        // foutre la création de perso tout le temps en lowercase

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

    @ParameterizedTest
    @MethodSource("joueurProvider")
    fun testRequeteCreationJoueur(nom : String, prenom : String) {
        var joueurCree = client.requeteCreationJoueur(nom, prenom)
        var joueurRecupere = client.requeteJoueur(joueurCree.id)
        assertEquals(nom, joueurRecupere.nom, "Le nom n'est pas le bon")
        assertEquals(prenom, joueurRecupere.prenom, "Le prénom n'est pas le meme")
    }
}