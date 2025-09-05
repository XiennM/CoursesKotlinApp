package com.example.effectivemobile.domain.usecase

import com.example.effectivemobile.domain.models.Course
import com.example.effectivemobile.domain.models.SortType
import com.example.effectivemobile.domain.repository.CoursesRepository
import kotlinx.coroutines.flow.Flow

class ObserveCoursesUseCase(private val repo: CoursesRepository) {
    operator fun invoke() = repo.observeCourses()
}