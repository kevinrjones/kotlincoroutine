import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking

// 6. Show this code, prints out 42
// 7. Move the println to before the await - this throws an exception
fun main(args: Array<String>) = runBlocking {
    val job = launch {
        println("About to work")
        val r1 = async { doWorkDeferred("Deferred") }

        r1.await()
        println("Result is ${r1.getCompleted()}")
    }
    job.join()
}

suspend fun doWorkDeferred(msg: String): Int {
    log(msg)
    delay(100)
    return 42
}
