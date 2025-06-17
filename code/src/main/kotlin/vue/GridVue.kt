package vue

import grid.Grid
import handlers.ImgHandler
import javafx.scene.Cursor
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.GridPane
import javafx.scene.shape.Rectangle
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class GridVue(
    val grid: Grid,
    val sizeRatio: Double,
    val handler: ImgHandler? = null,

    ): GridPane() {

    init {
        update()
    }

    fun update() {
        grid.grid.forEachIndexed { x, array ->
            array.forEachIndexed({ y, pers ->

                val image = ImageView(
                    Image(
                        "http://localhost:8080/resources/but1/${
                            URLEncoder.encode(pers.person.url, StandardCharsets.UTF_8.toString())
                        }"
                    )
                )

                image.fitWidth = 100.0 * sizeRatio
                image.fitHeight = 150.0 * sizeRatio

                if (pers.isGray) {
                    image.opacity = 0.3
                }

                // register click handler
                if (handler != null) {




                    image.setOnMouseClicked { handler.handle(x, y) }
                    image.setOnMouseEntered {
                        image.scaleX = 1.1
                        image.scaleY = 1.1
                        image.cursor = Cursor.HAND

                        val clip = Rectangle(image.fitWidth, image.fitHeight)
                        clip.arcWidth = 20.0
                        clip.arcHeight = 20.0
                        image.clip = clip

                        image.toFront()
                    }

                    image.setOnMouseExited {
                        image.scaleX = 1.0
                        image.scaleY = 1.0
                        image.cursor = Cursor.DEFAULT

                        image.clip = null
                    }
                }

                add(image, y, x)
            })
        }
    }

}