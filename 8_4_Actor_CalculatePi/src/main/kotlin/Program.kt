import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.actor

sealed class PiMessage
class Start(val response: CompletableDeferred<Double>, val workers: Long) : PiMessage()
class Work(var channel: Channel<PiMessage>, val start: Long, val iterations: Long) : PiMessage()
class Result(val result: Double) : PiMessage()

fun piActor() = actor<Work> {
    var total = 0.0

    for(msg in channel) {
        val start: Long = msg.start * msg.iterations
        val end: Long = (msg.start + 1) * msg.iterations
        for (i in start until end) {
            total += 4.0 * (1 - (i % 2) * 2) / (2 * i + 1)
        }
        msg.channel.send(Result(total))
    }
}


fun workerActor() = actor<PiMessage> {
    var response: CompletableDeferred<Double>? = null
    var total = 0.0
    var workers: Long = 0
    var finished: Long = 0

    val iterations: Long = 400000000

    for (msg in channel) {
        when (msg) {
            is Start -> {
                response = msg.response
                workers = msg.workers
                val range: Long = iterations / workers
                for (i in (0 until workers)) {
                    val start: Long = i * range
                    val end: Long = ((i + 1) * range) - 1
                    piActor().send(Work(channel, start, end))
                }
            }
            is Result -> {
                finished++
                total += msg.result
                if (finished == workers) {
                    response?.complete(total)
                }
            }
        }
    }
}

@Suppress("RemoveExplicitTypeArguments")
fun main(args: Array<String>) = runBlocking<Unit> {
    val response = CompletableDeferred<Double>()


    workerActor().send(Start(response, 1))
    log("wait for response")
    println("${response.await()}")

}

fun log(msg: String) = println("[${Thread.currentThread().name}] $msg")

