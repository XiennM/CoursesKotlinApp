package com.example.effectivemobile.domain.repository

import com.example.effectivemobile.domain.models.Course

interface CoursesRepository {
    suspend fun getCourses(): List<Course>
}