package com.example.effectivemobile.di

import com.example.effectivemobile.CoursesViewModel
import com.example.effectivemobile.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { LoginViewModel(get()) }
    viewModel { CoursesViewModel(get()) }
}