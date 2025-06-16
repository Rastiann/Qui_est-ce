package state

import ConnectedPlayer
import Player
import info.but1.sae2025.QuiEstCeClient

class PlayerCreation(
    apiClient: QuiEstCeClient,
    apiThread: ApiThread,
    stateChangeHandler: StateChangeHandler
): AppState(apiClient, apiThread, stateChangeHandler) {

    fun tryCreate(player: Player) {
        apiThread.executeImmediately {
            try {

                // request new player
                val id = apiClient.requeteCreationJoueur(player.name, player.firstName)

                // create connected player
                val connectedPlayer = ConnectedPlayer(
                    player.firstName,
                    player.name,
                    id.id,
                    id.cle
                )

                // create new state
                val homeState = Home(
                    apiClient,
                    apiThread,
                    stateChangeHandler,
                    connectedPlayer
                )

                // send new state
                stateChangeHandler.handle(homeState)

            }catch(e: Error) {
                stateChangeHandler.handle(this, e)
            }
        }
    }

    fun loadFrom(filePath: String) {
        apiThread.executeImmediately(Runnable {

            // read player form file
            val player = ConnectedPlayer.readFrom(filePath)

            // send error if no file and incorrect file
            if (player == null) {
                stateChangeHandler.handle(this, Error("cannot read player from $filePath"))
                return@Runnable
            }

            usePlayer(player)
        })
    }

    private fun usePlayer(player: ConnectedPlayer) {
        // test read player
        val returnedPlayer = apiClient.requeteJoueur(player.id)

        // send error if read player and returned player doesn't match
        if (returnedPlayer.nom != player.name || returnedPlayer.prenom != player.firstName) {
            stateChangeHandler.handle(this, Error("invalid player"))
            return
        }

        // create new state
        val homeState = Home(
            apiClient,
            apiThread,
            stateChangeHandler,
            player
        )

        // send new state
        stateChangeHandler.handle(homeState)
    }

    fun tryUsePlayer(player: ConnectedPlayer) {
        apiThread.executeImmediately{ usePlayer(player) }
    }

}