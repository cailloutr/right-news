package com.cailloutr.rightnews.data.local.dao

import androidx.room.*
import com.cailloutr.rightnews.data.local.roommodel.RoomArticle
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(vararg articles: RoomArticle)

    @Delete
    suspend fun deleteArticle(vararg articles: RoomArticle)

    @Query("SELECT * FROM articles WHERE container_id == :section")
    fun getAllArticlesFromSection(section: String): Flow<List<RoomArticle>>

    @Query("SELECT * FROM articles WHERE id == :id")
    fun getArticle(id: String): Flow<RoomArticle>

}