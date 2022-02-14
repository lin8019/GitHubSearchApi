package com.example.githubsearchapi.ui

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.githubsearchapi.R
import com.example.githubsearchapi.databinding.ActivityUserListBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchUserActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityUserListBinding
    private val viewModel : SearchUserViewModel by viewModel()
    private val userPagingAdapter = UserPagingAdapter()
    private var searchJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityUserListBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        viewBinding.etNameInput.doOnTextChanged {  text, _, _, _ ->
            lifecycleScope.launchWhenCreated {
                viewModel.searchUser(text.toString().trim())
            }
        }

        viewBinding.rvUser.layoutManager = GridLayoutManager(
            this, 4, GridLayoutManager
                .VERTICAL, false
        )
        viewBinding.rvUser.adapter = userPagingAdapter

        userPagingAdapter.addLoadStateListener {
            when (it.refresh) {
                is LoadState.NotLoading -> {
                    viewBinding.pbLoading.visibility = View.INVISIBLE
                    viewBinding.rvUser.visibility = View.VISIBLE
                    viewBinding.tvError.visibility = View.INVISIBLE
                }
                is LoadState.Loading -> {
                    viewBinding.pbLoading.visibility = View.VISIBLE
                    viewBinding.rvUser.visibility = View.INVISIBLE
                    viewBinding.tvError.visibility = View.INVISIBLE
                }
                is LoadState.Error -> {
                    val state = it.refresh as LoadState.Error
                    viewBinding.pbLoading.visibility = View.INVISIBLE
                    viewBinding.tvError.visibility = View.VISIBLE
                    viewBinding.tvError.text = getString(R.string.api_error_message, state.error.message)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.flowOfName.debounce(500).collect {
                (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
                    .hideSoftInputFromWindow(window.decorView.windowToken, 0)

                searchJob?.cancel()
                searchJob = launch {
                    viewModel.getUserPagingData(it).collect {
                        userPagingAdapter.submitData(it)
                    }
                }
            }
        }
    }
}