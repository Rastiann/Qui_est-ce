package grid

class Grid(
    grid: List<List<PersonItem>>
) {
    val grid: List<List<PersonItem>>

    init {

        require(grid.size == 4) { "grid must be of size 4" }
        for (sublist in grid) {
            require(sublist.size == 4) { "grid sublist must be of size 6" }
        }

        this.grid = grid
    }
}