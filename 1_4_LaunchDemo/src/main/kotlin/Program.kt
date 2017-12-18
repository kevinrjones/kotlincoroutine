import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import java.lang.Thread.sleep
import kotlin.concurrent.thread

fun main(args: Array<String>)  {

    // 1. Using co-routines

    launch {
        delay(1000)
        println("World")
    }

    print("Hello, ")
    Thread.sleep(1500)

    // 2. Equivalent with threads

    thread {
        sleep(1000)
        println("World")
    }

    print("Hello, ")
    Thread.sleep(1500)


}