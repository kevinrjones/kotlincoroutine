import kotlinx.coroutines.experimental.runBlocking
import org.junit.Assert
import org.junit.Test


class SimpleTest {

    // 3. SHow test does not work
    @Test
    fun firstTest() {
//        doWork()
        Assert.assertEquals(2, 1 + 1)
    }

    @Test
    fun secondTest() = runBlocking {
        doWork()
        Assert.assertEquals(2, 1 + 1)
    }
}