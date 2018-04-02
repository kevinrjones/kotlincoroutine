import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.sync.Mutex
import kotlinx.coroutines.experimental.sync.withLock
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.experimental.CoroutineContext
import kotlin.system.measureTimeMillis

open class Counter {
    private var counter = 0


    open suspend fun increment() {
        counter++
    }

    open var value: Int
        get() = counter
        set(value: Int) {
            counter = value
        }

    suspend fun massiveRun(context: CoroutineContext, n: Int, k: Int, action: suspend () -> Unit): Long {
        // action is repeated by each coroutine
        val time = measureTimeMillis {
            val jobs = List(n) {
                launch(context) {
                    repeat(k) { action() }
                }
            }
            jobs.forEach { it.join() }
        }
        return time
    }

}

class AtomicCounter : Counter() {
    var counter = AtomicInteger()

    override suspend fun increment() {
        counter.incrementAndGet()
    }

    override var value: Int
        get() = counter.get()
        set(value) {
            counter.set(value)
        }
}

class MutexCounter : Counter() {
    val mutex = Mutex()
    var counter:Int = 0

    override  suspend fun increment() {
        mutex.withLock {
            counter++
        }
    }

    override var value: Int
        get() = counter
        set(value) {
            counter = value
        }
}


fun main(args: Array<String>) = runBlocking<Unit> {
    val n = 1000 // number of coroutines to launch
    val k = 1000 // times an a
    var c = Counter()
    c.massiveRun(CommonPool, n, k) {
        c.increment()
    }

    c.value = 0

    var time = c.massiveRun(CommonPool, n, k) {
        c.increment()
    }
    logResult("Base counter", n, k, time, c)

    c.value = 0

    // use single thread context - fine grained
    val ctx = newSingleThreadContext("Counter")
    time = c.massiveRun(CommonPool, n, k) {
        withContext(ctx) {
            c.increment()
        }
    }
    logResult("Fine grained", n, k, time, c)

    c.value = 0

    // use single thread context - course grained
    time = c.massiveRun(ctx, n, k) {
        c.increment()
    }
    logResult("Course grained", n, k, time, c)

    c = AtomicCounter()
    time = c.massiveRun(CommonPool, n, k) {
        c.increment()
    }
    logResult("Atomic", n, k, time, c)


    c = MutexCounter()
    time = c.massiveRun(CommonPool, n, k) {
        c.increment()
    }
    logResult("Mutex", n, k, time, c)
}

private fun logResult(counterType: String, n: Int, k: Int, time: Long, c: Counter) {
    println("${counterType} completed ${n * k} actions in $time ms")
    println("Counter  : ${c.value}")
}

fun log(msg: String) = println("[${Thread.currentThread().name}] $msg")
