package state

import ConnectedPlayer
import Player
import grid.Grid
import grid.Person
import grid.PersonItem
import info.but1.sae2025.QuiEstCeClient
import state.gameinit.ChoosingCharacter
import state.gameinit.WaitingForOtherPlayer

class Home(
    apiClient: QuiEstCeClient,
    apiThread: ApiThread,
    stateChangeHandler: StateChangeHandler,
    val selfPlayer: ConnectedPlayer
): AppState(apiClient, apiThread, stateChangeHandler) {

    private var playersLock = Any()
    private var _registeredPlayers = mutableListOf<Player>()

    var registeredPlayers: List<Player>
        get() = synchronized(playersLock) { _registeredPlayers.toList() }
        set(value) = synchronized(playersLock) { _registeredPlayers = value.toMutableList() }

    private var gamesLock = Any()
    private var _registeredGames = mutableListOf<CreatedGame>()

    var registeredGames: List<CreatedGame>
        get() = synchronized(gamesLock) { _registeredGames.toList() }
        set(value) = synchronized(gamesLock) { _registeredGames = value.toMutableList() }


    private fun refreshGames(): MutableList<CreatedGame> {

        // ask for created game ids
        val gameIds = apiClient.requeteListePartiesCreees()

        // init games array with capacity
        val createdGames = ArrayList<CreatedGame>(gameIds.size)

        // fetch api games state
        for (id in gameIds) {
            val apiGameState = apiClient.requeteEtatPartie(id)

            // fetch player
            val player = apiClient.requeteJoueur(apiGameState.idJoueur1)
            createdGames.add(CreatedGame(
                id,
                Player(player.prenom, player.nom)
            ))
        }

        return createdGames
    }

    private fun refreshPlayers(): MutableList<Player> {

        // ask for connected player ids
        val playerIds = apiClient.requeteJoueurs()

        // init games array with capacity
        val connectedPlayers = ArrayList<Player>(playerIds.size)

        // fetch api players
        for (id in playerIds) {

            // fetch player
            val apiPlayer = apiClient.requeteJoueur(id)
            connectedPlayers.add(Player(
                apiPlayer.prenom, apiPlayer.nom
            ))
        }

        return connectedPlayers
    }

    init {
        apiThread.setPeriodicTask({
            try {

                val games = refreshGames()
                registeredGames = games

                val players = refreshPlayers()
                registeredPlayers = players

                stateChangeHandler.handle(this)

            }catch(e: Error) {
                stateChangeHandler.handle(this, e)
            }
        }, 1000)
    }

    fun createNewGame() {
        apiThread.executeImmediately {

            try {

                // fetch new game
                val newGameId = apiClient.requeteCreationPartie(
                    selfPlayer.id,
                    selfPlayer.key
                )

                // safety : remove all periodic task to be sure
                // they don't change state after this change
                apiThread.setPeriodicTask(null)

                // send new state
                stateChangeHandler.handle(
                    GameInit(
                        apiClient,
                        apiThread,
                        stateChangeHandler,
                        selfPlayer,
                        selfIsPlayer1 = true,
                        newGameId,
                        WaitingForOtherPlayer()
                    )
                )

            }catch(e: Error) {
                stateChangeHandler.handle(this, e)
            }
        }
    }

    fun joinGame(id: Int) {
        apiThread.executeImmediately {

            try {

                // join game
                val apiState = apiClient.requeteRejoindrePartie(
                    id, selfPlayer.id, selfPlayer.key
                )

                // fetch grids
                val apiSelfGrid = apiClient.requeteGrilleJoueur(id, selfPlayer.id)
                val apiOtherGrid = apiClient.requeteGrilleJoueur(id, apiState.idJoueur1)

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
                        apiClient,
                        apiThread,
                        stateChangeHandler,
                        selfPlayer,
                        selfIsPlayer1 = false,
                        id,
                        ChoosingCharacter(
                            apiState.idJoueur1,
                            selfGrid,
                            otherGrid
                        )
                    )
                )

            }catch(e: Error) {
                stateChangeHandler.handle(this, e)
            }
        }
    }

}