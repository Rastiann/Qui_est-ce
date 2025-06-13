package state

import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit

class ApiThread {

    private val taskQueue = LinkedBlockingQueue<Task>()

    @Volatile
    private var isRunning: Boolean = false

    private val workerThread: Thread = Thread { runLoop() }

    fun start() {
        workerThread.start()
    }

    fun stop() {
        isRunning = false

        // wait for thread to stop gracefully with 4s timeout
        // -> 4s because it's network opération
        workerThread.join(4000)

        if (workerThread.isAlive) {

            // force stop
            workerThread.interrupt()

        }
    }

    fun executeImmediately(runnable: Runnable) {
        taskQueue.put(Task.ImmediateTask(runnable))
    }

    fun setPeriodicTask(runnable: Runnable?, delay: Int = 1000) {
        if (runnable == null) {
            taskQueue.put(Task.RemovePeriodicTask)
        }else {
            taskQueue.put(Task.PeriodicTask(runnable, delay))
        }
    }

    private fun runLoop() {

        var currentPeriodicTask: Task.PeriodicTask? = null
        var lastPeriodicExecutionTime = System.currentTimeMillis()

        try {
            while (isRunning) {

                // if there is no periodic task,
                // listen for task and execute them directly
                val task = if (currentPeriodicTask == null) {

                    taskQueue.take()

                }else {

                    // compute how much time left to listen to new tasks
                    val currentTime = System.currentTimeMillis()
                    val remainingTimeMillis = lastPeriodicExecutionTime + currentPeriodicTask.delay - currentTime

                    if (remainingTimeMillis < 0) {

                        // time to listen is negative, execute periodic task
                        currentPeriodicTask

                    }else {

                        // listen until delay to execute periodic task is reached
                        taskQueue.poll(remainingTimeMillis, TimeUnit.MILLISECONDS) ?: currentPeriodicTask

                    }

                }

                when (task) {
                    is Task.ImmediateTask -> task.action.run()
                    is Task.PeriodicTask -> {

                        // execute periodic action after remembering time
                        // to be sure task is executed every task's delay

                        // this can be a new periodic task or another
                        // execution, doesn't matter
                        currentPeriodicTask = task
                        lastPeriodicExecutionTime = System.currentTimeMillis()
                        task.runnable.run()

                    }
                    Task.RemovePeriodicTask -> currentPeriodicTask = null
                    Task.ShutdownTask -> {
                        println("Api Thread stopped")
                        isRunning = false
                    }
                }

            }

        }catch(e: InterruptedException) {

            // if the thread is interrupted, handle situation correctly
            isRunning = false
            println("Api Thread interrupted")
            return

        }catch (e: Exception) {

            System.err.println("Error in API thread: ${e.message}")
            e.printStackTrace()

        }
    }
}

sealed class Task() {
    data class ImmediateTask(val action: Runnable) : Task()
    data class PeriodicTask(val runnable: Runnable, val delay: Int) : Task()
    data object RemovePeriodicTask: Task()
    data object ShutdownTask : Task()
}

