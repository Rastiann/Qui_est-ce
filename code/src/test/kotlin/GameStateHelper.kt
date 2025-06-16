import info.but1.sae2025.QuiEstCeClient
import info.but1.sae2025.data.EtatPartie
import info.but1.sae2025.data.IdentificationJoueur

class GameStateHelper(val client : QuiEstCeClient) {

    enum class GameStep {
        INITIALISATION,
        WAIT_QUESTION,
        WAIT_RESPONSE,
        WAIT_REFLEXION,
        END
    }



        fun advanceGameTo(
            joueur1: IdentificationJoueur,
            joueur2: IdentificationJoueur,
            partieId: Int,
            step: GameStep
        ): EtatPartie {
            // On rejoint la partie à chaque étape sauf pour INITIALISATION (selon ton code)
            client.requeteRejoindrePartie(partieId, joueur2.id, joueur2.cle)

            if (step == GameStep.INITIALISATION) {
                return client.requeteRejoindrePartie(partieId, joueur2.id, joueur2.cle)
            }

            // Choix personnages
            client.requeteChoixPersonnage(partieId, joueur1.id, joueur1.cle, 1, 1)

            if (step == GameStep.WAIT_QUESTION) {
                return client.requeteChoixPersonnage(partieId, joueur2.id, joueur2.cle, 2, 2)
            }

            // Joueur 2 choisit personnage
            client.requeteChoixPersonnage(partieId, joueur2.id, joueur2.cle, 2, 2)

            if (step == GameStep.WAIT_RESPONSE) {
                return client.requetePoserQuestion(partieId, joueur1.id, joueur1.cle, "Est ce qu'il est roux")
            }

            if (step == GameStep.WAIT_REFLEXION) {
                client.requetePoserQuestion(partieId, joueur1.id, joueur1.cle, "Est ce qu'il est roux")
                return client.requeteDonnerReponse(partieId, joueur2.id, joueur2.cle, "non")
            }

            if (step == GameStep.END) {
                client.requetePoserQuestion(partieId, joueur1.id, joueur1.cle, "Est ce qu'il est roux")
                client.requeteDonnerReponse(partieId, joueur2.id, joueur2.cle, "non")
                client.requeteTrouve(partieId, joueur1.id, joueur1.cle, 2, 2)
                return client.requeteEtatPartie(partieId)
            }

            // Par défaut, retourne l’état actuel de la partie
            return client.requeteEtatPartie(partieId)
        }
    }
