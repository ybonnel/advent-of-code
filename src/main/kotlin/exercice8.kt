fun main(args: Array<String>) {

    val datas = String::class.java.getResourceAsStream("/exercice8.txt").use {
        it.bufferedReader().readLines()
    }.first().split(" ").map {
        it.toInt()
    }


    // First part
    val firstData = datas.toMutableList()
    println(Node(firstData.removeAt(0), firstData.removeAt(0)).parse(firstData))

    // Part two
    val secondData = datas.toMutableList()
    println(ComplexNode(secondData.removeAt(0), secondData.removeAt(0)).parse(secondData))
}

data class Node (
        val nbChild: Int,
        val nbMetadata: Int
) {
    fun parse(datas: MutableList<Int>): Int {
        return (0 until nbChild).map {
            Node(datas.removeAt(0), datas.removeAt(0)).parse(datas)
        }.sum() + (0 until nbMetadata).map { datas.removeAt(0) }.sum()
    }
}



data class ComplexNode (
        val nbChild: Int,
        val nbMetadata: Int
) {
    fun parse(datas: MutableList<Int>): Int {
        return if (nbChild == 0) {
            (0 until nbMetadata).map { datas.removeAt(0) }.sum()
        } else {
            val childs = (0 until nbChild).map {
                ComplexNode(datas.removeAt(0), datas.removeAt(0)).parse(datas)
            }
            (0 until nbMetadata).map { datas.removeAt(0) }.map {childIndex ->
                childs.elementAtOrNull(childIndex - 1) ?: 0
            }.sum()
        }
    }
}