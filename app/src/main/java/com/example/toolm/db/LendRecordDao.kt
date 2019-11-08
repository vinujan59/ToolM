package com.example.toolm.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LendRecordDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(lendRecord: LendRecord)

    @Query("UPDATE lend_record_table SET count = count + 1 WHERE tool_id = :tool_id and friend_id = :friend_id")
    suspend fun lend(tool_id : Int, friend_id: Int)

    @Query("UPDATE lend_record_table SET count = count - :count WHERE tool_id = :tool_id and friend_id = :friend_id")
    suspend fun settle(tool_id : Int, friend_id: Int, count:Int)

    @Query("SELECT * from lend_record_table")
    fun getLendRecords(): LiveData<List<LendRecord>>
}