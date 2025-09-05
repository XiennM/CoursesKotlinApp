package com.example.effectivemobile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.effectivemobile.domain.models.Course
import com.example.effectivemobile.domain.usecase.GetCoursesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class CoursesUiState(
    val loading: Boolean = true,
    val data: List<Course> = emptyList(),
    val error: String? = null
)

class CoursesViewModel(
    private val getCourses: GetCoursesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CoursesUiState())
    val state: StateFlow<CoursesUiState> = _state

    init { refresh() }

    fun refresh() {
        _state.value = CoursesUiState(loading = true)
        viewModelScope.launch {
            try {
                val list = getCourses()
                _state.value = CoursesUiState(loading = false, data = list)
            } catch (t: Throwable) {
                android.util.Log.e("CoursesVM", "load error", t)
                _state.value = CoursesUiState(loading = false, error = t.message)
            }
        }
    }
}