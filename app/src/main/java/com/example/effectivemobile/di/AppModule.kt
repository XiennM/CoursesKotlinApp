package com.example.effectivemobile.di

import android.content.Context
import com.example.effectivemobile.CoursesViewModel
import com.example.effectivemobile.FavouritesViewModel
import com.example.effectivemobile.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.koin.android.ext.koin.androidContext

val appModule = module {
    viewModel { LoginViewModel(get()) }
    viewModel { CoursesViewModel(get(), get(), get()) }
    viewModel { FavouritesViewModel(get(), get()) }
}