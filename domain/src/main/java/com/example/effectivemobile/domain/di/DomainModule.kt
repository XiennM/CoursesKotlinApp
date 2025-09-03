package com.example.effectivemobile.domain.di

import com.example.effectivemobile.domain.usecase.LoginByEmailUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { LoginByEmailUseCase() }
}