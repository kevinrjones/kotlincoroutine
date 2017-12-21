import kotlinx.coroutines.experimental.*


// 1. problem with this code is that the singleThreadContext is expensive
//fun main(args: Array<String>) = runBlocking {
//
//
//    val job = launch(newSingleThreadContext("SingleThreadContext")) {
//        // not confined -- will work with main thread
//        println("'SingleThreadContext': I'm working in thread ${Thread.currentThread().name}")
//    }
//
//    job.join()
//
//}

// 2. maybe use this instead
fun main(args: Array<String>) = runBlocking {


    newSingleThreadContext("SingleThreadContext").use { ctx ->
        val job = run(ctx) {

            println("'SingleThreadContext': I'm working in thread ${Thread.currentThread().name}")
        }

//        job.join()
    }

}
