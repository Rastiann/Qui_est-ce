import info.but1.sae2025.QuiEstCeClient

class ConfigTest {

    companion object {
        val client = QuiEstCeClient("localhost", 8080)
        val playerProvider = PlayerProvider(client)
        val joueur1 = playerProvider.get()
        val joueur2 = playerProvider.get()
        val gameTestHelper = GameStateHelper(TestRequeteChercherEncore.Companion.client)
    }

}