
import controleur.AppController
import controleur.AskQuestionController
import info.but1.sae2025.QuiEstCeClient
import javafx.application.Application
import javafx.stage.Stage

class MainApp : Application() {
    override fun start(stage: Stage) {

        AppController(
            "localhost",
            8080,
            stage
        )

        stage.show()

        //val ecouteur = AskQuestionController()


        val client = QuiEstCeClient("localhost", 8080)

        val joueur1 = client.requeteCreationJoueur("Bastian", "COCHARD")
        val joueur2 = client.requeteCreationJoueur("Antonin", "COCHARD")

        val partieId = client.requeteCreationPartie(joueur1.id, joueur1.cle)
        val etat = client.requeteRejoindrePartie(partieId, joueur2.id, joueur2.cle)

        println("${etat.idJoueurReponseCourante}" + "${etat.idJoueurQuestionCourante}" + "${etat.questionCourante}" + "${etat.reponseCourante}")

    }
}

fun main() {
    Application.launch(MainApp::class.java)
}
