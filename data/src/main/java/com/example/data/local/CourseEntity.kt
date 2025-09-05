package com.example.data.local
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "courses")
data class CourseEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val description: String,
    val price: String,
    val rate: String,
    val startDate: String,
    val publishDate: String,
    val imageUrl: String?,
    val hasLike: Boolean = false
)
