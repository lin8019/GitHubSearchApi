package com.example.githubsearchapi.ui

import androidx.paging.PagingSource
import androidx.paging.PagingState
import java.io.IOException


class CommonPagingSource<T : Any>(private val requestData: suspend (page: Int, perPageSize: Int) -> List<T>) :
    PagingSource<Int, T>() {
    companion object {
        const val PAGE_SIZE = 100
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        return try {
            val page = params.key ?: 1
            val pageSize = params.loadSize
            val repoResponse = requestData.invoke(page, pageSize)
            val prevKey = if (page > 1) page - 1 else null
            val nextKey = if (repoResponse.isNotEmpty()) page + 1 else null
            if (repoResponse.isNotEmpty())
                LoadResult.Page(repoResponse, prevKey, nextKey)
            else
                LoadResult.Error(Exception("找不到搜尋結果"))
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        } catch (e: IOException) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, T>): Int? = null
}