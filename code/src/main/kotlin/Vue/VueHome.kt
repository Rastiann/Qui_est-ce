package Vue

import javafx.application.Application
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.control.TextField
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.stage.Stage

class VueHome() {
    val root = VBox(20.0)
    val title = Label("Qui est-ce ?")
    val subtitle = Label("By MVCrew")
    val sectionTitle = Label("Rejoindre/créer une Partie")
    val createBtn = Button("Créer")
    val joinRow = HBox(10.0)
    val codeField = TextField()
    val joinBtn = Button("Rejoindre")
    val gamesBox = VBox(20.0)
    val scroll = ScrollPane(gamesBox)
    val gamesTitle = Label("Parties joignables")
    init {
        root.alignment = Pos.CENTER
        root.style = "-fx-background-color: #1e1e1e"

        // Titre
        title.style = "-fx-font-size: 32px; -fx-text-fill: white; -fx-font-weight: bold;"
        subtitle.style = "-fx-text-fill: gray; -fx-font-size: 14px;"

        // Section créer/rejoindre
        sectionTitle.style = "-fx-font-size: 16px; -fx-text-fill: white; -fx-font-weight: bold;"

        // Ligne avec créer + code + rejoindre
        createBtn.style = "-fx-background-color: orange; -fx-text-fill: black; -fx-font-weight: bold;"
        createBtn.prefWidth = 120.0
        joinRow.alignment = Pos.CENTER

        codeField.promptText = "Code de la partie"
        codeField.style = "-fx-background-color: #404040; -fx-text-fill: white; -fx-border-color: orange; -fx-border-width: 2;"
        codeField.prefWidth = 150.0

        joinBtn.style = "-fx-background-color: orange; -fx-text-fill: black; -fx-font-weight: bold;"
        joinBtn.prefWidth = 120.0

        joinRow.children.addAll(codeField, joinBtn)

        // Section parties joignables avec bordure
        scroll.prefViewportHeight = 200.0
        gamesBox.alignment = Pos.CENTER
        scroll.padding = Insets(0.0,100.0,0.0,100.0)
        gamesBox.style = "-fx-border-color: orange; -fx-border-width: 2; -fx-padding: 10;"
        scroll.style = "-fx-background: transparent; -fx-background-color: transparent;"
        scroll.isFitToWidth = true

        gamesTitle.style = "-fx-font-size: 16px; -fx-text-fill: white; -fx-font-weight: bold;"

        gamesBox.children.add(gamesTitle)

        // Créer les 5 parties
        for (i in 2..10) {
            val gameRow = HBox(20.0)
            gameRow.alignment = Pos.CENTER

            val gameInfo = VBox(5.0)
            gameInfo.alignment = Pos.CENTER_LEFT

            val gameName = Label("Partie de Félix")
            gameName.style = "-fx-text-fill: white; -fx-font-weight: bold;"

            val playerCount = Label("0/2 joueurs")
            playerCount.style = "-fx-text-fill: gray; -fx-font-size: 12px;"

            gameInfo.children.addAll(gameName, playerCount)

            val joinGameBtn = Button("Rejoindre")
            joinGameBtn.style = "-fx-background-color: orange; -fx-text-fill: black; -fx-font-weight: bold;"
            joinGameBtn.prefWidth = 100.0

            gameRow.children.addAll(gameInfo, joinGameBtn)
            gamesBox.children.add(gameRow)
        }

        // Ajouter tout au conteneur principal
        root.children.addAll(
            title, subtitle, sectionTitle, createBtn, joinRow, scroll
        )
    }
}