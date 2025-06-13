import info.but1.sae2025.QuiEstCeClient
import info.but1.sae2025.data.IdentificationJoueur
import jdk.jfr.internal.handlers.EventHandler.timestamp

class PlayerProvider(client : QuiEstCeClient) {

    val timestamp = System.currentTimeMillis()
    private var joueur1: IdentificationJoueur? = client.requeteCreationJoueur("sdf$timestamp", "sdf")
    private var joueur2: IdentificationJoueur? = client.requeteCreationJoueur("sdf$timestamp", "sdff")

    fun get(client: QuiEstCeClient): Pair<IdentificationJoueur, IdentificationJoueur> {
        return Pair(joueur1!!, joueur2!!)
    }
}