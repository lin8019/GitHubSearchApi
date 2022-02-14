package com.example.githubsearchapi.api

import com.example.githubsearchapi.model.User
import com.google.gson.annotations.SerializedName

data class SearchUserResponse(
    @SerializedName("total_count")
    val count: Int = -1,
    @SerializedName("items")
    val items: List<User> = emptyList()
)