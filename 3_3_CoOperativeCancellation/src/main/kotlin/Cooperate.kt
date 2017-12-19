import kotlinx.coroutines.experimental.*

fun main(args: Array<String>) = runBlocking() {
    val job = launch {
        repeat(1000) {
            if(!isActive) throw CancellationException()

            print(".")
            Thread.sleep(1)
        }
    }

    delay(100)
    job.cancelAndJoin()
}