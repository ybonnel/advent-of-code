data class SquareWithSize(
        val position: Pair<Int, Int>,
        val size: Int
) {
    fun power(cells: MutableMap<Int, MutableMap<Int, MutableMap<Int, Int>>>): Int {
        val power = if (cells.containsKey(size - 1)) {
            val oldPower = cells[size - 1]!![position.first]!![position.second]!!
            val xPowers = (0 until size).map { cells[1]!![position.first+it]!![position.second+size - 1]!! }.sum()
            val yPower = (0..size -2).map {
                    cells[1]!![position.first+size-1]!![position.second+it]!!
                }.ifEmpty {
                    listOf(0)
                }.sum()
            oldPower + xPowers + yPower
        } else {
            (0..(size-1)).flatMap { x ->
                (0..(size-1)).map { y ->
                    cells[1]!![position.first+x]!![position.second+y]!!
                }
            }.sum()
        }
        if (size != 1) {
            cells.putIfAbsent(size, mutableMapOf())
            cells[size]!!.putIfAbsent(position.first, mutableMapOf())
            cells[size]!![position.first]!!.put(position.second, power)
        }
        return power
    }
}

fun main(args: Array<String>) {

    val serialNum = 6392

    val cells = (1..300).map { x ->
        x to (1..300).map { y ->
            y to power(x, y, serialNum)
        }.toMap().toMutableMap()
    }.toMap()

    // First part
    val firstResult = (1..298).flatMap { x ->
        (1..298).map { y ->
            x to y
        }
    }.asSequence().map {
        it to (0..2).flatMap { x ->
            (0..2).map { y ->
                cells[it.first+x]!![it.second+y]!!
            }
        }.sum()
    }.maxBy {
        it.second
    }
    println(firstResult)

    val cellsBySize = mutableMapOf(1 to cells.toMutableMap())

    // Second part
    val secondResult = (1..300).asSequence().flatMap { size ->
        println(size)
        if (size > 3) {
            cellsBySize.remove(size -2)
        }
        (1..(301-size)).asSequence().flatMap { x ->
            (1..(301-size)).asSequence().map { y ->
                SquareWithSize(
                        x to y,
                        size
                )
            }
        }
    }.map {
        it to it.power(cellsBySize)
    }.maxBy { it.second }
    println(secondResult)
}

private fun power(x: Int, y: Int, serialNum: Int): Int {
    val rackId = x + 10
    val powerLevelStart = rackId * y
    val withSerial = powerLevelStart + serialNum
    val multRackID = withSerial * rackId
    val hundredDigit = (multRackID / 100) % 10
    return hundredDigit - 5
}


