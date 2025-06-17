package vue.game

import grid.Grid
import handlers.ImgHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import vue.GridVue

class UserTurnVue() {

    val root = VBox()
    val questionBtn = Button("Question")

    init {
        questionBtn.style = "-fx-background-color: orange; -fx-text-fill: black; -fx-font-weight: bold;"
        questionBtn.prefWidth = 100.0
        root.spacing = 10.0
    }

    fun update(
        grid: Grid,
        sizeRatio: Double,
    ) {
        val gridVue = GridVue(grid, sizeRatio, null)

        root.children.clear()
        root.children.addAll(gridVue, questionBtn)
    }

}