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

    val client: QuiEstCeClient = QuiEstCeClient("172.26.69.145", 8080)

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
    fun testRequeteCreationJoueur_Success(nom: String, prenom: String) {
        val joueurCree = client.requeteCreationJoueur(nom, prenom)

        assert(joueurCree.id > 0) { "L'id du joueur doit être positif" }
        assert(joueurCree.cle.length == 32) { "La clé doit faire 32 caractères" }

        val joueurRecupere = client.requeteJoueur(joueurCree.id)
        assertEquals(nom, joueurRecupere.nom, "Le nom n'est pas le bon")
        assertEquals(prenom, joueurRecupere.prenom, "Le prénom n'est pas le meme")
    }

}