import kotlin.streams.asSequence

data class Count(
        val two: Int,
        val three: Int
) {
    operator fun plus(count: Count): Count {
        return Count(
                this.two + count.two,
                this.three + count.three
        )
    }

    fun countOnce(): Count {
        return Count(
                Math.min(this.two, 1),
                Math.min(this.three, 1)
        )
    }
}

val two = Count(1, 0)
val three = Count(0, 1)
val zero = Count(0, 0)

fun String.diffByOne(other: String): Boolean {
    return if (other.length != this.length) {
        false
    } else {
        other.toCharArray().filterIndexed { index, car ->
            car != this[index]
        }.size == 1
    }
}

fun main(args: Array<String>) {
    // First
    val count = String::class.java.getResourceAsStream("/exercice2.txt").use { stream ->
        stream.bufferedReader().lines().asSequence().map { line ->
            line.chars().asSequence().groupBy { it }.map {(_, list) ->
                when (list.size) {
                    2 -> two
                    3 -> three
                    else -> zero
                }
            }.fold(zero) { acc, count ->
                acc + count
            }.countOnce()
        }.fold(zero) { acc, count ->
            acc + count
        }
    }
    println(count.two * count.three)

    // Part two
    val words = String::class.java.getResourceAsStream("/exercice2.txt").use {
        it.bufferedReader().readLines()
    }

    val result = words.asSequence().map {
        it to words.find { other -> other.diffByOne(it) }
    }.filter {
        it.second != null
    }.map {
        it.first to it.second!!
    }.first()

    print(result.first.toCharArray().filterIndexed{index, car ->
        result.second[index] == car
    }.joinToString(""))
}