package com.kingbart.radicallydifferent.jsonadapters

import com.google.gson.annotations.SerializedName

data class Puzzle (
    @SerializedName("puzzle instructions") val puzzle_instructions : List<PuzzleInstruction>
)

data class PuzzleInstruction (
    @SerializedName("hanzi") val hanzi : String,
    @SerializedName("instruction") val instruction : String,
    @SerializedName("a") val a : String,
    @SerializedName("b") val b : String,
    @SerializedName("c") val c : String,
    @SerializedName("d") val d : String,
    @SerializedName("e") val e : String,
    @SerializedName("f") val f : String,
    @SerializedName("g") val g : String,
    @SerializedName("h") val h : String,
    @SerializedName("i") val i : String,
    @SerializedName("j") val j : String,
    @SerializedName("k") val k : String,
    @SerializedName("l") val l : String,
    @SerializedName("m") val m : String,
    @SerializedName("n") val n : String,
    @SerializedName("o") val o : String,
    @SerializedName("p") val p : String,
    @SerializedName("q") val q : String,
    @SerializedName("r") val r : String,
    @SerializedName("s") val s : String,
    @SerializedName("t") val t : String
)