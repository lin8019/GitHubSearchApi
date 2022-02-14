package com.example.githubsearchapi.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.githubsearchapi.data.UserRepo
import com.example.githubsearchapi.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class SearchUserViewModel(private val userRepo: UserRepo<Flow<PagingData<User>>>)  : ViewModel() {
    private val queryAll = "\"\""
    val flowOfName: Flow<String> = MutableStateFlow(queryAll)

    suspend fun searchUser(keyWord: String) {
        (flowOfName as MutableStateFlow).emit(if (keyWord.isEmpty()) queryAll else keyWord)
    }

    fun getUserPagingData(queryName: String): Flow<PagingData<User>> {
        return userRepo.getPagingDataFlow(queryName, CommonPagingSource.PAGE_SIZE).cachedIn(viewModelScope)
    }
}