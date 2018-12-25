import org.apache.commons.collections4.list.TreeList
import java.util.*

fun main(args: Array<String>) {

    println(calculate(9, 25))
    println(calculate(10, 1618))
    println(calculate(13, 7999))
    println(calculate(17, 1104))
    println(calculate(21, 6111))
    println(calculate(30, 5807))
    println(calculate(462, 71938))
    println(calculate(462, 7193800))

}

fun calculate(nbPlayers: Long, lastMarble: Long): Long {

    val playersScore = (1..nbPlayers).map {
        it to 0L
    }.toMap(mutableMapOf())
    val marbles = TreeList<Long>()
    marbles.add(0)
    var currentPlayer = 1L
    var currentMarble = 1L
    var currentPosition = 0



    while (currentMarble <= lastMarble) {
        if (currentMarble % 23L == 0L) {
            currentPosition -= 7
            playersScore.put(currentPlayer, playersScore.getOrDefault(currentPlayer, 0L) + currentMarble)
            while (currentPosition < 0) {
                currentPosition += marbles.size
            }
            playersScore.put(currentPlayer, playersScore.getOrDefault(currentPlayer, 0L) + marbles.removeAt(currentPosition))
        } else {
            currentPosition+=2
            while (currentPosition >= marbles.size) {
                currentPosition -= marbles.size
            }
            marbles.add(currentPosition, currentMarble)
        }
//        println(marbles.mapIndexed { index, marble ->
//            if (index == currentPosition) {
//                "($marble)"
//            } else {
//                " $marble "
//            }
//        }.joinToString(""))

        currentMarble++
        currentPlayer++
        if (currentPlayer > nbPlayers) {
            currentPlayer = 1
        }
    }

    return playersScore.values.max()!!
}

