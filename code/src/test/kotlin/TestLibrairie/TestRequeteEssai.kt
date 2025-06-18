import info.but1.sae2025.QuiEstCeClient
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TestRequeteEssai {

    val client: QuiEstCeClient = QuiEstCeClient("172.26.69.145", 8080)

    @Test
    fun testRequeteEssai() {
        assertEquals("Prêt à jouer à Qui-est-ce ?", client.requeteEssai(), "Le serveur n'est pas pret")
    }
}