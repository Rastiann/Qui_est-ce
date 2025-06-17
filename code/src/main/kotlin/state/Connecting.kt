package state

import info.but1.sae2025.QuiEstCeClient

class Connecting(
    host: String,
    port: Int,
    stateChangeHandler: StateChangeHandler
): AppState(QuiEstCeClient(host, port), ApiThread(), stateChangeHandler) {

    init {

        // start thread
        this.apiThread.start()

        this.apiThread.executeImmediately {

            // try contacting server
            try {

                apiClient.requeteEssai()

                stateChangeHandler.handle(
                    PlayerCreation(
                        apiClient,
                        apiThread,
                        stateChangeHandler
                    )
                )

            }catch (e: Throwable) {

                // connection went wrong, send error
                stateChangeHandler.handle(this, e)
                apiThread.stop()

            }
        }
    }

}