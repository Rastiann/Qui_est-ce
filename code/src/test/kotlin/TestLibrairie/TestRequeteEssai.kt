import info.but1.sae2025.QuiEstCeClient
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TestRequeteEssai {

    val client: QuiEstCeClient = ConfigTest.client

    @Test
    fun testRequeteEssai() {
        assertEquals("Prêt à jouer à Qui-est-ce ?", client.requeteEssai(), "Le serveur n'est pas pret")
    }
}