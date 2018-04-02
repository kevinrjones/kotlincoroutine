import kotlinx.coroutines.experimental.*
import kotlin.coroutines.experimental.CoroutineContext
import kotlin.coroutines.experimental.intrinsics.*

fun main(args: Array<String>) = runBlocking() {
    val job = launch {
        // 1. non-cooperative, no cancellation
        //doWorkNonCooperative()
        // 2. WOrk and yield
        //doWorkAndYield()
        // 3. Checking state
        doWorkAndCheck()
    }

    delay(100)
    job.cancelAndJoin()
}

// 1. Non-cooperative
suspend fun doWorkNonCooperative() {
    repeat(1000) {
        print(".")
        Thread.sleep(1)
    }
}

// 2. Yield function
suspend fun doWorkAndYield() {
    repeat(1000) {
        print(".")
        yield()
        Thread.sleep(1)
    }
}

// 3. See Cooperate.kt


suspend fun coroutineContext(): CoroutineContext = suspendCoroutineOrReturn { cont -> cont.context }

// 4. Checking state - not for this lab - later when we do contexts
suspend fun doWorkAndCheck() {
    val ctx = CommonPool + coroutineContext()
    repeat(1000) {
        print(".")

        if(!ctx[Job]!!.isActive) throw CancellationException()
        Thread.sleep(1)
    }
}
