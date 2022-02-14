package com.example.githubsearchapi.koin

import com.example.githubsearchapi.ui.SearchUserViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val  viewModelModule = module {
    viewModel { SearchUserViewModel(get()) }
}