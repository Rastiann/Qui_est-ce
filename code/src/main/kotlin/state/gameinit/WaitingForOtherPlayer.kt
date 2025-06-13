package state.gameinit

import grid.Grid
import grid.Person
import grid.PersonItem
import info.but1.sae2025.data.ETAPE
import state.GameInit

class WaitingForOtherPlayer: GameInitState() {

    override fun onAttached() {

        // check every 500ms if another player has joined in
        apiThread.setPeriodicTask(Runnable {

            try {

                // check
                val apiGameState = apiClient.requeteEtatPartie(gameInit.gameId)
                if (apiGameState.etape != ETAPE.INITIALISATION) {
                    return@Runnable
                }

                // fetch grids
                val apiSelfGrid = apiClient.requeteGrilleJoueur(gameInit.gameId, apiGameState.idJoueur1)
                val apiOtherGrid = apiClient.requeteGrilleJoueur(gameInit.gameId, apiGameState.idJoueur2)

                // parse grid
                val selfGrid = Grid(apiSelfGrid.map { array ->
                    array.map { apiPers ->
                        PersonItem(false, Person(apiPers.prenom, apiPers.nom, apiPers.url))
                    }
                })

                // parse grid
                val otherGrid = Grid(apiOtherGrid.map { array ->
                    array.map { apiPers ->
                        PersonItem(false, Person(apiPers.prenom, apiPers.nom, apiPers.url))
                    }
                })


                // safety : remove all periodic task to be sure
                // they don't change state after this change
                apiThread.setPeriodicTask(null)

                // send new state
                stateChangeHandler.handle(
                    GameInit(
                        gameInit,
                        ChoosingCharacter(
                            apiGameState.idJoueur2,
                            selfGrid,
                            otherGrid
                        )
                    )
                )

            }catch(e: Error) {
                stateChangeHandler.handle(gameInit, e)
            }
        }, 500)
    }
}