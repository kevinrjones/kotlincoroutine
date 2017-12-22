import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.runBlocking

// 5. SHow this and explain why non-idiomatic
// Should opt-in to concurrent code, this is always concurrent
// use the async builder like in previous demos
fun main(args: Array<String>) = runBlocking {
    val result = doWorkAsync("Hello")

    result.join()
    println(result.getCompleted())
}

suspend fun doWorkAsync(msg: String): Deferred<Int> = async {
    log("$msg - Working")
    delay(500)
    log("$msg - Work done")

    // return from the closure
    return@async 42
}
