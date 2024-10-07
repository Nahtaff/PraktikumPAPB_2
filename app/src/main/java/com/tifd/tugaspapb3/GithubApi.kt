package com.tifd.tugaspapb3

import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubApi {
    @GET("users/{username}")
    suspend fun getUserProfile(@Path("username") username: String): UserGithub
}