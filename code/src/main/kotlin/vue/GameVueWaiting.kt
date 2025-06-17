package vue

import grid.Grid
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.text.Font

class GameVueWaiting() {
    val root = VBox(30.0)
    private val leftGrid = GridPane()
    private val rightGrid = GridPane()
    val title = Label("Tour de l'adversaire")

    init {
        root.padding = Insets(20.0)
        root.style = "-fx-background-color: #1e1e1e;"

        title.font = Font.font(50.0)
        title.textFill = Color.LIGHTGRAY
        title.style = "-fx-background-color: #444; -fx-padding: 10px 20px;-fx-background-radius: 30px;"
        title.maxWidth = Double.MAX_VALUE
        val rightSection = VBox(10.0, rightGrid)
        rightSection.alignment = Pos.CENTER
        val titleBox = HBox(title)
        titleBox.alignment = Pos.CENTER
        titleBox.maxWidth = Double.MAX_VALUE// Centrer le titre dans sa boîte
        val grids = HBox(40.0, leftGrid, rightSection)
        grids.alignment = Pos.CENTER
        grids.maxWidth = Double.MAX_VALUE
        rightGrid.padding = Insets(0.0,0.0,0.0,35.0)

        root.children.addAll(titleBox, grids)
    }


    fun update(selfGrid: Grid, otherGrid: Grid) {
        showGrid(leftGrid, selfGrid)
        showGrid(rightGrid, otherGrid)
        leftGrid.isGridLinesVisible = true
        rightGrid.isGridLinesVisible = true
    }

    private fun showGrid(gridPane: GridPane, grid: Grid) {
        gridPane.children.clear()

        grid.grid.forEachIndexed { x, array ->
            grid.grid[x].forEachIndexed({ y, pers ->
                val image = ImageView(Image(pers.person.url))
                gridPane.add(image, x, y)
            })
        }
    }
}