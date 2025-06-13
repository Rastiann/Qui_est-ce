package state

import info.but1.sae2025.QuiEstCeClient

class Connecting(
    host: String,
    port: Int,
    stateChangeHandler: StateChangeHandler
): AppState(QuiEstCeClient(host, port), ApiThread(), stateChangeHandler) {

    init {
        this.apiThread.start()

        this.apiThread.executeImmediately {


            // try contacting server
            this.apiClient.requeteEssai()



        }
    }

}