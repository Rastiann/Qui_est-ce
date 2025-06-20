package statetest

import state.*

data class NewState(val state: AppState, val error: Throwable?)
