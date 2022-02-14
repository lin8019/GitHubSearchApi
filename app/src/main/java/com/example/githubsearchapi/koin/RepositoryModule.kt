package com.example.githubsearchapi.koin

import androidx.paging.PagingData
import com.example.githubsearchapi.data.GitHubUserRepo
import com.example.githubsearchapi.data.UserRepo
import com.example.githubsearchapi.model.User
import kotlinx.coroutines.flow.Flow
import org.koin.dsl.module

val userRepoModule = module {
    factory<UserRepo<Flow<PagingData<User>>>> { GitHubUserRepo(get()) }
}