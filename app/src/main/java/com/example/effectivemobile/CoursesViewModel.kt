package com.example.effectivemobile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.effectivemobile.domain.models.Course
import com.example.effectivemobile.domain.models.SortType
import com.example.effectivemobile.domain.usecase.ObserveCoursesUseCase
import com.example.effectivemobile.domain.usecase.RefreshCoursesUseCase
import com.example.effectivemobile.domain.usecase.ToggleBookmarkUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.*

data class CoursesUiState(
    val loading: Boolean = true,
    val data: List<Course> = emptyList(),
    val sort: SortOrder = SortOrder.NONE
)

enum class SortOrder { NONE, DATE_DESC }

class CoursesViewModel(
    private val observeCourses: ObserveCoursesUseCase,
    private val refreshCourses: RefreshCoursesUseCase,
    private val toggleBookmark: ToggleBookmarkUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CoursesUiState())
    val state: StateFlow<CoursesUiState> = _state

    init {
        observeCourses()
            .onEach { courses ->
                _state.update { st ->
                    st.copy(
                        loading = false,
                        data = applySort(courses, st.sort)
                    )
                }
            }
            .catch { e -> _state.value = CoursesUiState(loading = false) }
            .launchIn(viewModelScope)
    }

    fun toggleSort() {
        val newSort = if (_state.value.sort == SortOrder.NONE) SortOrder.DATE_DESC else SortOrder.NONE
        _state.update { st ->
            st.copy(
                sort = newSort,
                data = applySort(st.data, newSort)
            )
        }
    }

    private fun applySort(courses: List<Course>, sort: SortOrder): List<Course> {
        return when (sort) {
            SortOrder.NONE -> courses
            SortOrder.DATE_DESC -> courses.sortedByDescending { it.publishDate }
        }
    }

    fun onBookmarkClick(course: Course) {
        viewModelScope.launch {
            toggleBookmark(course.id, !course.hasLike)
        }
    }
}
