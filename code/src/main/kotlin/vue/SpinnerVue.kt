import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.control.ProgressIndicator
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.text.Font

class SpinnerVue(val text: String, val direction: Direction) : VBox() {

    enum class Direction {
        VERTICAL, HORIZONTAL
    }

    private val container = when (direction) {
        Direction.VERTICAL -> {
            val pane = VBox(10.0)
            pane.alignment = Pos.CENTER
            pane
        }
        Direction.HORIZONTAL -> {
            val pane = HBox(10.0)
            pane.alignment = Pos.CENTER
            pane
        }
    }

    init {
        container.style = "-fx-background-color: #1e1e1e;"

        val title = Label(text)
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
        this.alignment = Pos.CENTER
        this.children.add(container)
    }
}
