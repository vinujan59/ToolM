package com.example.toolm.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.toolm.app.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [LendRecord::class], version = 1)
abstract class ToolMDB : RoomDatabase() {

    abstract fun lendRecordDao(): LendRecordDao

    companion object {
        @Volatile
        private var INSTANCE: ToolMDB? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): ToolMDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ToolMDB::class.java,
                    "toolm_database"
                )
                    .addCallback(ToolMDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class ToolMDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let {
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(it.lendRecordDao())
                    }
                }
            }
        }

        suspend fun populateDatabase(lendRecordDao: LendRecordDao) {
            for (toolIndex  in Constants.toolsInHand.indices) {
                for (frinedIndex in Constants.friends.indices) {
                    val lendRecord = LendRecord(toolIndex,frinedIndex,0)
                    lendRecordDao.insert(lendRecord)
                }
            }
        }
    }

}
