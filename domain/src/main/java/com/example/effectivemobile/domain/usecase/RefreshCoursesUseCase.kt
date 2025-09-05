package com.example.effectivemobile.domain.usecase

import com.example.effectivemobile.domain.repository.CoursesRepository

class RefreshCoursesUseCase(private val repo: CoursesRepository) {
    suspend operator fun invoke() = repo.refresh()
}