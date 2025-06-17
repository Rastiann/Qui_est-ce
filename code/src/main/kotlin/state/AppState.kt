package state

import info.but1.sae2025.QuiEstCeClient

sealed class AppState(
    protected val apiClient: QuiEstCeClient,
    protected val apiThread: ApiThread,
    protected val stateChangeHandler: StateChangeHandler
)