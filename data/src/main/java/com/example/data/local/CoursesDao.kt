package com.example.data.local
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface CoursesDao {

    @Query("SELECT * FROM courses")
    fun observeAll(): Flow<List<CourseEntity>>

    @Upsert
    suspend fun upsertAll(items: List<CourseEntity>)

    @Query("UPDATE courses SET hasLike = :enabled WHERE id = :id")
    suspend fun updateBookmark(id: Long, enabled: Boolean)

    @Query("SELECT id, hasLike FROM courses")
    suspend fun snapshotLikes(): List<IdLike>
}

data class IdLike(val id: Long, val hasLike: Boolean)
