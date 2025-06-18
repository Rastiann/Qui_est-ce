package grid

class Grid(
    grid: List<List<PersonItem>>
) {
    val grid: List<List<PersonItem>>

    init {

        require(grid.size == 4) { "grid must be of size 4" }
        for (sublist in grid) {
            require(sublist.size == 6) { "grid sublist must be of size 6" }
        }

        this.grid = grid
    }

    fun setGrey(x: Int, y: Int, value: Boolean) {
        grid[x][y].isGray = value
    }

    fun copy(): Grid {
        return Grid(
            grid.map {array -> array.map {pers -> pers.copy() } }
        )
    }
}