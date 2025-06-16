open class Player(
    val name: String,
    val firstName: String
) {
    init {
        require(!name.contains('\n') && !name.contains('\r')) { "name connot have break line" }
        require(!firstName.contains('\n') && !firstName.contains('\r')) { "name connot have break line" }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Player

        if (name != other.name) return false
        if (firstName != other.firstName) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + firstName.hashCode()
        return result
    }
}