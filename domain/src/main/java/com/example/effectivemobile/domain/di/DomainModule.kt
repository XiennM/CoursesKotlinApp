package com.example.effectivemobile.domain.di

import com.example.effectivemobile.domain.repository.CoursesRepository
import com.example.effectivemobile.domain.usecase.LoginByEmailUseCase
import com.example.effectivemobile.domain.usecase.ObserveCoursesUseCase
import com.example.effectivemobile.domain.usecase.RefreshCoursesUseCase
import com.example.effectivemobile.domain.usecase.ToggleBookmarkUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { LoginByEmailUseCase() }
    factory { ObserveCoursesUseCase(get()) }
    factory { RefreshCoursesUseCase(get()) }
    factory { ToggleBookmarkUseCase(get()) }
}