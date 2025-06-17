package vue

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Label
import state.Message

class MessageVue(message: Message): Label(message.message) {

    init {
        prefWidth = 200.0
        style = "-fx-border-radius: 8px; -fx-background-color: #333232"
        padding = Insets(8.0)
        alignment = if (message.isSelf) {
            Pos.CENTER_LEFT
        }else {
            Pos.CENTER_RIGHT
        }
    }

}