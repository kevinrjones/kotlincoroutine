import kotlinx.coroutines.experimental.channels.ReceiveChannel
import kotlinx.coroutines.experimental.channels.produce
import kotlinx.coroutines.experimental.runBlocking
import kotlinx.coroutines.experimental.selects.select

// send one message (thien close)
fun producer1() = produce {
    send("message1")
}

// send one message (thien close)
fun producer2() = produce {
    send("message2")
}

// select 1 then 2
suspend fun selector1(message1: ReceiveChannel<String>, message2: ReceiveChannel<String>): String {
    return select<String> {
        message1.onReceiveOrNull { value ->
            value ?: "Channel 1 closed"
        }
        message2.onReceiveOrNull { value ->
            value ?: "Channel 2 closed"
        }
    }
}

// select 2 then 1
suspend fun selector2(message1: ReceiveChannel<String>, message2: ReceiveChannel<String>): String =
        select<String> {
            message2.onReceiveOrNull { value ->
                value ?: "Channel 2 closed"
            }
            message1.onReceiveOrNull { value ->
                value ?: "Channel 1 closed"
            }
        }


fun main(args: Array<String>) = runBlocking<Unit> {
    val m1 = producer1()
    val m2 = producer2()

    repeat(3) {
        println(selector1(m1, m2))
    }

    repeat(3) {
        println(selector2(m1, m2))
    }
}
