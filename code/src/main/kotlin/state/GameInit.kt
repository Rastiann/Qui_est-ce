package state

import info.but1.sae2025.QuiEstCeClient
import state.gameinit.GameInitState

class GameInit(
    apiClient: QuiEstCeClient,
    apiThread: ApiThread,
    stateChangeHandler: StateChangeHandler,
    gameInitState: GameInitState
): AppState(apiClient, apiThread, stateChangeHandler) {
}