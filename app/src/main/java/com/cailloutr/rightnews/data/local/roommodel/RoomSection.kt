package com.cailloutr.rightnews.data.local.roommodel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cailloutr.rightnews.model.Section

@Entity(tableName = "section")
data class RoomSection(
    @PrimaryKey
    val id: String,
    val title: String,
    @ColumnInfo(name = "web_url")
    val webUrl: String,
    @ColumnInfo(name = "api_url")
    val apiUrl: String,
    val code: String
)

fun RoomSection.toSection() = Section(
    id = id,
    title = title,
    webUrl = webUrl,
    apiUrl = apiUrl,
    code = code
)
