import java.io.File

class ConnectedPlayer(
    name: String,
    firstName: String,
    id: Int,
    val key: String
): Player(name, firstName, id) {

    companion object Saver {

        fun saveTo(player: ConnectedPlayer, filePath: String) {

            val file = File(filePath)
            file.writeText(
                   "${player.name}\n" +
                        "${player.firstName}\n" +
                        "${player.id}\n" +
                           "${player.key}\n",
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as ConnectedPlayer

        if (id != other.id) return false
        if (key != other.key) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + id
        result = 31 * result + key.hashCode()
        return result
    }
}