import info.but1.sae2025.QuiEstCeClient
import info.but1.sae2025.data.ETAPE
import info.but1.sae2025.data.IdentificationJoueur
import info.but1.sae2025.exceptions.QuiEstCeException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class TestRequeteChoixPersonnage {

    companion object {

        val client: QuiEstCeClient = ConfigTest.client
        val joueur1 = ConfigTest.joueur1
        val partieId = client.requeteCreationPartie(joueur1.id, joueur1.cle)
        val joueur2 = ConfigTest.joueur2
        val etat = client.requeteRejoindrePartie(partieId, joueur2.id, joueur2.cle)

        @JvmStatic
        fun argumentsIllegalProvider_ChoixPerso(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(-partieId, joueur1.id, joueur1.cle, 2, 2),               // idPartie invalide
                Arguments.of(partieId, -joueur1.id, joueur1.cle, 2, 2),               // idJoueur invalide
                Arguments.of(partieId, joueur1.id, "a".repeat(33), 2, 2),             // cle trop longue
                Arguments.of(partieId, joueur1.id, "a".repeat(31), 2, 2),             // cle trop courte
                Arguments.of(partieId, joueur1.id, joueur1.cle, 2, -1),               // colonne out of range
                Arguments.of(partieId, joueur1.id, joueur1.cle, 2, 6),                // colonne out of range
                Arguments.of(partieId, joueur1.id, joueur1.cle, -1, 2),               // ligne out of range
                Arguments.of(partieId, joueur1.id, joueur1.cle, 4, 2)                 // ligne out of range
            )
        }

        @JvmStatic
        fun argumentsQuiEstCeProvider_ChoixPerso(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(123, joueur1.id, joueur1.cle, 2, 1),              // idPartie valide mais inexistant
                Arguments.of(partieId, 123, joueur1.cle, 3, 1),                        // idJoueur valide mais inexistant
                Arguments.of(partieId, joueur1.id, "a".repeat(32), 2, 3),            // cle trop courte
            )
        }
    }

    // Test de cas invalides
    @ParameterizedTest
    @MethodSource("argumentsIllegalProvider_ChoixPerso")
    fun testRequeteChoixPersonnage_Illegal(idPartie: Int, idJoueur: Int, cleJoueur: String, ligne: Int, colonne: Int) {
        assertThrows<IllegalArgumentException> {
            client.requeteChoixPersonnage(
                idPartie,
                idJoueur,
                cleJoueur,
                ligne,
                colonne
            )
        }
    }

    // Test pour vérifier l'absence de joueur ou de partie valide
    @ParameterizedTest
    @MethodSource("argumentsQuiEstCeProvider_ChoixPerso")
    fun testRequeteChoixPersonnage_QuiEstCe(idPartie: Int, idJoueur: Int, cleJoueur: String, ligne: Int, colonne: Int) {
        assertThrows<QuiEstCeException> {
            client.requeteChoixPersonnage(
                idPartie,
                idJoueur,
                cleJoueur,
                ligne,
                colonne
            )
        }
    }

    // Test de réussite
    @Test
    fun testRequeteChoixPersonnage_Success() {

        // Requête de choix pour le joueur 1
        var etat = client.requeteChoixPersonnage(partieId, joueur1.id, joueur1.cle, 1, 1)
        assert(etat.etape == ETAPE.INITIALISATION) {
            "Erreur : l'étape de la partie devrait être 'INITIALISATION' pour la partie $partieId, trouvée: ${etat.etape}"
        }

        // Requête de choix pour le joueur 2
        etat = client.requeteChoixPersonnage(partieId, joueur2.id, joueur2.cle, 3, 4)
        assert(etat.idJoueurReponseCourante == joueur2.id) {
            "Erreur : l'idJoueurReponseCourante devrait être celui du joueur 2 (${joueur2.id}), mais c'est ${etat.idJoueurReponseCourante} dans la partie $partieId"
        }

        assert(etat.etape == ETAPE.ATTENTE_QUESTION) {
            "Erreur : l'étape de la partie devrait être 'ATTENTE_QUESTION' pour la partie $partieId, trouvée: ${etat.etape}"
        }

        assert(etat.idJoueurQuestionCourante == joueur1.id) {
            "Erreur : l'idJoueurQuestionCourante devrait être celui du joueur 1 (${joueur1.id}), mais c'est ${etat.idJoueurQuestionCourante} dans la partie $partieId"
        }

        // Requête choix au mauvais moment
        assertThrows<QuiEstCeException> {
            client.requeteChoixPersonnage(partieId, joueur1.id, joueur1.cle, 3, 2)
        }
    }
}
