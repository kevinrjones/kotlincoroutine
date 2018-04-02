import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.thread

fun main(args: Array<String>)  {

    val result = AtomicInteger()

    // 1. Using threads

    for (i in 1..15_000) {
        thread(start = true) {
            result.getAndIncrement()
            Thread.sleep(10)
        }
    }

    println(result.get())

    result.set(0)

    // 2. coroutines - note that the counter is 10* higher but is still a lot quicker
    // on the mac I'm getting the correct result, I'm guessing because all the threads are spun
    // up at this point
    // Mention this and then remove the thread code above and show getting the wrong result quickly
    for (i in 1..1_500_000) {
        launch {
            result.getAndIncrement()
            delay(0)
        }
    }

    println(result.get())
}