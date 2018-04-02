import kotlinx.coroutines.experimental.channels.ReceiveChannel
import kotlinx.coroutines.experimental.channels.produce
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.runBlocking
import kotlinx.coroutines.experimental.selects.SelectBuilder
import kotlinx.coroutines.experimental.selects.select

fun producer1() = produce {
    while (true) {
        delay(200)
        send("message1")
    }
}


fun main(args: Array<String>) = runBlocking<Unit> {
    var msg = producer1().receive()

    select<Unit> {

        producer1().onReceive { value ->
            when(value) {
                is String ->            println(value)
                else -> println("default")
            }
        }
        default {
            println("default")
        }
    }


    var value1 = producer1().poll()
    when (value1) {
        is String -> println(value1)
        else -> {
            println("default")
        }
    }

    /*
    producer1().poll()?.also { value1 -> /* do something with it */ } ?: // reads as "or else"
    producer2().poll()?.also { value2 -> /* do something with it */ } ?:
    run { /* default */ }
    * */
}

fun <R> SelectBuilder<in R>.default(block: suspend () -> R) {
    onTimeout(0) { block() }
}

