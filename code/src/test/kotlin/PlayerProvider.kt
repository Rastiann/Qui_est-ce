import info.but1.sae2025.QuiEstCeClient
import info.but1.sae2025.data.IdentificationJoueur
import java.time.Instant

class PlayerProvider(client : QuiEstCeClient) {

    val timestamp = Instant.now().toEpochMilli()
    private var joueur1: IdentificationJoueur? = client.requeteCreationJoueur("sdf$timestamp", "sdf")
    private var joueur2: IdentificationJoueur? = client.requeteCreationJoueur("sdf$timestamp", "sdff")

    fun get(client: QuiEstCeClient): Pair<IdentificationJoueur, IdentificationJoueur> {
        return Pair(joueur1!!, joueur2!!)
    }
}