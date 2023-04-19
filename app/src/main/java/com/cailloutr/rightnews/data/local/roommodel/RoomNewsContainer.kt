package com.cailloutr.rightnews.data.local.roommodel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cailloutr.rightnews.constants.Constants.ROOM_NEWS_CONTAINER_DEFAULT_SECTION

@Entity(tableName = "news_container")
data class RoomNewsContainer(
    @PrimaryKey
    val id: String = ROOM_NEWS_CONTAINER_DEFAULT_SECTION,
    val total: Long,
    @ColumnInfo(name = "start_index")
    val startIndex: Long,
    @ColumnInfo(name = "page_size")
    val pageSize: Long,
    @ColumnInfo(name = "current_page")
    val currentPage: Long,
    val pages: Long,
    @ColumnInfo(name = "order_by")
    val orderBy: String,
)