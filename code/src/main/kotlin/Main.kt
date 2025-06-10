import info.but1.sae2025.QuiEstCeClient
import kotlin.random.Random

fun main() {
    println("Hello, World!")
    var client: QuiEstCeClient = QuiEstCeClient("localhost", 8080)
    // configuration à modifier bien entendu
    client.requeteEssai()
}