import kotlinx.coroutines.experimental.*
import kotlin.coroutines.experimental.coroutineContext

fun main(args: Array<String>) = runBlocking {
    val job = launch {
        // 1. job variable not visib;e
        //println("isActive?  ${job.isActive}")

        // 2. Use context
        println("isActive? ${coroutineContext[Job.Key]!!.isActive}")

        // 3. Or, idiomatically Key is the companion object of Job and so is available like this
        println("isActive? ${coroutineContext[Job]!!.isActive}")
    }

    job.join()
}

suspend fun doWork() {
    println("isActive? ${coroutineContext[Job]!!.isActive}")
}

