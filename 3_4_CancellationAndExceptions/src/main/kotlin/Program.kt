import kotlinx.coroutines.experimental.*

// 1. Use a standard try..finally
fun main(args: Array<String>) = runBlocking() {
    val job = launch {
        try {
            repeat(1000) {
                yield()

                print(".")
                Thread.sleep(1)
            }
        } finally {
            run(NonCancellable) {
                delay(1000)
                println()
                println("In finally")
            }
        }
    }

    delay(100)
    job.cancelAndJoin()
}


// 1. Use a standard try..finally
//fun main(args: Array<String>) = runBlocking() {
//    val job = launch {
//        try {
//            repeat(1000) {
//                yield()
//
//                print(".")
//                Thread.sleep(1)
//            }
//        } finally {
//            println()
//            println("In finally")
//        }
//    }
//
//    delay(100)
//    job.cancelAndJoin()
//}
//
