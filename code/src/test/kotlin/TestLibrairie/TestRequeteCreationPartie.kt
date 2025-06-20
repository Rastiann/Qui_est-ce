import info.but1.sae2025.QuiEstCeClient
import info.but1.sae2025.exceptions.QuiEstCeException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertEquals

class TestRequeteCreationPartie {

    val client: QuiEstCeClient = ConfigTest.client
    val joueur = ConfigTest.joueur1

    companion object {
        @JvmStatic
        fun illegalArgumentProvider(): Stream<Arguments> = Stream.of(
            Arguments.of(-1, "a".repeat(32)),             // ID invalide
            Arguments.of(ConfigTest.joueur1.id, "short"), // Clé trop courte
            Arguments.of(ConfigTest.joueur1.id, "a".repeat(33)) // Clé trop longue
        )

        @JvmStatic
        fun quiEstCeExceptionProvider(): Stream<Arguments> = Stream.of(
            Arguments.of(123, ConfigTest.joueur1.cle),          // ID inexistant
            Arguments.of(ConfigTest.joueur1.id, "a".repeat(32)), // Clé inexistante
            Arguments.of(123, "a".repeat(32))                    // ID + clé inexistants
        )
    }

    // Tests d’IllegalArgumentException (paramétrés)
    @ParameterizedTest
    @MethodSource("illegalArgumentProvider")
    fun testRequeteCreationPartie_IllegalArguments(id: Int, cle: String) {
        assertThrows<IllegalArgumentException> {
            client.requeteCreationPartie(id, cle)
        }
    }

    // Tests de QuiEstCeException (paramétrés)
    @ParameterizedTest
    @MethodSource("quiEstCeExceptionProvider")
    fun testRequeteCreationPartie_QuiEstCeException(id: Int, cle: String) {
        assertThrows<QuiEstCeException> {
            client.requeteCreationPartie(id, cle)
        }
    }

    // Test de création de parties réussies
    @Test
    fun testRequeteCreationPartie_Success() {
        repeat(2) {
            val partieId = client.requeteCreationPartie(joueur.id, joueur.cle)
            val etat = client.requeteEtatPartie(partieId)
            assertEquals(joueur.id, etat.idJoueur1, "Le joueur1 n'a pas été correctement enregistré dans la partie.")
            assert(etat.etape.name == "CREEE") {
                "L'étape de la partie devrait être 'CREEE', trouvée: ${etat.etape}"
            }
        }
    }
}
