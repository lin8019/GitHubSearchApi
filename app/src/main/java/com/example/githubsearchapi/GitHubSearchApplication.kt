package com.example.githubsearchapi

import android.app.Application
import com.example.githubsearchapi.koin.okhttpClientModule
import com.example.githubsearchapi.koin.serviceModule
import com.example.githubsearchapi.koin.userRepoModule
import com.example.githubsearchapi.koin.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class GitHubSearchApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@GitHubSearchApplication)
            modules(listOf(okhttpClientModule, serviceModule, userRepoModule, viewModelModule))
        }
    }
}