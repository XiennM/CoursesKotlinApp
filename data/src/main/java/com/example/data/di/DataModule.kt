package com.example.data.di

import androidx.room.Room
import com.example.data.CoursesRepositoryImpl
import com.example.data.local.AppDatabase
import com.example.effectivemobile.domain.repository.CoursesRepository
import org.koin.dsl.module
import android.content.Context

val dataRoomModule = module {
    single {
        val ctx: Context = get()
        Room.databaseBuilder(ctx, AppDatabase::class.java, "courses.db")
            .fallbackToDestructiveMigration()
            .build()
    }
    single { get<AppDatabase>().coursesDao() }
}

val dataRepositoryModule = module {
    single<CoursesRepository> { CoursesRepositoryImpl(api = get(), dao = get(), json = get()) }
}



