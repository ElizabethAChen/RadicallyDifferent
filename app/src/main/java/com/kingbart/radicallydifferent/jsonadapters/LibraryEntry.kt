package com.kingbart.radicallydifferent.jsonadapters

import com.google.gson.annotations.SerializedName

class Library (
    @SerializedName("RadicalEntry") val radicalEntry: List<RadicalEntry>
)

data class RadicalEntry (
    @SerializedName("Order") val order : Int,
    @SerializedName("Simplified") val simplified : String,
    @SerializedName("Traditional") val traditional : String,
    @SerializedName("Variant1") val variant1 : String,
    @SerializedName("Variant2") val variant2 : String,
    @SerializedName("Meaning") val meaning : String,
    @SerializedName("Strokes") val strokes : Int,
    @SerializedName("Examples1") val examples1 : String,
    @SerializedName("Examples2") val examples2 : String,
    @SerializedName("Examples3") val examples3 : String,
    @SerializedName("Mnemonic") val mnemonic : String,
    @SerializedName("Comments") val comments : String
)