package vue

import javafx.scene.control.Alert
import javafx.scene.control.ButtonType

class ErrorDialog(
    title: String,
    header: String,
    message: String
) {

    private val alert = Alert(Alert.AlertType.INFORMATION)

    init {
        alert.title = title
        alert.headerText = header
        alert.contentText = message

        val dialogPane = alert.dialogPane

        dialogPane.style = """
        -fx-font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
        -fx-font-weight: bold;
        -fx-text-fill: black; 
        """.trimIndent()

        dialogPane.lookupButton(ButtonType.OK).style = """
        -fx-background-color: orange; 
        -fx-text-fill: black; 
        -fx-font-weight: bold;
        -fx-padding: 6 15 6 15;
        """.trimIndent()

        // Fond noir et texte blanc pour le header
        dialogPane.lookup(".header-panel").style = """
        -fx-text-fill: white; 
        -fx-font-size: 18px; 
        -fx-font-weight: bold;
        -fx-padding: 10 10 10 10;
        """.trimIndent()

        // Texte du contenu en blanc
        dialogPane.lookup(".content.label").style = "-fx-text-fill: black; -fx-font-size: 14px;"
    }

    fun show() {
        alert.showAndWait()
    }
}