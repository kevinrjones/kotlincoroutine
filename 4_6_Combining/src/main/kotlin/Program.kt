import kotlinx.coroutines.experimental.*

// 1. combining to give a context a name
//fun main(args: Array<String>) = runBlocking {
//    println("     'runBlocking': I'm working in thread ${Thread.currentThread().name}")
//
//
//    val job = launch(CoroutineName("kevin-context") + coroutineContext) {
//        // not confined -- will work with main thread
//        println("'coroutineContext': I'm working in thread ${Thread.currentThread().name}")
//    }
//
//    job.join()
//
//}

// 2. Combining behaviour
fun main(args: Array<String>) = runBlocking {
    println("     'runBlocking': I'm working in thread ${Thread.currentThread().name}")


    val jobs = arrayListOf<Job>()
    jobs += launch(coroutineContext) {
        // not confined -- will work with main thread
        println("'coroutineContext': I'm working in thread ${Thread.currentThread().name}")
    }

    jobs += launch(coroutineContext + CommonPool) {
        delay(1)
        // not confined -- will work with main thread
        println("        'combined': I'm working in thread ${Thread.currentThread().name}")
    }

    jobs.forEach{it -> it.join()}

}

