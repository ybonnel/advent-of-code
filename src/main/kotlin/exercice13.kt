import java.lang.IllegalStateException
import java.util.*

enum class Movement(val move: (point: Pair<Int, Int>) -> Pair<Int, Int>, val car: Char) {
    UP({
        it.copy(second = it.second - 1)
    }, '^'),
    DOWN({
        it.copy(second = it.second + 1)
    }, 'v'),
    LEFT({
        it.copy(first = it.first - 1)
    }, '<'),
    RIGHT({
        it.copy(first = it.first + 1)
    }, '>')
}

enum class Turn(val next: () -> Turn, val nextMove: (Movement) -> Movement) {
    LEFT({
        Turn.STRAIGHT
    }, {
        when (it) {
            Movement.UP -> Movement.LEFT
            Movement.LEFT -> Movement.DOWN
            Movement.DOWN -> Movement.RIGHT
            Movement.RIGHT -> Movement.UP
        }
    }),
    STRAIGHT({
        Turn.RIGHT
    }, {
        it
    }),
    RIGHT({
        Turn.LEFT
    }, {
        when (it) {
            Movement.UP -> Movement.RIGHT
            Movement.RIGHT -> Movement.DOWN
            Movement.DOWN -> Movement.LEFT
            Movement.LEFT -> Movement.UP
        }
    })
}

data class Car(
        val point: Pair<Int, Int>,
        val movement: Movement,
        val currentTurn: Turn = Turn.LEFT,
        val pointBefore: Pair<Int, Int> = point,
        var crash: Boolean = false
)

abstract class PointOnMap(val car: Char) {
    abstract fun nextMove(movement: Movement): Movement

    fun moveCar(car: Car): Car {
        val nextMovement = if (this is Cross) {
            car.currentTurn.nextMove(car.movement)
        } else {
            this.nextMove(car.movement)
        }
        return Car(
                point = nextMovement.move(car.point),
                movement = nextMovement,
                currentTurn = if (this is Cross) {
                    car.currentTurn.next()
                } else {
                    car.currentTurn
                },
                pointBefore = car.point
        )
    }

}

class Vert: PointOnMap('|') {
    override fun nextMove(movement: Movement): Movement {
        return when(movement) {
            Movement.UP -> Movement.UP
            Movement.DOWN -> Movement.DOWN
            else -> throw IllegalStateException()
        }
    }

}

class Hori: PointOnMap('-') {
    override fun nextMove(movement: Movement): Movement {
        return when(movement) {
            Movement.LEFT -> Movement.LEFT
            Movement.RIGHT -> Movement.RIGHT
            else -> throw IllegalStateException()
        }
    }

}

class Slash: PointOnMap('/') {
    override fun nextMove(movement: Movement): Movement {
        return when(movement) {
            Movement.UP -> Movement.RIGHT
            Movement.DOWN -> Movement.LEFT
            Movement.LEFT -> Movement.DOWN
            Movement.RIGHT -> Movement.UP
        }
    }

}

class AntiSlash: PointOnMap('\\') {
    override fun nextMove(movement: Movement): Movement {
        return when (movement) {
            Movement.UP -> Movement.LEFT
            Movement.DOWN -> Movement.RIGHT
            Movement.LEFT -> Movement.UP
            Movement.RIGHT -> Movement.DOWN
        }
    }

}

class Cross: PointOnMap('+') {
    override fun nextMove(movement: Movement): Movement {
        return when(movement) {
            Movement.UP -> Movement.UP
            Movement.DOWN -> Movement.DOWN
            Movement.LEFT -> Movement.LEFT
            Movement.RIGHT -> Movement.RIGHT
        }
    }

}

open class Nothing: PointOnMap(' ') {
    override fun nextMove(movement: Movement): Movement {
        throw IllegalStateException()
    }
}

class Undeterminated: Nothing()


fun Array<Array<PointOnMap>>.print(size: Pair<Int, Int>, cars: List<Car>) {
    println((0 until size.second).map {y ->
        (0 until size.first).map { x ->
            cars.find {
                it.point.first == x && it.point.second == y
            }?.movement?.car ?: this[x][y].car
        }.joinToString("")
    }.joinToString("\n"))
}



fun main(args: Array<String>) {

    val datas = String::class.java.getResourceAsStream("/exercice13.txt").use {
        it.bufferedReader().readLines()
    }.map {
        it.toCharArray()
    }.toTypedArray()

    val size = datas.first().size to datas.size

    val map = (0 until size.first).map { x ->
        (0 until size.second).map { y ->
            when (datas[y][x]) {
                '/' -> Slash()
                '\\' -> AntiSlash()
                ' ' -> Nothing()
                '|' -> Vert()
                '-' -> Hori()
                '+' -> Cross()
                else -> Undeterminated()
            }
        }.toTypedArray()
    }.toTypedArray()

    var cars = datas.mapIndexed { y, line ->
        line.mapIndexed { x, car ->
            when (car) {
                '>' -> Car(x to y, Movement.RIGHT)
                '<' -> Car(x to y, Movement.LEFT)
                'v' -> Car(x to y, Movement.DOWN)
                '^' -> Car(x to y, Movement.UP)
                else -> null
            }
        }
    }.flatten().filter {
        it != null
    }.map {
        it!!
    }.toMutableList()

    for (car in cars) {
        val topPoint = if (car.point.second > 0) {
            map[car.point.first][car.point.second - 1]
        } else {
            Nothing()
        }
        val bottomPoint = if (car.point.second < size.second - 1) {
            map[car.point.first][car.point.second + 1]
        } else {
            Nothing()
        }
        val leftPoint = if (car.point.first > 0) {
            map[car.point.first - 1][car.point.second]
        } else {
            Nothing()
        }
        val rightPoint = if (car.point.first < size.first - 1) {
            map[car.point.first + 1][car.point.second]
        } else {
            Nothing()
        }
        val isVert = (topPoint !is Hori && topPoint !is Nothing) && (bottomPoint !is Hori && bottomPoint !is Nothing)
        val isHori = (leftPoint !is Vert && leftPoint !is Nothing) && (rightPoint !is Vert && rightPoint !is Nothing)
        val pointOnMap = if (isVert && isHori) {
            Cross()
        } else if (isHori) {
            Hori()
        } else if (isVert) {
            Vert()
        } else {
            throw IllegalStateException()
        }
        map[car.point.first][car.point.second] = pointOnMap
    }

    //map.print(size, cars)

    while (cars.size > 1) {
        val oldCars = cars.sortedBy { it.point.second * 99999 + it.point.first }.toMutableList()
        cars.clear()

        while (oldCars.isNotEmpty()) {
            val carToMove = oldCars.removeAt(0)
            val nextCar = map[carToMove.point.first][carToMove.point.second].moveCar(carToMove)
            nextCar.crash = carToMove.crash

            val otherCarsAtSamePoint = oldCars.filter {
                it.point == nextCar.point
            } + cars.filter {
                it.point == nextCar.point
            }
            if (otherCarsAtSamePoint.isNotEmpty()) {
                nextCar.crash = true
                otherCarsAtSamePoint.forEach { it.crash = true }
            }
            cars.add(nextCar)
        }

        for (crash in cars.filter { it.crash }) {
            println("Crash $crash")
        }
        cars = cars.filter {
            !it.crash
        }.toMutableList()
    }
    println(cars)

}


