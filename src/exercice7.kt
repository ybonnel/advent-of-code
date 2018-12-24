fun main(args: Array<String>) {
    val constraints = String::class.java.getResourceAsStream("/exercice7.txt").use { stream ->
        stream.bufferedReader().readLines().map {
            it.substring(5, 6) to it.substring(36, 37)
        }
    }

    val letters = constraints.flatMap {
        listOf(
                it.first,
                it.second
        )
    }.toSet()

    val mapOfConstraints = letters.map {
        it to mutableSetOf<String>()
    }.toMap(mutableMapOf())

    for (constaint in constraints) {
        mapOfConstraints[constaint.second]!!.add(constaint.first)
    }
    // First Part
    val lettersToExplore = letters.toMutableSet()

    var result = ""

    while (lettersToExplore.isNotEmpty()) {

        val letterToExplore = mapOfConstraints.filter { it.value.isEmpty() }.map { it.key }.sorted().first()

        result += letterToExplore

        lettersToExplore.remove(letterToExplore)

        mapOfConstraints.remove(letterToExplore)
        mapOfConstraints.values.forEach {
            it.remove(letterToExplore)
        }

    }
    println(result)

    // Part two
    letters.map {
        it to mutableSetOf<String>()
    }.forEach {
        mapOfConstraints.put(it.first, it.second)
    }

    for (constaint in constraints) {
        mapOfConstraints[constaint.second]!!.add(constaint.first)
    }

    letters.forEach{
        lettersToExplore.add(it)
    }

    val workers = listOf(
            Worker(),
            Worker(),
            Worker(),
            Worker(),
            Worker()
    )

    var time = 0

    while (lettersToExplore.isNotEmpty()) {
        val letterToExplore = mapOfConstraints.filter { it.value.isEmpty() }.map { it.key }.sorted().toMutableList()

        val freeWorkers = workers.filter { it.isFree()}.toMutableList()


        while (letterToExplore.isNotEmpty() && freeWorkers.isNotEmpty()) {
            val letter = letterToExplore.removeAt(0)
            freeWorkers.removeAt(0).addWork(letter)
            mapOfConstraints.remove(letter)
        }

        println("$time : ${workers.map { it.letter }}")

        time++

        workers.map{ it.work() }.filter { it != null }.forEach { finishedLetter ->

            println("Remove : $finishedLetter")
            lettersToExplore.remove(finishedLetter)
            mapOfConstraints.values.forEach {
                it.remove(finishedLetter)
            }
        }

    }


    println(time)

}

data class Worker(
        var letter: String? = null,
        var time: Int = 0
) {
    fun isFree(): Boolean {
        return letter == null
    }

    fun addWork(letter: String) {
        this.letter = letter
        this.time = 60 + 1 + letter[0].minus('A')
    }

    fun work(): String? {
        if (letter != null) {
            time--
            if (time == 0) {
                val oldLetter = letter
                letter = null
                return oldLetter
            }
        }
        return null
    }

    fun currentTime(): Int {
        return time
    }
}