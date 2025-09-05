package com.example.effectivemobile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.effectivemobile.domain.models.Course
import com.example.effectivemobile.domain.models.SortType
import com.example.effectivemobile.domain.usecase.ObserveCoursesUseCase
import com.example.effectivemobile.domain.usecase.ToggleBookmarkUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class FavouritesUiState(
    val loading: Boolean = true,
    val data: List<Course> = emptyList(),
    val error: String? = null
)

class FavouritesViewModel(
    private val observeCourses: ObserveCoursesUseCase,
    private val toggleBookmark: ToggleBookmarkUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(FavouritesUiState())
    val state: StateFlow<FavouritesUiState> = _state

    init {
        observeCourses()
            .map { list -> list.filter { it.hasLike } }
            .onEach { favs -> _state.value = FavouritesUiState(loading = false, data = favs) }
            .catch { e -> _state.value = FavouritesUiState(loading = false, error = e.message) }
            .launchIn(viewModelScope)
    }

    fun onBookmarkClick(course: Course) {
        viewModelScope.launch {
            toggleBookmark(course.id, enabled = !course.hasLike)
        }
    }
}
