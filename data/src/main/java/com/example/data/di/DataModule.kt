package com.example.data.di

import com.example.data.CoursesRepositoryImpl
import com.example.effectivemobile.domain.repository.CoursesRepository
import org.koin.dsl.module

val dataRepositoryModule = module {
    single<CoursesRepository> {
        CoursesRepositoryImpl(api = get(), json = get())
    }
}
