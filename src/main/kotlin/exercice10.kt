

fun Pair<Int, Int>.applyVelocity(velocity: Pair<Int, Int>): Pair<Int, Int> {
    return Pair(
            first + velocity.first,
            second + velocity.second
    )
}

data class Point(
        val position: Pair<Int, Int>,
        val velocity: Pair<Int, Int>
) {
    fun next(): Point {
        return this.copy(
                position = position.applyVelocity(velocity)
        )
    }
}

fun List<Point>.print() {
    val minX = this.map {
        it.position.first
    }.min()!!
    val maxX = this.map {
        it.position.first
    }.max()!!

    val minY = this.map {
        it.position.second
    }.min()!!
    val maxY = this.map {
        it.position.second
    }.max()!!

    val positions = this.map {
        it.position
    }.toSet()

    println((minY..maxY).map {y ->
        (minX..maxX).map { x ->
            if (positions.contains(x to y)) {
                "#"
            } else {
                "."
            }
        }.joinToString("")
    }.joinToString("\n"))
}
fun main(args: Array<String>) {

    val regexp = """position=< *(-?\d+), *(-?\d+)> velocity=< *(-?\d+), *(-?\d+)>""".toRegex()
    var datas = String::class.java.getResourceAsStream("/exercice10.txt").use {
        it.bufferedReader().readLines()
    }.map {
        val (posX, poxY, velX, velY) = regexp.find(it)!!.destructured
        Point(
                posX.toInt() to  poxY.toInt(),
                velX.toInt() to velY.toInt()
        )
    }

    var nbSeconds = 0L


    while (true) {
        val minX = datas.map {
            it.position.first
        }.min()!!
        val maxX = datas.map {
            it.position.first
        }.max()!!

        val minY = datas.map {
            it.position.second
        }.min()!!
        val maxY = datas.map {
            it.position.second
        }.max()!!
        if (maxX - minX < 100 && maxY - minY < 100) {
            println((0..80).map { "-" }.joinToString(""))
            println(nbSeconds)
            println((0..80).map { "-" }.joinToString(""))
            datas.print()
            Thread.sleep(500)
        }
        datas = datas.map {
            it.next()
        }
        nbSeconds++
    }
}


