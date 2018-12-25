fun main(args: Array<String>) {
    val map = IntRange(0, 1000).map { x ->
        x to IntRange(0, 1000).map { y ->
            y to mutableListOf<Int>()
        }.toMap()
    }.toMap()

    val claims = String::class.java.getResourceAsStream("/exercice3.txt").use {
        it.bufferedReader().readLines()
    }

    val ids = mutableListOf<Int>()
    // First
    for (claim in claims) {
        //#13 @ 508,21: 10x24
        val columns = claim.split(" ")
        val id = columns[0].replace("#", "").toInt()
        ids.add(id)
        val coord = columns[2].replace(":", "").split(",").let {
            it[0].toInt() to it[1].toInt()
        }
        val size= columns[3].split("x").let {
            it[0].toInt() to it[1].toInt()
        }
        for (x in coord.first until coord.first + size.first) {
            for (y in coord.second until coord.second + size.second) {
                map[x]!![y]!!.add(id)
            }
        }
    }

    println(
            map.values.map { yCoords ->
                yCoords.values.filter { it.size > 1 }.count()
            }.sum()
    )

    // Part two
    val idsOverlaps = map.values.map { yCoords ->
        yCoords.values.filter { it.size > 1 }.flatMap {
            it.toList()
        }.toSet()
    }.flatMap {
        it
    }.toSet()
    println(ids.first {
        !idsOverlaps.contains(it)
    })
}