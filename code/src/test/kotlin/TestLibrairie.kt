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
        val gameTestHelper = GameStateHelper(client)

        val joueur1 = playerProvider.get()
        val joueur2 = playerProvider.get()
        val partie_id = client.requeteCreationPartie(joueur1.id, joueur1.cle)
        val grille1 = client.requeteGrilleJoueur(partie_id, joueur1.id)
        var etat = client.requeteRejoindrePartie(partie_id, joueur2.id, joueur2.cle)
        val grille2 = client.requeteGrilleJoueur(partie_id, joueur2.id)



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

        assertEquals(4, grille1.size, "La grille du joueur 1 doit contenir 4 lignes")
        assertEquals(4, grille2.size, "La grille du joueur 2 doit contenir 4 lignes")

        grille1.forEachIndexed { index, ligne ->
            assertEquals(6, ligne.size, "Ligne ${index + 1} de la grille 1 doit contenir 6 personnages")
        }
        grille2.forEachIndexed { index, ligne ->
            assertEquals(6, ligne.size, "Ligne ${index + 1} de la grille 2 doit contenir 6 personnages")
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

        assert(etat.etape == ETAPE.INITIALISATION ) {
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


        val etat2 = client.requeteChoixPersonnage(partie_id, joueur1.id, joueur1.cle, 1, 1)

        assert(etat2.etape == ETAPE.INITIALISATION) {
            "L'étape de la partie devrait être 'INITIALISATION', trouvée: ${etat2.etape}"
        }

        assert(etat2.idJoueurReponseCourante == joueur1.id) {
            "l'idJoueurReponseCourante devrait etre celle correspondant au joueur1 (${joueur1.id}) à la place c'était : ${etat.idJoueurReponseCourante}"
        }

        etat = client.requeteChoixPersonnage(partie_id, joueur2.id, joueur2.cle, 3, 4)

        assert(etat.etape == ETAPE.TERMINEE) {
            "L'étape de la partie devrait être 'ATTENTE_QUESTION', trouvée: ${etat.etape}"
        }

        assert(etat.idJoueurQuestionCourante == joueur2.id) {
            "l'idJoueurReponseCourante devrait etre celle correspondant au joueur2 (${joueur2.id}) à la place c'était : ${etat.idJoueurQuestionCourante}"
        }
    }

    // ****** RequeteListePartiesCreees() ******** //

    @Test
    fun testRequeteListePartiesCreees() {
        val partiesCreeesIds = mutableListOf<Int>()

        // Game 1

        var partieId = client.requeteCreationPartie(joueur1.id, joueur1.cle)
        var etat = client.requeteEtatPartie(partieId)
        assertEquals(ETAPE.CREEE, etat.etape, "La partie $partieId devrait être terminée")

        partiesCreeesIds.add(partieId)

        // Game 2

        partieId = client.requeteCreationPartie(joueur1.id, joueur1.cle)
        etat = client.requeteEtatPartie(partieId)
        assertEquals(ETAPE.CREEE, etat.etape, "La partie $partieId devrait être terminée")

        partiesCreeesIds.add(partieId)

        // Game 3

        partieId = client.requeteCreationPartie(joueur1.id, joueur1.cle)
        etat = client.requeteEtatPartie(partieId)
        assertEquals(ETAPE.CREEE, etat.etape, "La partie $partieId devrait être terminée")

        partiesCreeesIds.add(partieId)

        val partiesCreeesIdsServer = client.requeteListePartiesCreees()

        for (idPartie in partiesCreeesIdsServer) {
            val etat = client.requeteEtatPartie(idPartie)
            assertEquals(ETAPE.CREEE, etat.etape, "La partie $idPartie devrait être à l'étape CREEE")
        }

        for (idPartie in partiesCreeesIds) {
            assert(partiesCreeesIdsServer.contains(idPartie)) {
                "La partie $idPartie devrait apparaître dans la liste des parties terminées"
            }
        }
    }


//    @Test
//    fun testRequeteListePartiesTerminees() {
//        val partiesTermineesIds = mutableListOf<Int>()
//
//        // Game 1
//
//        var partieId = client.requeteCreationPartie(TestLibrairie.Companion.joueur1.id, TestLibrairie.Companion.joueur1.cle)
//        var gameState = gameTestHelper.gameEnder(joueur1, joueur2, partieId)
//        assertEquals(ETAPE.TERMINEE, gameState.etape, "La partie $partieId devrait être terminée")
//
//        partiesTermineesIds.add(partieId)
//
//        // Game 2
//
//        partieId = client.requeteCreationPartie(TestLibrairie.Companion.joueur1.id, TestLibrairie.Companion.joueur1.cle)
//        gameState = gameTestHelper.gameEnder(joueur1, joueur2, partieId)
//        assertEquals(ETAPE.TERMINEE, gameState.etape, "La partie $partieId devrait être terminée")
//
//        partiesTermineesIds.add(partieId)
//
//        // Game 3
//
//        partieId = client.requeteCreationPartie(TestLibrairie.Companion.joueur1.id, TestLibrairie.Companion.joueur1.cle)
//        gameState = gameTestHelper.gameEnder(joueur1, joueur2, partieId)
//        assertEquals(ETAPE.TERMINEE, gameState.etape, "La partie $partieId devrait être terminée")
//
//        partiesTermineesIds.add(partieId)
//
//        val partiesTermineesServeur = client.requeteListePartiesTerminees()
//
//        // Je vérifie que les parties que j'ai terminées sont bien présent dans la liste
//
//        for (idPartie in partiesTermineesIds) {
//            assert(partiesTermineesServeur.contains(idPartie)) {
//                "La partie $idPartie devrait apparaître dans la liste des parties terminées"
//            }
//        }
//
//        // Je vérifie que la liste de parties renvoyées par le serveur sont tous terminés.
//
//        for (idPartie in partiesTermineesServeur) {
//            val etat = client.requeteEtatPartie(idPartie)
//            assertEquals(ETAPE.TERMINEE, etat.etape, "L'étape de la partie $idPartie devrait être TERMINEE")
//        }
//    }
}