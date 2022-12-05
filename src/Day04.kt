// Day 04 - Camp Cleanup
// https://adventofcode.com/2022/day/4

fun main() {

    fun range(str: String): Pair<Int, Int> {
        val (from, to) = str.split('-')
        return from.toInt() to to.toInt()
    }

    fun Pair<Int, Int>.eatsup(that: Pair<Int, Int>) =
        (this.first <= that.first) && (this.second >= that.second)

    fun Pair<Int, Int>.overlap(that: Pair<Int, Int>) =
        (this.first >= that.first && this.first <= that.second) ||
                (this.second >= that.first && this.second <= that.second)

    fun part1(input: List<String>): Int {
        return input
            .map {
                val (first, second) = it.split(',')
                range((first)) to range((second))
            }.onEach {
                println(it)
            }
            .count {
                it.first.eatsup(it.second) || it.second.eatsup(it.first)
            }
    }

    fun part2(input: List<String>): Int {
        return input
            .map {
                val (first, second) = it.split(',')
                range((first)) to range((second))
            }.onEach {
                println(it)
            }
            .count {
                it.first.overlap(it.second) || it.second.overlap(it.first)
            }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput).also { println(it) } == 2)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}

/*

TODO

*/
