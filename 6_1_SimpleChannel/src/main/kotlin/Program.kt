import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking

// part1 - code with a channel.send, sends the data but nothing to receive it
fun main(args: Array<String>) = runBlocking {
    val channel = Channel<Int>()
    launch {
        // this might be heavy CPU-consuming computation or async logic, we'll just send five squares
        for (x in 1..5) {
            println("send $x")
            channel.send(x * x)
        }
    }

    // part2 - add the single receive

    println(channel.receive())

    // part3 - add the repeat

    repeat(4) { println(channel.receive()) }
}
