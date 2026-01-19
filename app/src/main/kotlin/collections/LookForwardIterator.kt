package collections

import java.util.function.Predicate

class LookForwardIterator<T>(
    private val iter: Iterator<T>,
    private val predicate: Predicate<T>? = null
) {
    private val buffer = ArrayDeque<T>()
    private var count: Int = -1

    private fun prefetch(n: Int) {
        if (n < 0) throw RuntimeException("only could look forward")

        if (count == -1) {
            getNextFilteredItem()?.let { item ->
                buffer.addLast(item)
                count = 1
            } ?: run {
                count = 0
            }
        }

        while (count <= n) {
            getNextFilteredItem()?.let { item ->
                buffer.addLast(item)
                count++
            } ?: break
        }
    }

    private fun getNextFilteredItem(): T? {
        // 不存在过滤规则
        predicate ?: run {
            return if (iter.hasNext()) iter.next() else null
        }

        // 返回下一个规则内的item
        while (iter.hasNext()) {
            val item = iter.next()
            if (predicate.test(item)) {
                return item
            }
        }
        return null
    }


    fun hasNext(n: Int = 0): Boolean {
        if (n < 0) throw RuntimeException("only could look forward")

        prefetch(n + 1)
        return count > n
    }

    fun cur(n: Int = 0): T {
        prefetch(n)

        if (n < count) {
            return buffer[n]
        } else {
            throw RuntimeException("not enough elements")
        }
    }

    fun moveNext(n: Int = 1) {
        if (buffer.isNotEmpty()) buffer.removeFirst()

        count -= n
        if (count < 0) {
            count = 0
        }

        prefetch(n)
    }

    fun word(s: String): Boolean {
        for (i in s.indices) {
            if (!hasNext(i) || cur(i) != s[i]) {
                return false
            }
        }
        return true
    }

    companion object {
        fun <T> from(base: Iterator<T>, predicate: Predicate<T>): LookForwardIterator<T> {
            return LookForwardIterator(base, predicate)
        }
    }
}