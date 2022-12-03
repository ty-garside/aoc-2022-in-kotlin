// Rock Paper Scissors
// https://adventofcode.com/2022/day/2
// * Enhanced to support Lizard and Spock

object Rock : Shape({
    it("breaks", Scissors)
    it("crushes", Lizard)
})

object Paper : Shape({
    it("covers", Rock)
    it("disproves", Spock)
})

object Scissors : Shape({
    it("cuts", Paper)
    it("decapitates", Lizard)
})

object Lizard : Shape({
    it("eats", Paper)
    it("poisons", Spock)
})

object Spock : Shape({
    it("vaporizes", Rock)
    it("smashes", Scissors)
})

fun score(theirs: Shape, mine: Shape) =
    when (mine) { // shape score
        Rock -> 1
        Paper -> 2
        Scissors -> 3
        Lizard -> 4
        Spock -> 5
    } + when { // outcome score
        mine > theirs -> 6 // win
        mine < theirs -> 0 // lose
        else -> 3 // draw
    }

fun List<String>.sumOfScores(
    withStrategy: Shape.(Char) -> Shape
) = sumOf { line ->

    val theirs = when (line[0]) {
        'A' -> Rock
        'B' -> Paper
        'C' -> Scissors
        'D' -> Lizard
        'E' -> Spock
        else -> error("Invalid: $line")
    }

    val ours = theirs.withStrategy(line[2])

    score(theirs, ours)
        .also {
            if (DEBUG) when {
                ours > theirs -> println("WIN: $ours ${ours.beats[theirs]} $theirs ($it points)")
                ours < theirs -> println("LOSE: $theirs ${ours.loses[theirs]} $ours ($it points)")
                else -> println("DRAW: $ours ($it points)")
            }
        }
}

const val DEBUG = false

fun main() {

    fun part1(input: List<String>): Int {
        return input.sumOfScores {
            when (it) {
                'X' -> Rock
                'Y' -> Paper
                'Z' -> Scissors
                'W' -> Lizard
                'V' -> Spock
                else -> error("Invalid $it")
            }
        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOfScores {
            when (it) {
                'X' -> beats.keys.first() // always lose
                'Y' -> this // always draw
                'Z' -> loses.keys.first() // always win
                'W' -> beats.keys.last() // maximize loss score
                'V' -> loses.keys.last() // maximize win score
                else -> error("Invalid $it")
            }
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 15)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}

typealias Rules = (verb: String, loser: Shape) -> Unit

sealed class Shape(defineRules: (Rules) -> Unit) : Comparable<Shape> {

    val beats by lazy {
        mutableMapOf<Shape, String>().apply {
            defineRules { verb, loser ->
                put(loser, verb)
            }
        }.toMap()
    }

    val loses by lazy {
        Shapes.filter {
            it.beats.contains(this)
        }.associateWith {
            it.beats[this]!!
        }
    }

    override fun compareTo(other: Shape) = when {
        beats.contains(other) -> 1
        loses.contains(other) -> -1
        else -> 0
    }

    override fun toString() = this::class.simpleName!!
}

val Shapes by lazy {
    // used to avoid sealed class reflection
    // lazy avoids circular reference weirdness
    setOf(
        Rock,
        Paper,
        Scissors,
        Lizard,
        Spock
    )
}

/*

--- Day 2: Rock Paper Scissors ---

The Elves begin to set up camp on the beach. To decide whose tent gets to be closest to the snack storage, a giant Rock Paper Scissors tournament is already in progress.

Rock Paper Scissors is a game between two players. Each game contains many rounds; in each round, the players each simultaneously choose one of Rock, Paper, or Scissors using a hand shape. Then, a winner for that round is selected: Rock defeats Scissors, Scissors defeats Paper, and Paper defeats Rock. If both players choose the same shape, the round instead ends in a draw.

Appreciative of your help yesterday, one Elf gives you an encrypted strategy guide (your puzzle input) that they say will be sure to help you win. "The first column is what your opponent is going to play: A for Rock, B for Paper, and C for Scissors. The second column--" Suddenly, the Elf is called away to help with someone's tent.

The second column, you reason, must be what you should play in response: X for Rock, Y for Paper, and Z for Scissors. Winning every time would be suspicious, so the responses must have been carefully chosen.

The winner of the whole tournament is the player with the highest score. Your total score is the sum of your scores for each round. The score for a single round is the score for the shape you selected (1 for Rock, 2 for Paper, and 3 for Scissors) plus the score for the outcome of the round (0 if you lost, 3 if the round was a draw, and 6 if you won).

Since you can't be sure if the Elf is trying to help you or trick you, you should calculate the score you would get if you were to follow the strategy guide.

For example, suppose you were given the following strategy guide:

A Y
B X
C Z

This strategy guide predicts and recommends the following:

    In the first round, your opponent will choose Rock (A), and you should choose Paper (Y). This ends in a win for you with a score of 8 (2 because you chose Paper + 6 because you won).
    In the second round, your opponent will choose Paper (B), and you should choose Rock (X). This ends in a loss for you with a score of 1 (1 + 0).
    The third round is a draw with both players choosing Scissors, giving you a score of 3 + 3 = 6.

In this example, if you were to follow the strategy guide, you would get a total score of 15 (8 + 1 + 6).

What would your total score be if everything goes exactly according to your strategy guide?

Your puzzle answer was 14163.

--- Part Two ---

The Elf finishes helping with the tent and sneaks back over to you. "Anyway, the second column says how the round needs to end: X means you need to lose, Y means you need to end the round in a draw, and Z means you need to win. Good luck!"

The total score is still calculated in the same way, but now you need to figure out what shape to choose so the round ends as indicated. The example above now goes like this:

    In the first round, your opponent will choose Rock (A), and you need the round to end in a draw (Y), so you also choose Rock. This gives you a score of 1 + 3 = 4.
    In the second round, your opponent will choose Paper (B), and you choose Rock so you lose (X) with a score of 1 + 0 = 1.
    In the third round, you will defeat your opponent's Scissors with Rock for a score of 1 + 6 = 7.

Now that you're correctly decrypting the ultra top secret strategy guide, you would get a total score of 12.

Following the Elf's instructions for the second column, what would your total score be if everything goes exactly according to your strategy guide?

Your puzzle answer was 12091.

Both parts of this puzzle are complete! They provide two gold stars: **

*/
