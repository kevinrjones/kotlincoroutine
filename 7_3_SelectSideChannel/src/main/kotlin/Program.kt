import kotlinx.coroutines.experimental.channels.*
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import kotlinx.coroutines.experimental.selects.select

// 1. run without the side.onsend
fun produceNumbers(side: SendChannel<Int>) = produce<Int> {
    for(num in 1 ..10) {
        delay(100)

        // non-blocking send
        select<Unit> {
            onSend(num){}
            side.onSend(num){}
        }
    }
}

fun main(args: Array<String>) = runBlocking<Unit> {
    val side  = Channel<Int>()
    launch { side.consumeEach { println("side $it") } }

    val producer = produceNumbers(side)
    producer.consumeEach {
        println("Consuming $it")
        // consuemer is slow
        delay(500)
    }

    println("Done")
}
