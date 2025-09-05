package com.example.effectivemobile.domain.repository

import com.example.effectivemobile.domain.models.Course
import com.example.effectivemobile.domain.models.SortType
import kotlinx.coroutines.flow.Flow

interface CoursesRepository {
    fun observeCourses(): Flow<List<Course>>
    suspend fun refresh()
    suspend fun updateBookmark(id: Long, hasLike: Boolean)
}