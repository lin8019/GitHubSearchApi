package com.example.githubsearchapi.api

import retrofit2.http.GET
import retrofit2.http.Query

interface GitHubSearchService {
    @GET("search/users")
    suspend fun searchRepos(
        @Query("q") name: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): SearchUserResponse

    companion object {
        const val BASE_URL = "https://api.github.com/"
    }
}