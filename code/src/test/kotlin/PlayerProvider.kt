import info.but1.sae2025.QuiEstCeClient
import info.but1.sae2025.data.IdentificationJoueur
import jdk.jfr.internal.handlers.EventHandler.timestamp

class PlayerProvider(client : QuiEstCeClient) {

    val timestamp = System.currentTimeMillis()
    private var joueur: IdentificationJoueur = client.requeteCreationJoueur("sdf$timestamp", "sdf")

    fun get() : IdentificationJoueur {
        return joueur
    }
}