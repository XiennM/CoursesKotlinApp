package com.example.effectivemobile.domain.usecase

import com.example.effectivemobile.domain.repository.CoursesRepository

class ToggleBookmarkUseCase(private val repo: CoursesRepository) {
    suspend operator fun invoke(id: Long, enabled: Boolean) = repo.updateBookmark(id, enabled)
}