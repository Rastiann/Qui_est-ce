package vue.game

import grid.Grid
import handlers.ImgHandler
import javafx.scene.control.Button
import javafx.scene.layout.VBox
import vue.GridVue

class GuessVue {

    val root = VBox()
    val okBtn = Button("Ok")

    init {

        okBtn.style = "-fx-background-color: orange; -fx-text-fill: black; -fx-font-weight: bold;"
        okBtn.prefWidth = 100.0
        root.spacing = 10.0
    }

    fun update(
        grid: Grid,
        sizeRatio: Double,
        handler: ImgHandler
    ) {
        val gridVue = GridVue(grid, sizeRatio, handler)

        root.children.clear()
        root.children.addAll(gridVue, okBtn)
    }
}