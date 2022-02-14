package com.example.githubsearchapi.model

import com.google.gson.annotations.SerializedName


data class User(
    @SerializedName("id")
    val id: Int = -1,
    @SerializedName("login")
    val name: String = "",
    @SerializedName("avatar_url")
    val avatarUrl: String = ""
)