package com.cailloutr.rightnews.data.network.responses

import com.cailloutr.rightnews.enums.Code
import com.cailloutr.rightnews.model.Section
import com.google.gson.annotations.SerializedName

data class SectionsResult(
    val id: String,
    val webTitle: String,

    @SerializedName("webUrl")
    val webURL: String,

    @SerializedName("apiUrl")
    val apiURL: String,

    val editions: List<SectionsEdition>,
)

fun SectionsResult.toDefaultSection(): Section {
    val edition = editions.first {
        it.code == Code.Default.value
    }

    return Section(
        id = edition.id,
        title = edition.webTitle,
        webUrl = edition.webURL,
        apiUrl = edition.apiURL,
        code = edition.code
    )
}