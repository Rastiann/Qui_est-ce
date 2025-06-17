package vue.gameinit

import grid.Grid
import handlers.ImgHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.BorderPane
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.paint.Color
import javafx.scene.text.Font
import vue.GamePane
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class ChoosingCharacterVue() {

    private val leftGrid = GridPane()
    private val rightGrid = GridPane()

    val main = VBox()
    val root = GamePane("Partie avec : ", main)

    init {

        main.padding = Insets(20.0)

        val title = Label("Choisir son personnage")
        title.font = Font.font(50.0)
        title.textFill = Color.LIGHTGRAY
        title.style = "-fx-background-color: #444; -fx-padding: 10px 20px;-fx-background-radius: 30px;"
        title.maxWidth = Double.MAX_VALUE

        val titleBox = HBox(title)
        titleBox.alignment = Pos.CENTER
        titleBox.maxWidth = Double.MAX_VALUE// Centrer le titre dans sa boîte

        val grids = HBox(40.0, leftGrid, rightGrid)
        grids.alignment = Pos.CENTER
        grids.maxWidth = Double.MAX_VALUE

        main.children.addAll(titleBox, grids)
        root.center = main
    }

    fun update(selfGrid: Grid, selfGridHandler: ImgHandler, otherGrid: Grid, otherGridHandler: ImgHandler) {
        showGrid(leftGrid, selfGrid, 1.0, selfGridHandler)
        showGrid(rightGrid, otherGrid, 0.5, otherGridHandler)
        leftGrid.isGridLinesVisible = true
        rightGrid.isGridLinesVisible = true
    }

    private fun showGrid(gridPane: GridPane, grid: Grid, sizeRation: Double, handler: ImgHandler) {
        gridPane.children.clear()

        grid.grid.forEachIndexed { x, array ->
            array.forEachIndexed({ y, pers ->

                val image = ImageView(Image(
                    "http://${Config.serverAddr}:${Config.serverHost}/resources/but1/${
                        URLEncoder.encode(pers.person.url, StandardCharsets.UTF_8.toString())
                    }"
                ))

                image.fitWidth = 100.0 * sizeRation
                image.fitHeight = 150.0 * sizeRation

                // register click handler
                image.setOnMouseClicked { handler.handle(x, y) }

                gridPane.add(image, y, x)
            })
        }
    }
}
