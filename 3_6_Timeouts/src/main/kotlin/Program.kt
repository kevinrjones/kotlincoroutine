import kotlinx.coroutines.experimental.*

// 4. withTimeoutOrNull
fun main(args: Array<String>) = runBlocking() {
    val job = withTimeoutOrNull(100) {

        repeat(1000) {
            yield()

            print(".")
            Thread.sleep(1)
        }

    }

    delay(100)

    println()
    if (job != null) {
        println("job completed")
    } else {
        println("timeout")
    }

}


// 3. now the exception is handled
//fun main(args: Array<String>) = runBlocking() {
//    try {
//        val job = withTimeout(100) {
//
//            repeat(1000) {
//                yield()
//
//                print(".")
//                Thread.sleep(1)
//            }
//        }
//    } catch (ex: TimeoutCancellationException) {
//        println()
//        println("Exception")
//    }
//
//    delay(100)
//
//}


// 2. withTimeout throws an exception even if the exception is handled inside the block
//fun main(args: Array<String>) = runBlocking() {
//    val job = withTimeout(100) {
//
//        try {
//            repeat(1000) {
//                yield()
//
//                print(".")
//                Thread.sleep(1)
//            }
//        } catch (ex: TimeoutCancellationException) {
//            println("Exception")
//        }
//    }
//
//    delay(100)
//
//}


// 1. withTimeout throws an exception
//fun main(args: Array<String>) = runBlocking() {
//    val job = withTimeout(100) {
//
//        repeat(1000) {
//            yield()
//
//            print(".")
//            Thread.sleep(1)
//        }
//    }
//
//    delay(100)
//
//}


