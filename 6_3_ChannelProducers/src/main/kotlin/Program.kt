import kotlinx.coroutines.experimental.channels.ProducerJob
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.channels.produce
import kotlinx.coroutines.experimental.runBlocking


// go from the previous demo to this
fun produceSquares() : ProducerJob<Int> = produce<Int> {
    for (x in 1..5) {
        println("sending")
        send(x * x)
    }
    println("sending - done")
}

fun main(args: Array<String>) = runBlocking<Unit> {
    val squares = produceSquares()
    squares.consumeEach { println(it) }
    println("Done!")
}