import kotlinx.coroutines.experimental.cancelAndJoin
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking


fun main(args: Array<String>) = runBlocking {

    var job = launch {
        repeat(1000) {
            delay(100)
            print(".")
        }
    }

    delay(2500)
//    println()
    // 1. show job.cancel and job.join
//    job.cancel()
//    job.join()

    // 2. Show job.cancelAndJoin
    job.cancelAndJoin()
    println()
    println("done")
}



