package vue

import grid.Person
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.text.Font
import state.Message
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

open class GamePane(
    val topLabelTxt: String,
    val centerNode: Node
): BorderPane() {

    val topLabel = Label(topLabelTxt)
    private val discussionVBox = VBox()

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
        this.right = discussionVBox
    }

    fun updateDiscussion(discussion: List<Message>) {

        discussionVBox.children.clear()

        for (message in discussion) {
            val vue = MessageVue(message)
            discussionVBox.children.add(vue)
        }
    }

    fun setBottomPers(pers: Person) {
        val hBox = HBox()

        val image = ImageView(
            Image(
                "http://${Config.serverAddr}:${Config.serverHost}/resources/but1/${
                    URLEncoder.encode(pers.url, StandardCharsets.UTF_8.toString())
                }"
            )
        )

        image.fitWidth = 100.0
        image.fitHeight = 150.0

        val label = Label("votre personnage")
        label.font = Font.font(20.0)
        label.style  = "-fx-text-fill: white"

        val subLabel = Label("(${pers.firstName} ${pers.name})")
        subLabel.style = "-fx-text-fill: #dbdad9"

        val vBox = VBox(label, subLabel)
        vBox.alignment = Pos.CENTER
        vBox.padding = Insets(0.0, 0.0, 0.0, 20.0)

        hBox.children.addAll(image, vBox)
        bottom = hBox
    }

}