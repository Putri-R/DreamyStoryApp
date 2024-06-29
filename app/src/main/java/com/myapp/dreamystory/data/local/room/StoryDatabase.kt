package com.myapp.dreamystory.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.myapp.dreamystory.data.remote.response.ListStoryItem

@Database(
    entities = [ListStoryItem::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class StoryDatabase : RoomDatabase() {

    abstract fun storiesDao(): StoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        @Volatile
        private var instance: StoryDatabase? = null

        fun getInstance(context: Context): StoryDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    StoryDatabase::class.java,
                    "story_database"
                ).build().also { instance = it }
            }
    }
}