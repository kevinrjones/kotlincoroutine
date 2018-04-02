import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking

suspend fun sum(s: Array<Int>, c: Channel<Int>) {

    var result = 0
    s.forEach {
        result += it
    }
    c.send(result)
}

fun main(args: Array<String>) = runBlocking {
    val values = arrayOf(7, 2, 8, -9, 4, 0)
    val channel = Channel<Int>()

    launch { sum (values.sliceArray(0..2), channel)}
    launch { sum (values.sliceArray(3..5), channel)}

    val x = channel.receive()
    val y = channel.receive()

    println("$x, $y, ${x + y}")
}