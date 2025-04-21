package com.example.text_classification.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.text_classification.data.api.NewsApiService
import com.example.text_classification.data.paging.NewsPagingSource

class NewsViewModel(
    private val apiService: NewsApiService
) : ViewModel() {
    val pagedNews = Pager(PagingConfig(pageSize = 20)) {
        NewsPagingSource(apiService, "0db8aaefe18a476b86f1970327cfe251")
    }.flow.cachedIn(viewModelScope)
}
