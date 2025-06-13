package state

/**
 * handle function is called on api thread, <br>
 * **you must handle concurrency yourself**
 */
interface StateChangeHandler {
    fun handle(newState: AppState, error: Error? = null)
}