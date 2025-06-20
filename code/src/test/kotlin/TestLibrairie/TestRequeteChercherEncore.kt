import info.but1.sae2025.data.ETAPE
import info.but1.sae2025.exceptions.QuiEstCeException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertEquals

class TestRequeteChercherEncore {

    companion object {
        val client = ConfigTest.client
        val joueur1 = ConfigTest.joueur1
        val joueur2 = ConfigTest.joueur2
        val partieId = client.requeteCreationPartie(joueur1.id, joueur1.cle)
        val gameTestHelper = ConfigTest.gameTestHelper

        @JvmStatic
        fun argumentsIllegalProvider_ChercherEncore(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(-1, joueur1.id, joueur1.cle),               // idPartie invalide
                Arguments.of(partieId, -1, joueur1.cle),                          // idJoueur invalide
                Arguments.of(partieId, joueur1.id, "a".repeat(33)),          // cle trop longue
                Arguments.of(partieId, joueur1.id, "a".repeat(31)),         // cle trop courte
            )
        }

        @JvmStatic
        fun argumentsQuiEstCeProvider_ChercherEncore(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(123, joueur1.id, joueur1.cle),             // idPartie valide mais inexistant
                Arguments.of(partieId, 123, joueur1.cle),                        // idJoueur valide mais inexistant
                Arguments.of(partieId, joueur1.id, "a".repeat(32)),         // cle valide mais inexistante
            )
        }
    }

    // Test de cas invalides
    @ParameterizedTest
    @MethodSource("argumentsIllegalProvider_ChercherEncore")
    fun testRequeteChercherEncore_Illegal(idPartie: Int, idJoueur: Int, cleJoueur: String) {
        assertThrows<IllegalArgumentException> {
            client.requeteChercherEncore(idPartie, idJoueur, cleJoueur)
        }
    }

    @ParameterizedTest
    @MethodSource("argumentsQuiEstCeProvider_ChercherEncore")
    fun testRequeteChercherEncore_QuiEstCe(idPartie: Int, idJoueur: Int, cleJoueur: String) {
        assertThrows<QuiEstCeException> {
            client.requeteChercherEncore(idPartie, idJoueur, cleJoueur)
        }
    }

    // Test de réussite
    @Test
    fun testRequeteChercherEncore_Success() {
        gameTestHelper.advanceGameTo(joueur1, joueur2, partieId, GameStateHelper.GameStep.WAIT_REFLEXION)

        // Test du succès lorsque le joueur effectue l'action au bon moment
        val etat = client.requeteChercherEncore(partieId, joueur1.id, joueur1.cle)

        // Vérification de l'état du jeu
        assertEquals(ETAPE.ATTENTE_QUESTION, etat.etape,
            "Erreur à l'étape: attendu ATTENTE_QUESTION mais obtenu ${etat.etape} pour partie $partieId, joueur ${joueur1.id}")

        assertEquals(joueur1.id, etat.idJoueurReponseCourante,
            "Erreur de joueur pour la réponse: attendu joueur ${joueur1.id} mais obtenu ${etat.idJoueurReponseCourante} dans la partie $partieId")

        assertEquals(joueur2.id, etat.idJoueurQuestionCourante,
            "Erreur de joueur pour la question: attendu joueur ${joueur2.id} mais obtenu ${etat.idJoueurQuestionCourante} dans la partie $partieId")
    }

    // Test pour vérifier l'échec au mauvais moment avec le mauvais joueur
    @Test
    fun testRequeteChercherEncore_Failure_MauvaisMoment() {
        val partieId = client.requeteCreationPartie(joueur1.id, joueur1.cle)
        gameTestHelper.advanceGameTo(joueur1, joueur2, partieId, GameStateHelper.GameStep.WAIT_REFLEXION)

        // Cherche au bon moment avec le mauvais joueur
        assertThrows<QuiEstCeException> {
            client.requeteChercherEncore(partieId, joueur2.id, joueur2.cle)
        }
    }

    // Test pour vérifier l'échec au mauvais moment avec le bon joueur
    @Test
    fun testRequeteChercherEncore_Failure_MauvaisJoueur() {
        val partieId = client.requeteCreationPartie(joueur1.id, joueur1.cle)
        gameTestHelper.advanceGameTo(joueur1, joueur2, partieId, GameStateHelper.GameStep.INITIALISATION)

        // Cherche au mauvais moment avec le bon joueur
        assertThrows<QuiEstCeException> {
            client.requeteChercherEncore(partieId, joueur2.id, joueur2.cle)
        }
    }
}
