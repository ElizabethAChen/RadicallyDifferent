package com.kingbart.radicallydifferent

import com.google.gson.annotations.SerializedName

data class Chinese (
    @SerializedName("character entry") val character_entry : List<CharacterEntry>
)

data class CharacterEntry (
    @SerializedName("Character") val character : String,
    @SerializedName("Radical 1") val radical_1 : String,
    @SerializedName("Radical 2") val radical_2 : String,
    @SerializedName("Radical 3") val radical_3 : String,
    @SerializedName("Radical 4") val radical_4 : String,
    @SerializedName("Radical 5") val radical_5 : String,
    @SerializedName("Radical 6") val radical_6 : String,
    @SerializedName("Radical 7") val radical_7 : String,
    @SerializedName("Radical 8") val radical_8 : String,
    @SerializedName("Radical 9") val radical_9 : String,
    @SerializedName("Radical 10") val radical_10 : String,
    @SerializedName("Radical 11") val radical_11 : String,
    @SerializedName("Radical 12") val radical_12 : String
)