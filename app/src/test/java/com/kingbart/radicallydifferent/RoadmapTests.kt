package com.kingbart.radicallydifferent

import org.junit.Assert
import org.junit.Test

class RoadmapTests {
    @Test
    fun mapAnsCorrect(mapGameOutcome : String){
        Assert.assertTrue(mapGameOutcome == "You Win!")
    }

    @Test
    fun mapAnsShort(mapGameOutcome : String){
        Assert.assertTrue(mapGameOutcome == "Try Again!")
    }

    @Test
    fun mapAnsWrong(mapGameOutcome : String){
        Assert.assertTrue(mapGameOutcome == "Try Again!")
    }
}