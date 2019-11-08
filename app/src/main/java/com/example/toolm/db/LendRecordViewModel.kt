package com.example.toolm.db

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LendRecordViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: LendRecordRepository
    val allLendRecords: LiveData<List<LendRecord>>

    init {
        val wordsDao = ToolMDB.getDatabase(application, viewModelScope).lendRecordDao()
        repository = LendRecordRepository(wordsDao)
        allLendRecords = repository.allLendRecords
    }

    fun lend(lendrecord: LendRecord) = viewModelScope.launch {
        repository.lend(lendrecord)
    }

    fun settle(lendrecord: LendRecord) = viewModelScope.launch {
        repository.settle(lendrecord)
    }
}
