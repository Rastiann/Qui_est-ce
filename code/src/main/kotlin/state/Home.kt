package state

import info.but1.sae2025.QuiEstCeClient

class Home(
    host: String,
    port: Int,
    stateChangeHandler: StateChangeHandler
): AppState(QuiEstCeClient(host, port), ApiThread(), stateChangeHandler) {
}