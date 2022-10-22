package com.listery.text

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.Before
import org.junit.Test

internal class NumberFormatterTest {
    private lateinit var subject: NumberFormatter

    @Before
    fun setUp() {
        subject = NumberFormatter()
    }

    @Test
    fun concise_with_no_fraction() {
        val given = 42.0
        val expected = "42"

        val actual = subject.concise(given)

        assertEquals(expected, actual)
    }

    @Test
    fun concise_with_fraction() {
        val given = 42.37
        val expected = "42.37"

        val actual = subject.concise(given)

        assertEquals(expected, actual)
    }

    @Test
    fun concise_with_single_comma_no_fraction() {
        val given = 4242.0
        val expected = "4,242"

        val actual = subject.concise(given)

        assertEquals(expected, actual)
    }

    @Test
    fun concise_with_multiple_commas_no_fraction() {
        val given = 42424242.0
        val expected = "42,424,242"

        val actual = subject.concise(given)

        assertEquals(expected, actual)
    }

    @Test
    fun concise_with_single_comma_and_fraction() {
        val given = 4242.73
        val expected = "4,242.73"

        val actual = subject.concise(given)

        assertEquals(expected, actual)
    }

    @Test
    fun concise_with_multiple_commas_and_fraction() {
        val given = 42424242.56
        val expected = "42,424,242.56"

        val actual = subject.concise(given)

        assertEquals(expected, actual)
    }


}