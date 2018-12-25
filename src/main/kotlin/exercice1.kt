import java.io.BufferedReader
import java.util.stream.Collectors

fun main(args: Array<String>) {
    // First
    val result = String::class.java.getResourceAsStream("/exercice1.txt").use { stream ->
        stream.bufferedReader().lines().mapToLong {
            it.toLong()
        }.sum()
    }
    println(result)

    // Second
    val numbers = String::class.java.getResourceAsStream("/exercice1.txt").use { stream ->
        stream.bufferedReader().lines().map {
            it.toLong()
        }.collect(Collectors.toList())
    }

    val freqs = mutableSetOf<Long>()

    var lastFreq: Long = 0

    while (true) {
        for (freq in numbers) {
            lastFreq += freq
            if (freqs.contains(lastFreq)) {
                println(lastFreq)
                return
            }
            freqs.add(lastFreq)
        }
    }
}