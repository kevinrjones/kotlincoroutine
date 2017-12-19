import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking


fun main(args: Array<String>) = runBlocking {

    var job = launch {
        delay(1000)
        println("World")
    }

    print("Hello, ")

    job.join()

}



