
import java.util.concurrent.ForkJoinPool
import java.util.concurrent.RecursiveTask


internal class Sum(private var array: IntArray, private var low: Int, private var high: Int) : RecursiveTask<Long>() {

    override fun compute(): Long {
        println("low: $low, high: $high  on ${Thread.currentThread().name}")

        return if (high - low <= SEQUENTIAL_THRESHOLD) {
            (low until high)
                    .map { array[it].toLong() }
                    .sum()
        } else {
            val mid = low + (high - low) / 2
            val left = Sum(array, low, mid)
            val right = Sum(array, mid, high)
            left.fork()
            val rightAns = right.compute()
            val leftAns = left.join()
            leftAns + rightAns
        }
    }

    companion object {
        val SEQUENTIAL_THRESHOLD = 5000

        fun sumArray(array: IntArray): Long {
            return ForkJoinPool.commonPool().invoke(Sum(array, 0, array.size))
        }
    }
}

fun main(args: Array<String>) {
    val list = mutableListOf<Int>()

    var limit = 200_000

    while (limit > 0) {
        list.add(limit--)
    }

    val result = Sum.sumArray(list.toIntArray())


    print(result)
}