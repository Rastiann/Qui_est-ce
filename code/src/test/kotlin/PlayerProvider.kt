import info.but1.sae2025.data.Joueur


class PlayerProvider {
    fun get(nom: String, prenom: String): Joueur {
        return Joueur(nom, prenom)
    }
}