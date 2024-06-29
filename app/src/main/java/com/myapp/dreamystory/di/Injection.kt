package com.myapp.dreamystory.di

import android.content.Context
import com.myapp.dreamystory.data.UserRepository
import com.myapp.dreamystory.data.local.room.StoryDatabase
import com.myapp.dreamystory.data.pref.UserPreference
import com.myapp.dreamystory.data.pref.dataStore
import com.myapp.dreamystory.data.remote.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getDataSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        val database = StoryDatabase.getInstance(context)
        return UserRepository.getInstance(database, apiService, pref)
    }
}