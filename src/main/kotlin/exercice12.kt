import java.util.concurrent.TimeUnit

fun IntArray.valAtIndex(index: Int): Int {
    var sum = 0
    var subIndex = 0
    while (subIndex < 5) {
        val it = index - 2 + subIndex
        if (it >= 0 && it < this.size && this[it] == 1) {
            sum += powerOfTwo[subIndex]
        }
        subIndex++
    }
    return sum
}


val powerOfTwo = (0..4).map {
    Math.pow(2.toDouble(), it.toDouble()).toInt()
}.toIntArray()


fun IntArray.evol(grows: IntArray, left: Long): Pair<IntArray, Long> {

    val output = createOutput(grows)

    val trimLeft = calculateTrimLeft(output)

    val trimRight = calculateTrimRight(output)

    return output.copyOfRange(
            trimLeft, output.size - trimRight
    ) to left - 2 + trimLeft
}



private fun calculateTrimRight(output: IntArray): Int {
    var trimRight = 0
    while (output[output.size - 1 - trimRight] == 0) {
        trimRight++
    }
    return trimRight
}

private fun calculateTrimLeft(output: IntArray): Int {
    var trimLeft = 0
    while (output[trimLeft] == 0) {
        trimLeft++
    }
    return trimLeft
}

private fun IntArray.createOutput(grows: IntArray): IntArray {
    return IntArray(this.size + 4) { index ->
        grows[this.valAtIndex(index - 2)]
    }
}

fun IntArray.print(generation: Long, left: Long) {
    println("$generation : ${this.map {
        if (it == 1) {
            "#"
        } else {
            "."
        }
    }.joinToString("")} (size=${this.size}, left=$left)")
}

fun main(args: Array<String>) {

    val initialString = "##...#......##......#.####.##.#..#..####.#.######.##..#.####...##....#.#.####.####.#..#.######.##"
    val initial = initialString.map {
        if (it == '#') {
            1
        } else {
            0
        }
    }.toIntArray()

    val grows = setOf(
            "#..##",
            "...##",
            ".#...",
            ".##.#",
            "###.#",
            "#####",
            "##..#",
            "#.###",
            ".#..#",
            ".##..",
            "##...",
            ".#.#.",
            ".###.",
            ".####",
            "#.##.",
            "#...#"
    )

    val growsByNumer = IntArray(32) {
        0
    }
    for (grow in grows) {
        val number = grow.mapIndexed { index, car ->
            if (car == '#') {
                powerOfTwo[index]
            } else {
                0
            }
        }.sum()
        growsByNumer[number] = 1
    }

    var state = initial
    var left = 0L
    var startTime = System.nanoTime()
    state.print(0, left)
    var generation = 0L
    var lastState = ""
    var lastLeft = left
    while (generation < 50000000000L) {
        val string = String(CharArray(state.size) {
            if(state[it] == 1) {
                '#'
            } else {
                '.'
            }
        })
        if (lastState == string) {
            val incLeft = (left - lastLeft)
            while (generation < 50000000000L) {
                left += incLeft
                generation++
            }
        } else {
            lastState = string
            lastLeft = left
            state.evol(growsByNumer, left).let {
                state = it.first
                left = it.second
            }
            generation++
        }
        //state.print(generation, left)
        if (generation % 1000000L == 0L) {
            println("${generation / 1000000}/${50000000000L / 1000000L} (${TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime)}) (size=${state.size}, left=${left})")
            startTime = System.nanoTime()
        }
    }
    println(state.mapIndexed { index, car ->
        (index + left) * car
    }.sum())
}

