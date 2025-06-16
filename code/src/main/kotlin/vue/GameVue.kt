package Vue

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.text.Font

class GameVue() {
    val root = VBox(30.0)
    private val leftGrid = GridPane()
    private val rightGrid = GridPane()
    val btn = Button("Pass")
    val btn2 = Button("Guess")
    val btn3 = Button("Question")
    val title = Label("QUI EST-CE ?")

    init {
        root.padding = Insets(20.0)
        root.style = "-fx-background-color: #1e1e1e;"

        title.font = Font.font(50.0)
        title.textFill = Color.LIGHTGRAY
        title.style = "-fx-background-color: #444; -fx-padding: 10px 20px;-fx-background-radius: 30px;"
        title.maxWidth = Double.MAX_VALUE
        val buttons = listOf(btn, btn2, btn3)
        buttons.forEach {
            it.style = "-fx-background-color: orange; -fx-text-fill: black; -fx-font-weight: bold;"
            it.prefWidth = 100.0
        }
        val buttonBox = HBox(20.0, btn, btn2, btn3) // 20.0 = espacement horizontal
        buttonBox.alignment = Pos.CENTER
        buttonBox.padding = Insets(10.0)
        val rightSection = VBox(10.0, rightGrid,buttonBox)
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

    fun update(left: List<String>, right: List<String>) {
        showGrid(leftGrid, left, fontSize = 80.0)
        showGrid(rightGrid, right, fontSize = 35.0)
        leftGrid.isGridLinesVisible = true
        rightGrid.isGridLinesVisible = true
    }

    private fun showGrid(grid: GridPane, data: List<String>, fontSize: Double) {
        grid.children.clear()
        data.forEachIndexed { i, emoji ->
            val label = Label(emoji)
            label.font = Font.font(fontSize)
            label.prefWidth = 40.0
            label.prefHeight = 40.0
            label.style = "-fx-background-color: #ccc; -fx-alignment: center;"
            grid.add(label, i % 6, i / 6)

        }
    }
}