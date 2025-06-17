package vue

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.control.Label
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.text.Font
import state.Message

open class GamePane(
    val topLabelTxt: String,
    val centerNode: Node
): BorderPane() {

    val topLabel = Label(topLabelTxt)
    private val discussionHBox = HBox()

    init {
        topLabel.font = Font.font(20.0)
        topLabel.style = "-fx-text-fill: white;"
        val labelContainer = HBox(topLabel)
        labelContainer.alignment = Pos.CENTER

        this.padding = Insets(20.0)
        this.style = "-fx-background-color: #1e1e1e;"
        this.top = labelContainer
        this.center = centerNode

        //  discussion
        this.right = discussionHBox
    }

    fun updateDiscussion(discussion: List<Message>) {

        discussionHBox.children.clear()

        for (message in discussion) {
            val vue = MessageVue(message)
            discussionHBox.children.add(vue)
        }
    }

}