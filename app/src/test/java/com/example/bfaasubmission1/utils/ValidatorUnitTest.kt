package com.example.bfaasubmission1.utils

import org.junit.Assert
import org.junit.Test

class ValidatorUnitTest {
    private val emptyText = ""
    private val spaceText = "   "
    private val invalidText = "fa"
    private val validText = "fadil"

    @Test
    fun invalidSearchText() {
        Assert.assertFalse(Validator.isSearchTextValid(emptyText))
        Assert.assertFalse(Validator.isSearchTextValid(spaceText))
        Assert.assertFalse(Validator.isSearchTextValid(invalidText))
    }

    @Test
    fun validSearchText() {
        Assert.assertTrue(Validator.isSearchTextValid(validText))
    }
}
