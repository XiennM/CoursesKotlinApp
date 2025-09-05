package com.example.data


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CoursesResponseDto(
    @SerialName("courses") val courses: List<CourseDto>
)

@Serializable
data class CourseDto(
    val id: Long,
    val title: String,
    @SerialName("text") val description: String,
    val price: String,
    val rate: String,
    val startDate: String,
    val hasLike: Boolean,
    val publishDate: String,
    val imageUrl: String? = null
)