package com.example.effectivemobile.domain.usecase

import com.example.effectivemobile.domain.models.Course
import com.example.effectivemobile.domain.repository.CoursesRepository

class GetCoursesUseCase(private val repo: CoursesRepository) {
    suspend operator fun invoke(): List<Course> = repo.getCourses()
}