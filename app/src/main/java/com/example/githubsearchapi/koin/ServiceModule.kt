package com.example.githubsearchapi.koin

import com.example.githubsearchapi.api.GitHubSearchService
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val okhttpClientModule = module {
    single { createOkHttpClient() }
}

val serviceModule = module {
    factory<GitHubSearchService> { createService(get()) }
}

fun createOkHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .apply {
            addInterceptor { chain ->
                chain.request().newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("accept", "application/vnd.github.v3+json")
                    .url(chain.request().url())
                    .build().run {
                        chain.proceed(this).run {
                            if (this.headers().get("x-ratelimit-remaining") == "0") {
                                Response.Builder()
                                    .protocol(this.protocol())
                                    .headers(this.headers())
                                    .sentRequestAtMillis(this.sentRequestAtMillis())
                                    .request(this.request())
                                    .receivedResponseAtMillis(this.receivedResponseAtMillis())
                                    .code(this.code())
                                    .body(ResponseBody.create(this.body()?.contentType(), ""))
                                    .message("搜尋速率超過限制").build()
                            } else {
                                this
                            }
                        }
                    }
            }
        }
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()
}

inline fun <reified T> createService(okHttpClient: OkHttpClient): T {
    return Retrofit.Builder()
        .baseUrl(GitHubSearchService.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(T::class.java)
}