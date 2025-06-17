import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.control.ProgressIndicator
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import javafx.scene.text.Font
import javafx.stage.Screen

open class SpinnerVue(val text: String, val direction: Direction) : BorderPane() {

    var title: Label

    enum class Direction {
        VERTICAL, HORIZONTAL
    }

    private val container = when (direction) {
        Direction.VERTICAL -> {
            val pane = VBox()
            pane.alignment = Pos.CENTER
            pane
        }
        Direction.HORIZONTAL -> {
            val pane = HBox()
            pane.alignment = Pos.CENTER
            pane
        }
    }

    init {
        container.style = "-fx-background-color: #1e1e1e;"

        this.maxWidth = Double.MAX_VALUE
        this.maxHeight = Double.MAX_VALUE

        title = Label(text)
        title.font = Font.font(30.0)
        title.alignment = Pos.CENTER
        title.style = "-fx-text-fill: white"
        title.padding = Insets(20.0)

        val spinner = ProgressIndicator()
        spinner.setPrefSize(100.0, 100.0)
        spinner.style = """
            -fx-progress-color: orange;
            -fx-background-color: transparent;
        """.trimIndent()

        container.children.addAll(title, spinner)
        this.children.clear()
        this.center = container
    }
}
