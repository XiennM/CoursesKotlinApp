package com.example.effectivemobile.domain.di

import com.example.effectivemobile.domain.repository.CoursesRepository
import com.example.effectivemobile.domain.usecase.GetCoursesUseCase
import com.example.effectivemobile.domain.usecase.LoginByEmailUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { LoginByEmailUseCase() }
    factory { GetCoursesUseCase(get<CoursesRepository>()) }
}