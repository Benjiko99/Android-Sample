package com.spiraclesoftware.androidsample.local

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val localModule = module {

    single {
        Room.databaseBuilder(androidContext(), MainDatabase::class.java, "main.db")
            .fallbackToDestructiveMigration()
            .addMigrations(*MainDatabase.Migrations.ALL_MIGRATIONS)
            .build()
    }

    single { get<MainDatabase>().transactionsDao() }

}