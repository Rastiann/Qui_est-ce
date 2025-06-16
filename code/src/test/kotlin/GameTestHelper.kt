import TestLibrairie.Companion.client
import TestLibrairie.Companion.joueur1
import info.but1.sae2025.QuiEstCeClient
import info.but1.sae2025.data.EtatPartie
import info.but1.sae2025.data.IdentificationJoueur
import info.but1.sae2025.data.Joueur

class GameTestHelper(client : QuiEstCeClient) {

    fun gameEnder(joueur1 : IdentificationJoueur, joueur2 : IdentificationJoueur, partieId : Int) : EtatPartie {

        client.requeteRejoindrePartie(partieId, joueur2.id, joueur2.cle)

        client.requeteChoixPersonnage(partieId, joueur1.id, joueur1.cle, 1, 1)

        client.requeteChoixPersonnage(partieId, joueur2.id, joueur2.cle, 2, 2)

        client.requetePoserQuestion(partieId, joueur1.id, joueur1.cle, "Est ce qu'il est roux")
        client.requeteDonnerReponse(partieId, joueur2.id, joueur2.cle, "non")
        client.requeteTrouve(partieId, joueur1.id, joueur1.cle, 2, 2)

        val gameState = client.requeteEtatPartie(partieId)

        return gameState
    }
}