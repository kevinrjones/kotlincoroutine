import kotlinx.coroutines.experimental.*

// 3. show the code as is
// 4. uncomment the result.await, shows the async is concurrent
fun main(args: Array<String>) = runBlocking {
    log("main")
    val job = launch {
        log("launch")
        val result = async(coroutineContext) {
            doWork("Work 1")
        }
        //println(result.await())
        doWork("Work 2")

    }

    job.join()

    println("Active: ${job.isActive}")
    println("Cancelled: ${job.isCancelled}")
}

suspend fun doWork(msg: String): Int {
    log("$msg - Working")
    delay(500)
    log("$msg - Work done")
    return 42
}

public fun log(msg: String) {
    println("$msg in ${Thread.currentThread().name}")
}



