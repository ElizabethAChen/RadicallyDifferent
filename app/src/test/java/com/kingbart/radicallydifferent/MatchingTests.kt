package com.kingbart.radicallydifferent

import org.junit.Assert
import org.junit.Test

class MatchingTests {
    @Test
    fun matchAnsCorrect(matchingGameOutcome : String){
        Assert.assertTrue(matchingGameOutcome  == "You Win!")
    } //length is 6 and all correct

    @Test
    fun matchAnsShort(matchingGameOutcome : String){
        Assert.assertTrue(matchingGameOutcome == "Try Again!")
    } //length is 5

    @Test
    fun matchAnsLong(matchingGameOutcome : String){
        Assert.assertTrue(matchingGameOutcome == "Try Again!")
    } //length is 7

    @Test
    fun matchAnsWrong(matchingGameOutcome : String){
        Assert.assertTrue(matchingGameOutcome == "Try Again!")
    } // length is 6 but not all answers correct
}