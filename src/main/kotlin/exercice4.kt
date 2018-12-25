import java.time.LocalDateTime

fun main(args: Array<String>) {

    val lines = String::class.java.getResourceAsStream("/exercice4.txt").use {
        it.bufferedReader().readLines()
    }.sorted().toList()

    var lastGardianId = 0
    var lastSleep = LocalDateTime.now()
    val sleepByGuardian = mutableMapOf<Int, MutableMap<Int, Int>>()

    for (line in lines) {
        val data = line.substring(19)
        val time = line.substring(1, 17).replace(" ", "T") + ":00"
        println("data=$data, time=${time}")
        when (data) {
            "falls asleep" -> lastSleep = LocalDateTime.parse(time)
            "wakes up" -> {
                val wakeTime = LocalDateTime.parse(time)
                sleepByGuardian.putIfAbsent(lastGardianId, mutableMapOf())
                while (!lastSleep.isEqual(wakeTime)) {
                    val lastSleepMinutes = lastSleep.hour * 60 + lastSleep.minute

                    sleepByGuardian[lastGardianId]!!.putIfAbsent(lastSleepMinutes, 0)
                    sleepByGuardian[lastGardianId]!![lastSleepMinutes] =
                            sleepByGuardian[lastGardianId]!![lastSleepMinutes]!! + 1

                    lastSleep = lastSleep.plusMinutes(1)
                }

            }
            else -> {
                lastGardianId = data.split(" ")[1].replace("#", "").toInt()
            }
        }
    }

    val maxGardian = sleepByGuardian.maxBy {
        it.value.values.sum()
    }!!.key

    val maxMinute = sleepByGuardian[maxGardian]!!.maxBy {
        it.value
    }!!.key

    println("$maxGardian x $maxMinute = ${maxGardian * maxMinute}")

    // Part two
    val (gardianId, minute) = sleepByGuardian.maxBy {
        it.value.values.max()!!
    }?.let {
        it.key to it.value.maxBy {
            it.value
        }!!.key
    }!!

    println("$gardianId x $minute = ${gardianId * minute}")

}