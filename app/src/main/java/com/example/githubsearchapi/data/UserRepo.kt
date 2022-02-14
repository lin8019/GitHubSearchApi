package com.example.githubsearchapi.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.githubsearchapi.api.GitHubSearchService
import com.example.githubsearchapi.model.User
import com.example.githubsearchapi.ui.CommonPagingSource
import kotlinx.coroutines.flow.Flow

interface UserRepo<T> {
    fun getPagingDataFlow(key: String, pageSize: Int): T
}

class GitHubUserRepo(private val service: GitHubSearchService) : UserRepo<Flow<PagingData<User>>>{
    override fun getPagingDataFlow(key: String, pageSize: Int): Flow<PagingData<User>> {
        return Pager(
            config = PagingConfig(pageSize = pageSize),
            pagingSourceFactory = {
                CommonPagingSource { page, perSize ->
                    service.searchRepos(key, page, perSize).items
                }
            }
        ).flow
    }
}