package vue

import Config
import grid.Grid
import grid.Person
import handlers.ImgHandler
import javafx.scene.Cursor
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.GridPane
import javafx.scene.shape.Rectangle
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class GridVue(
    grid: Grid,
    var sizeRatio: Double,
    var handler: ImgHandler? = null
): GridPane() {

    private val imageVueGrid: List<List<ImageView>>
    var grid: Grid = grid.copy()

    init {

        val arrayList = ArrayList<List<ImageView>>(grid.grid.size)
        grid.grid.forEachIndexed { x, array ->

            val mutableList = ArrayList<ImageView>(array.size)

            array.forEachIndexed { y, pers ->
                val image = ImageView(getImage(pers.person))
                setSizeRatio(image, sizeRatio)
                setGray(image, pers.isGray)
                setHandler(image, x, y, handler)

                this.add(image, y, x)
                mutableList.add(image)
            }

            arrayList.add(mutableList)
        }

        imageVueGrid = arrayList
    }

    private fun getImage(pers: Person): Image {
        return Image(
            "http://${Config.serverAddr}:${Config.serverPort}/resources/but1/${
                URLEncoder.encode(
                    pers.url, StandardCharsets.UTF_8.toString())
            }"
        )
    }

    private fun setSizeRatio(imageView: ImageView, sizeRatio: Double) {
        imageView.fitWidth = 100.0 * sizeRatio
        imageView.fitHeight = 150.0 * sizeRatio
    }

    private fun setHandler(imageView: ImageView, x: Int, y: Int, handler: ImgHandler?) {

        if (handler == null) {
            imageView.onMouseClicked = null
            imageView.onMouseEntered = null
            imageView.onMouseExited = null
            return
        }

        imageView.setOnMouseClicked { handler.handle(x, y) }

        // anim
        imageView.setOnMouseEntered {
            imageView.scaleX = 1.1
            imageView.scaleY = 1.1
            imageView.cursor = Cursor.HAND

            val clip = Rectangle(imageView.fitWidth, imageView.fitHeight)
            clip.arcWidth = 20.0
            clip.arcHeight = 20.0
            imageView.clip = clip

            imageView.toFront()
        }

        imageView.setOnMouseExited {
            imageView.scaleX = 1.0
            imageView.scaleY = 1.0
            imageView.cursor = Cursor.DEFAULT

            imageView.clip = null
        }
    }

    private fun setGray(imageView: ImageView, isGray: Boolean) {
        imageView.opacity = if (isGray) { 0.3 }else { 1.0 }
    }

    fun update(
        grid: Grid,
        sizeRatio: Double,
        handler: ImgHandler? = null
    ) {

        grid.grid.forEachIndexed { x, array ->
            array.forEachIndexed lastLoop@{ y, pers ->

                // avoid recreating images always
                val cachePers = this.grid.grid[x][y]
                val cacheImage = imageVueGrid[x][y]


                // update image
                if (pers.person != cachePers.person) {
                    cacheImage.image = getImage(pers.person)
                }

                // update sizeRatio
                if (sizeRatio != this.sizeRatio) {
                    setSizeRatio(cacheImage, sizeRatio)
                }

                // update gray
                setGray(cacheImage, pers.isGray)

                // update handler
                setHandler(cacheImage, x, y, handler)
            }
        }

        // copy to see change
        this.grid = grid.copy()
    }

}