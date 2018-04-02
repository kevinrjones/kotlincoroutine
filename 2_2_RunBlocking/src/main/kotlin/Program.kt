import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking


// 2. Use runBlocking
// Mention that this is often used in tests, but we need to look at suspending functions first
fun main(args: Array<String>) = runBlocking<Unit> {

    launch {
        delay(1000)
        println("World")
    }

    print("Hello, ")

    delay(2000)

}


//fun main(args: Array<String>)  {
//
//    launch {
//        delay(1000)
//        println("World")
//    }
//
//    print("Hello, ")
//
//    // 1. Use runBlocking
//    runBlocking {
//        delay(2000)
//    }
//}
