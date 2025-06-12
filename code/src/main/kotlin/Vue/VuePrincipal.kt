package Vue

import javafx.scene.control.Button
import javafx.scene.layout.BorderPane
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox

class VuePrincipal() {
    var GrilleGauche : GridPane
    var GrilleDroite : GridPane
    var GuessButton : Button
    var QuestionButton : Button
    var PassButton : Button
    var Rectanglehaut : HBox

    init {
        this.GrilleGauche = GridPane()
        this.GrilleDroite = GridPane()
        this.Rectanglehaut = HBox()
        this.GuessButton = Button("Guess")
        this.PassButton = Button("Pass")
        this.QuestionButton = Button("Question")


    }
}