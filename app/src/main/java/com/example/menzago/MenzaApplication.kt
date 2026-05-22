package com.example.menzago

import android.app.Application
import androidx.room.Room
import com.example.menzago.data.local.MenzaDatabase
import com.example.menzago.data.repository.MenzaRepository
import com.example.menzago.data.repository.RepositoryProvider

class MenzaApplication : Application() {

    lateinit var database: MenzaDatabase
        private set

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(
            applicationContext,
            MenzaDatabase::class.java,
            "menza_database"
        ).build()

        RepositoryProvider.repository = MenzaRepository(
            dao = database.favoritesDao()
        )
    }
}