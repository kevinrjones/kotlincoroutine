import kotlinx.coroutines.experimental.channels.ReceiveChannel
import kotlinx.coroutines.experimental.channels.produce
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.runBlocking
import kotlinx.coroutines.experimental.selects.select

fun producer1() = produce {
    while(true) {
        delay(200)
        send("message1")
    }
}

fun producer2() = produce {
    while(true) {
        delay(300)
        send("message2")
    }
}

suspend fun selector(message1: ReceiveChannel<String>, message2: ReceiveChannel<String>) {
    select<Unit> {
        message1.onReceive { value ->
            println(value)
        }
        message2.onReceive { value ->
            println(value)
        }
    }
}

fun main(args: Array<String>) = runBlocking<Unit> {
    val m1 = producer1()
    val m2 = producer2()

    repeat(2) {
        selector(m1, m2)
    }
    //delay(1000)
}
