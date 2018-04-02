import kotlinx.coroutines.experimental.channels.*
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import kotlinx.coroutines.experimental.selects.select

fun producer1() = produce {
    while (true) {
        // 1. run tith 50
        // 2. increase delay to 220 and re-run to show timeout
        delay(50)
        send("message1")
    }
}


fun main(args: Array<String>) = runBlocking<Unit> {
    var msg = producer1().receive()

    select<Unit> {

        producer1().onReceive { value ->
            println(value)
        }
        onTimeout(100) {
            println("timeout")
        }
    }
}