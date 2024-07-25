package com.example.myapplication

import org.junit.Assert.*
import org.junit.Test

class MainKtControllerTest {

    @Test
    fun readCellTest() {
        val expected: Byte = 12
        val provided = expected.toString()

        val res = readCell(println = {}, readln = { provided })

        assertEquals(expected, res)
    }
}