import info.but1.sae2025.QuiEstCeClient
import info.but1.sae2025.data.ETAPE
import info.but1.sae2025.data.IdentificationJoueur
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class TestRequeteChoixPersonnage {

    companion object {

        val client: QuiEstCeClient = QuiEstCeClient("172.26.69.145", 8080)
        val playerProvider = PlayerProvider(client)
        val joueur1 : IdentificationJoueur = playerProvider.get()
        val partieId = client.requeteCreationPartie(joueur1.id, joueur1.cle)
        val joueur2 : IdentificationJoueur = playerProvider.get()
        val etat = client.requeteRejoindrePartie(partieId, joueur2.id, joueur2.cle)

        @JvmStatic
        fun argumentsIllegalProvider_ChoixPerso(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(-partieId, joueur1.id, joueur1.cle, 2, 2),               // idPartie invalide
                Arguments.of(partieId, -joueur1.id, joueur1.cle, 2, 2),                         // idJoueur invalide
                Arguments.of(partieId, joueur1.id, "a".repeat(33), 2, 2),         // cle trop longue
                Arguments.of(partieId, joueur1.id, "a".repeat(31), 2, 2),        // cle trop courte
                Arguments.of(partieId, joueur1.id, joueur1.cle, 2, -1),             // colonne out of range
                Arguments.of(partieId, joueur1.id, joueur1.cle, 2, 6),             // colonne out of range
                Arguments.of(partieId, joueur1.id, joueur1.cle, -1, 2),           // ligne out of range
                Arguments.of(partieId, joueur1.id, joueur1.cle, 4, 2)            // cle trop courte
            )
        }

        @JvmStatic
        fun argumentsQuiEstCeProvider_ChoixPerso(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(-partieId, joueur1.id, joueur1.cle, 2, 2),       // idPartie invalide
                Arguments.of(partieId, -joueur1.id, joueur1.cle, 2, 2),                // idJoueur invalide
                Arguments.of(partieId, joueur1.id, "a".repeat(33), 2, 2),         // cle trop longue
                Arguments.of(partieId, joueur1.id, "a".repeat(31), 2, 2),        // cle trop courte
                Arguments.of(partieId, joueur1.id, joueur1.cle, 2, -1),             // colonne out of range
                Arguments.of(partieId, joueur1.id, joueur1.cle, 2, 6),             // colonne out of range
                Arguments.of(partieId, joueur1.id, joueur1.cle, -1, 2),           // ligne out of range
                Arguments.of(partieId, joueur1.id, joueur1.cle, 4, 2)            // cle trop courte
            )
        }
    }

    @ParameterizedTest
    @MethodSource("argumentsIllegalProvider_ChoixPerso")
    fun TestRequeteChoixPersonnage_Illegal(idPartie: Int, idJoueur: Int, cleJoueur: String, ligne : Int, colonne : Int) {
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

    // Tout est valide mais inexistant
    @ParameterizedTest
    @MethodSource("argumentsQuiEstCeProvider_ChoixPerso")
    fun TestRequeteChoixPersonnage_QuiEstCe(idPartie: Int, idJoueur: Int, cleJoueur: String, ligne : Int, colonne : Int) {
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

    @Test
    fun testRequeteChoixPersonnage_Success() {
    // Optimiser les tests

        var etat = client.requeteChoixPersonnage(partieId, joueur1.id, joueur1.cle, 1, 1)

        assert(etat.etape == ETAPE.INITIALISATION) {
            "L'étape de la partie devrait être 'INITIALISATION', trouvée: ${etat.etape}"
        }

        etat = client.requeteChoixPersonnage(partieId, joueur2.id, joueur2.cle, 3, 4)

        assert(etat.idJoueurReponseCourante == joueur2.id) {
            "l'idJoueurReponseCourante devrait etre celle correspondant au joueur1 (${joueur1.id}) à la place c'était : ${etat.idJoueurReponseCourante}"
        }

        assert(etat.etape == ETAPE.ATTENTE_QUESTION) {
            "L'étape de la partie devrait être 'ATTENTE_QUESTION', trouvée: ${etat.etape}"
        }

        assert(etat.idJoueurQuestionCourante == joueur1.id) {
            "l'idJoueurReponseCourante devrait etre celle correspondant au joueur2 (${joueur2.id}) à la place c'était : ${etat.idJoueurQuestionCourante}"
        }
    }
}