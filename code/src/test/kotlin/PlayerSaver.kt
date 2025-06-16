import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

class PlayerSaver {

    companion object {
        val configPath = Paths.get(System.getProperty("user.home"), ".config", "quiestce")
        val playerFilePath =  configPath.resolve("player-test")

        @JvmStatic
        @BeforeAll
        fun setup(): Unit {
            Files.createDirectories(configPath)
        }
    }


    @Test
    fun savePlayer() {

        // write file
        val connectedPlayer = ConnectedPlayer("DUPONT", "Marcel", 1, "secret")
        ConnectedPlayer.saveTo(connectedPlayer, playerFilePath.toString())

        // read file raw
        val file = File(playerFilePath.toString())
        val lines = file.readLines()

        assert(lines[0] == "DUPONT" &&
                lines[1] == "Marcel" &&
                lines[2] == "1" &&
                lines[3] == "secret"
        )
    }

    @Test
    fun readPlayer() {

        // write to file
        val connectedPlayer = ConnectedPlayer("DUPONT", "Marcel", 1, "secret")
        ConnectedPlayer.saveTo(connectedPlayer, playerFilePath.toString())

        // read
        val connectedPlayerRead = ConnectedPlayer.readFrom(playerFilePath.toString())

        assert(connectedPlayer == connectedPlayerRead)
    }

}