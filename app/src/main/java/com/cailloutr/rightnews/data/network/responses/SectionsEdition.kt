package com.cailloutr.rightnews.data.network.responses

import com.google.gson.annotations.SerializedName

data class SectionsEdition(
    val id: String,
    val webTitle: String,

    @SerializedName("webUrl")
    val webURL: String,

    @SerializedName("apiUrl")
    val apiURL: String,

    val code: String
)

