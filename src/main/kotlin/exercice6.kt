fun main(args: Array<String>) {

    val coords = String::class.java.getResourceAsStream("/exercice6.txt").use {
        it.bufferedReader().readLines()
    }.map {
        it.split(", ")
    }.map {
        it[0].toInt() to it[1].toInt()
    }

    val xMax = coords.map { it.first }.max()!!
    val xMin = coords.map { it.first }.min()!!
    val yMax = coords.map { it.second }.max()!!
    val yMin = coords.map { it.second }.min()!!
    val xAvg = (xMax + xMin) / 2
    val yAvg = (yMax + yMin) / 2

    val infinitePoints = (-1000..1000).flatMap {
        listOf(
                // Left
                xAvg - 10000 to it + yAvg,
                // Top
                it + xAvg to yAvg - 10000,
                // Right
                xAvg + 10000 to it + yAvg,
                // Bottom
                xAvg + it to yAvg + 10000
        )
    }

    // Part one
    val infiniteCoords = infinitePoints.map { infinitPoint ->
        coords.minBy {
            Math.abs(it.first - infinitPoint.first) + Math.abs(it.second - infinitPoint.second)
        }!!
    }.toSet()

    println(infiniteCoords)

    val result = coords.filter {
        !infiniteCoords.contains(it)
    }.map { origin ->
        origin to origin.expand(coords)
    }

    println(result.maxBy { it.second })

    // Part two

    println((xAvg to yAvg).expendSafe(coords))

}

fun MutableList<Pair<Int, Int>>.addIfNotExplored(toAdd: Pair<Int, Int>, alreadyExplore: MutableSet<Pair<Int, Int>>) {
    if (!alreadyExplore.contains(toAdd)) {
        this.add(toAdd)
        alreadyExplore.add(toAdd)
    }
}

fun Pair<Int, Int>.toLeft(): Pair<Int, Int> {
    return this.copy(
            first = first - 1
    )
}

fun Pair<Int, Int>.toTop(): Pair<Int, Int> {
    return this.copy(
            second = second - 1
    )
}

fun Pair<Int, Int>.toRight(): Pair<Int, Int> {
    return this.copy(
            first = first + 1
    )
}

fun Pair<Int, Int>.toBottom(): Pair<Int, Int> {
    return this.copy(
            second = second + 1
    )
}

fun Pair<Int, Int>.expendSafe(coords: List<Pair<Int, Int>>): Int {


    var nb = 0
    val toExplore = mutableListOf(this)
    val alreadyExplore = mutableSetOf(this)

    while (toExplore.isNotEmpty()) {
        val toTest = toExplore.removeAt(0)

        val distanceSum = coords.map {
            Math.abs(it.first - toTest.first) + Math.abs(it.second - toTest.second)
        }.sum()

        if (distanceSum < 10000) {
            nb++
            toExplore.addIfNotExplored(toTest.toLeft(), alreadyExplore)
            toExplore.addIfNotExplored(toTest.toTop(), alreadyExplore)
            toExplore.addIfNotExplored(toTest.toRight(), alreadyExplore)
            toExplore.addIfNotExplored(toTest.toBottom(), alreadyExplore)
        }
    }

    return nb
}

fun Pair<Int, Int>.expand(coords: List<Pair<Int, Int>>): Int {
    var nb = 0
    val toExplore = mutableListOf(this)
    val alreadyExplore = mutableSetOf(this)

    while (toExplore.isNotEmpty()) {
        val toTest = toExplore.removeAt(0)

        val distances = coords.map { it ->
            it to Math.abs(it.first - toTest.first) + Math.abs(it.second - toTest.second)
        }

        val min = distances.minBy {
            it.second
        }!!

        if (min.first == this) {
            if (distances.filter { it.second == min.second }.size == 1) {
                nb++
            }
            toExplore.addIfNotExplored(toTest.toLeft(), alreadyExplore)
            toExplore.addIfNotExplored(toTest.toTop(), alreadyExplore)
            toExplore.addIfNotExplored(toTest.toRight(), alreadyExplore)
            toExplore.addIfNotExplored(toTest.toBottom(), alreadyExplore)
        }
    }

    return nb
}