import info.but1.sae2025.QuiEstCeClient
import info.but1.sae2025.data.IdentificationJoueur

class PlayerProvider(val client : QuiEstCeClient) {

    fun get() : IdentificationJoueur {
        val randomString = generateRandomString()
        val joueur: IdentificationJoueur = client.requeteCreationJoueur("sdf$randomString", "sdf")
        return joueur
    }

    private fun generateRandomString(): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..10)
            .map { chars.random() }
            .joinToString("")
    }
}