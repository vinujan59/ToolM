package com.example.toolm.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lend_record_table", primaryKeys = ["tool_id", "friend_id"])
data class LendRecord (
    @ColumnInfo(name = "tool_id")  val tool_id : Int,
    @ColumnInfo(name = "friend_id")  val friend_id : Int,
    @ColumnInfo(name = "count")  val count : Int
    )
