import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalUnit

fun Char.inverseCase(): Char {
    return if (this.isUpperCase()) {
        this.toLowerCase()
    } else {
        this.toUpperCase()
    }
}

fun String.react(): String {
    var lastIndex = -1

    for (index in 0 until this.length -1) {
        val car = this[index]
        if (car.inverseCase() == this[index + 1]){
            lastIndex = index
            break
        }
    }
    return if (lastIndex == -1) {
        this
    } else {
        this.substring(0, lastIndex) + this.substring(lastIndex + 2, this.length)
    }
}

fun reactPoylmer(polymer: String): Int {
    var polymerReacted = polymer
    var nextPolymer = polymer.react()
    while (polymerReacted != nextPolymer) {
        polymerReacted = nextPolymer
        nextPolymer = nextPolymer.react()
    }
    return polymerReacted.length
}

fun main(args: Array<String>) {

    val polymer = String::class.java.getResourceAsStream("/exercice5.txt").use {
        it.bufferedReader().readLines()
    }.first()

    // Part one
    println(reactPoylmer(polymer))

    // Part two
    val result = 'a'.rangeTo('z').map {
        println("remove $it")
        it to reactPoylmer(polymer.replace(it.toString(), "", true))
    }.minBy {
        it.second
    }
    println(result?.second)

}