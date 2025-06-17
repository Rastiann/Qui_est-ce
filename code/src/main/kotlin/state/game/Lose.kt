package state.game

import state.Home

class Lose: GameState() {
    override fun onAttached() {}

    fun backHome() {
        stateChangeHandler.handle(Home(
            apiClient,
            apiThread,
            stateChangeHandler,
            selfPlayer,
        ))
    }
}