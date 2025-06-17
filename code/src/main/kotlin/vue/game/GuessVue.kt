package vue.game

import grid.Grid
import handlers.ImgHandler
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.layout.HBox
import vue.GridVue

class GuessVue {

    val root = HBox()
    val okBtn = Button("Ok")

    init {
        okBtn.alignment = Pos.CENTER_RIGHT
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