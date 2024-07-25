package com.example.myapplication

import kotlin.math.abs

//MVC - Model View Controller

//Controller - dialog with user
//View - print board
//Model - 1) state(data)
//        2) logic (service)

const val EMPTY: Byte = 16
const val DIM = 4
val INITIAL_STATE = ByteArray(16) { (it + 1).toByte() }

// MODEL: STATE
var state = INITIAL_STATE.clone()

// MODEL: ENGINE
fun transitionState(oldState: ByteArray, cell: Byte): ByteArray {
    val ixCell = oldState.indexOf(cell)
    val ixEmpty = oldState.indexOf(EMPTY)

    return if (areAdjacent(ixCell, ixEmpty))
        withSwapped(oldState, ixCell, ixEmpty)
    else oldState
}

fun withSwapped(arr: ByteArray, ix1: Int, ix2: Int): ByteArray {
    if (ix1 == ix2) return arr
    val res = arr.clone()
    res[ix1] = arr[ix2]
    res[ix2] = arr[ix1]
    return res
}

fun areAdjacent(ix1: Int, ix2: Int): Boolean {
    val row1 = row(ix1)
    val col1 = col(ix1)
    val row2 = row(ix2)
    val col2 = col(ix2)
    return (row1 == row2 && abs(col1 - col2) == 1 ||
            col1 == col2 && abs(row1 - row2) == 1)
}

fun row(ix: Int) = ix / DIM
fun col(ix: Int) = ix % DIM

fun ix(row: Int, col: Int) = row * DIM + col

fun isWin(state: ByteArray): Boolean =
    state.contentEquals(INITIAL_STATE)

fun countInversions(state: ByteArray): Int {
    val rowOfEmptyCell = row(state.indexOf(EMPTY))
    var inversions = rowOfEmptyCell
    repeat(state.size) {
        if (state[it] != EMPTY)
            for (j in it + 1..<state.size) {
                if (state[j] != EMPTY && state[it] > state[j]) inversions++
            }
    }
    return inversions
}

fun isFeasibleSolution(state: ByteArray): Boolean = countInversions(state) % 2 == 1

fun getInitialState(): ByteArray {
    // state = TEST_STATE
    val res = INITIAL_STATE.clone()
    res.shuffle()
    if (isFeasibleSolution(res)) {
        return res
    }
    return if (res[0] != EMPTY && res[1] != EMPTY) {
        withSwapped(res, 0, 1)
    } else {
        withSwapped(res, 2, 3)
    }
}

// CONTROLLER
fun main() {
    println("Welcome to game Fifteen!")
    state = getInitialState()
    while (!isWin(state)) {
        printBoard(state)
        val cell = readCell()
        state = transitionState(state, cell)
    }
    printBoard(state)
    println("You win!")
}

fun readCell(
    println: (String) -> Unit = ::println,
    readln: () -> String = ::readln
    ): Byte {
    while (true) {
        println("Enter cell to move 1..15: ")
        val res = readln().toIntOrNull()
        if (res in 1..15) return res!!.toByte()
    }
}

// VIEW
fun printBoard(
    state: ByteArray,
    printer: (String) -> Unit = ::print
) {
    printer("-".repeat(18))
    printer("\n")
    for (iRow in 0..<DIM) {
        printer("|")
        for (iCol in 0..<DIM) {
            printer(formatCell(state[ix(iRow, iCol)]))
        }
        printer("|\n")
    }
    printer("------------------")
}

fun formatCell(cell: Byte) = "%3s ".format(if (cell == EMPTY) " " else cell)
