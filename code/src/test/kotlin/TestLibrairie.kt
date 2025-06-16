import info.but1.sae2025.QuiEstCeClient
import info.but1.sae2025.data.ETAPE
import info.but1.sae2025.exceptions.QuiEstCeException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertEquals


class TestLibrairie {

    companion object {

        val client: QuiEstCeClient = QuiEstCeClient("localhost", 8080)
        val playerProvider = PlayerProvider(client)

        val joueurs = playerProvider.get(client)
        val joueur1 = joueurs.first
        val joueur2 = joueurs.second
        val partie_id = client.requeteCreationPartie(joueur1.id, joueur1.cle)




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

        @JvmStatic
        fun argumentsInvalidesProvider_requeteChoixPersonnage(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(-1, joueur1.id, joueur1.cle, 2, 2),               // idPartie invalide
                Arguments.of(partie_id, -1, joueur1.cle, 2, 2),                         // idJoueur invalide
                Arguments.of(partie_id, joueur1.id, "a".repeat(33), 2, 2),         // cle trop longue
                Arguments.of(partie_id, joueur1.id, "a".repeat(31), 2, 2),        // cle trop courte
                Arguments.of(partie_id, joueur1.id, joueur1.cle, 2, -1),             // colonne out of range
                Arguments.of(partie_id, joueur1.id, joueur1.cle, 2, 6),             // colonne out of range
                Arguments.of(partie_id, joueur1.id, joueur1.cle, -1, 2),           // ligne out of range
                Arguments.of(partie_id, joueur1.id, joueur1.cle, 4, 2)            // cle trop courte
            )
        }
    }

    // ****** RequeteEssai() ******** //

    @Test
    fun testRequeteEssai() {
        assertEquals("Prêt à jouer à Qui-est-ce ?", client.requeteEssai(), "Le serveur n'est pas pret")
    }

    // ****** RequeteCreationJoueur() ******** //

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



    // ****** RequeteCreationPartie() ******** //

    @Test
    fun testRequeteCreationPartie_Exceptions() {
        assertThrows<IllegalArgumentException> {
            client.requeteCreationPartie(-1, joueur1.cle)
        }

        assertThrows<IllegalArgumentException> {
            client.requeteCreationPartie(joueur1.id, "short_key")
        }

        assertThrows<IllegalArgumentException> {
            client.requeteCreationPartie(joueur1.id, "a".repeat(33))
        }

        assertThrows<QuiEstCeException> {
            val id_inexistant = 123
            client.requeteCreationPartie(id_inexistant, joueur1.cle)
        }

        assertThrows<QuiEstCeException> {
            val cle_inexistante = "a".repeat(32)
            client.requeteCreationPartie(joueur1.id, cle_inexistante)
        }

        assertThrows<QuiEstCeException> {
            val cle_inexistante = "a".repeat(32)
            val id_inexistant = 123
            client.requeteCreationPartie(id_inexistant, cle_inexistante)
        }
    }

    @Test
    fun testRequeteCreationPartie_Success() {
        val partieId = client.requeteCreationPartie(joueur1.id, joueur1.cle)
        val etat = client.requeteEtatPartie(partieId)
        assertEquals(joueur1.id, etat.idJoueur1)
        assert(etat.etape.name == "CREEE") {
            "L'étape de la partie devrait être 'CREEE', trouvée: ${etat.etape}"
        }
    }



    // ****** requeteGrilleJoueur() ******** //

    @Test
    fun testRequeteGrilleJoueur_Exceptions() {
        assertThrows<IllegalArgumentException> {
            client.requeteGrilleJoueur(-partie_id, joueur1.id)
        }

        assertThrows<IllegalArgumentException> {
            client.requeteGrilleJoueur(partie_id, -joueur1.id)
        }

        assertThrows<IllegalArgumentException> {
            client.requeteGrilleJoueur(-partie_id, -joueur1.id)
        }


        assertThrows<QuiEstCeException> {
            val id_partie_inexistant = 123
            client.requeteGrilleJoueur(id_partie_inexistant, joueur1.id)
        }

        assertThrows<QuiEstCeException> {
            val id_joueur_inexistant = 123
            client.requeteGrilleJoueur(partie_id, id_joueur_inexistant)
        }
    }

    @Test
    fun testRequeteGrilleJoueur_Success() {
        val partieId = client.requeteCreationPartie(joueur1.id, joueur1.cle)

        client.requeteRejoindrePartie(partieId, joueur2.id, joueur2.cle)

        val grille1 = client.requeteGrilleJoueur(partieId, joueur1.id)
        val grille2 = client.requeteGrilleJoueur(partieId, joueur2.id)

        assertEquals(4, grille1.size, "La grille du joueur 1 doit contenir 3 lignes")
        assertEquals(4, grille2.size, "La grille du joueur 2 doit contenir 3 lignes")

        grille1.forEachIndexed { index, ligne ->
            assertEquals(6, ligne.size, "Ligne ${index + 1} de la grille 1 doit contenir 8 personnages")
        }
        grille2.forEachIndexed { index, ligne ->
            assertEquals(6, ligne.size, "Ligne ${index + 1} de la grille 2 doit contenir 8 personnages")
        }
    }


    // ****** requeteRejoindrePartie() ******** //

    @Test
    fun testRequeteRejoindrePartie_Exceptions() {

        assertThrows<IllegalArgumentException> {
            client.requeteRejoindrePartie(-partie_id, joueur2.id, joueur2.cle)
        }

        assertThrows<IllegalArgumentException> {
            client.requeteRejoindrePartie(partie_id, -joueur2.id, joueur2.cle)
        }

        assertThrows<IllegalArgumentException> {
            val shortKey = "a".repeat(31)
            client.requeteRejoindrePartie(partie_id, joueur2.id, shortKey)
        }

        assertThrows<IllegalArgumentException> {
            val longKey = "a".repeat(31)
            client.requeteRejoindrePartie(partie_id, joueur2.id, longKey)
        }

        assertThrows<QuiEstCeException> {
            val cleInexistante = "a".repeat(32)
            client.requeteRejoindrePartie(partie_id, joueur2.id, cleInexistante)
        }

        assertThrows<QuiEstCeException> {
            client.requeteRejoindrePartie(123456, joueur2.id, joueur2.cle)
        }

        assertThrows<QuiEstCeException> {
            client.requeteRejoindrePartie(partie_id, 123456, joueur2.cle)
        }
    }


    @Test
    fun testRequeteRejoindrePartie_EtatValide() {

        val idPartie = client.requeteCreationPartie(joueur1.id, joueur1.cle)
        val etat = client.requeteRejoindrePartie(idPartie, joueur2.id, joueur2.cle)

        assertEquals(joueur2.id, etat.idJoueur2, "idJoueur2 incorrect dans l'état de la partie")
        assertEquals(joueur1.id, etat.idJoueur1, "idJoueur2 incorrect dans l'état de la partie")

        assert(etat.etape.name == "INITIALISATION") {
            "L'étape de la partie devrait être 'INITIALISATION', trouvée: ${etat.etape}"
        }
    }


    // ****** RequeteChoixPersonnage() ******** //

    @ParameterizedTest
    @MethodSource("argumentsInvalidesProvider_requeteChoixPersonnage")
    fun testRequeteChoixPersonnage_Exception(idPartie: Int, idJoueur: Int, cleJoueur: String, ligne : Int, colonne : Int) {
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
    fun testRequeteChoixPersonnage() {


        val etat1 = client.requeteChoixPersonnage(partie_id, joueur1.id, joueur1.cle, 1, 1)

        assert(etat1.etape == ETAPE.INITIALISATION) {
            "L'étape de la partie devrait être 'INITIALISATION', trouvée: ${etat1.etape}"
        }

        assert(etat1.idJoueurReponseCourante == joueur1.id) {
            "l'idJoueurReponseCourante devrait etre celle correspondant au joueur1 (${joueur1.id}) à la place c'était : ${etat1.idJoueurReponseCourante}"
        }

        val etat2 = client.requeteChoixPersonnage(partie_id, joueur2.id, joueur2.cle, 3, 4)

        assert(etat2.etape == ETAPE.ATTENTE_QUESTION) {
            "L'étape de la partie devrait être 'ATTENTE_QUESTION', trouvée: ${etat2.etape}"
        }

        assert(etat2.idJoueurQuestionCourante == joueur2.id) {
            "l'idJoueurReponseCourante devrait etre celle correspondant au joueur2 (${joueur2.id}) à la place c'était : ${etat2.idJoueurQuestionCourante}"
        }
    }

}