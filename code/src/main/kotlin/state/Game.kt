package state

import info.but1.sae2025.QuiEstCeClient

class Game(
    apiClient: QuiEstCeClient,
    apiThread: ApiThread,
    stateChangeHandler: StateChangeHandler
): AppState(apiClient, apiThread, stateChangeHandler) {
}