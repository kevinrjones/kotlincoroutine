import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking


val SEQUENTIAL_THRESHOLD = 5000

suspend fun compute(array: IntArray, low: Int, high: Int): Long {

    println("low: $low, high: $high  on ${Thread.currentThread().name}")

    return if (high - low <= SEQUENTIAL_THRESHOLD) {
        (low until high)
                .map { array[it].toLong() }
                .sum()
    } else {
        val mid = low + (high - low) / 2
        val left = async {compute(array, low, mid)}
        val right = compute(array, mid, high)
        return left.await() + right
    }
}

fun main(args: Array<String>) = runBlocking {

    val list = mutableListOf<Int>()

    var limit = 200_000

    while (limit > 0) {
        list.add(limit--)
    }

    val result = compute(list.toIntArray(), 0, list.toIntArray().size)

    print(result)
}