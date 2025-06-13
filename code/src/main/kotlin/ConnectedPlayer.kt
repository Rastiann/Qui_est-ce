import java.io.File

class ConnectedPlayer(
    firstName: String,
    name: String,
    val id: Int,
    val key: String
): Player(firstName, name) {


    companion object Saver {

        fun saveTo(player: ConnectedPlayer, filePath: String) {

            val file = File(filePath)
            file.writeText(
                   "${player.firstName}\n" +
                        "${player.name}\n" +
                        "${player.id}\n" +
                           player.key,
                Charsets.UTF_8
            )
        }


        fun readFrom(filePath: String): ConnectedPlayer? {

            return try {
                val file = File(filePath)
                val lines = file.readLines()

                if (lines.size != 4) {
                    return null
                }

                 ConnectedPlayer(
                     lines[0],
                     lines[1],
                     lines[2].toInt(),
                     lines[3],
                 )

            }catch(_: Error) {
                null
            }
        }
    }

}