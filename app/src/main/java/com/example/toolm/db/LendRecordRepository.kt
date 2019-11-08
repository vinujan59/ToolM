package com.example.toolm.db

import androidx.lifecycle.LiveData

class LendRecordRepository(private val lendRecordDao: LendRecordDao) {

    val allLendRecords: LiveData<List<LendRecord>> = lendRecordDao.getLendRecords()

    suspend fun lend(lendRecord: LendRecord) {
        lendRecordDao.lend(lendRecord.tool_id, lendRecord.friend_id)
    }

    suspend fun settle(lendRecord: LendRecord) {
        lendRecordDao.settle(lendRecord.tool_id, lendRecord.friend_id, lendRecord.count)
    }
}
