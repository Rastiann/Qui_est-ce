package state.game

/**
 * wrapper class use when server is unreachable
 */
class ConnectionError(message: String): Error(message)