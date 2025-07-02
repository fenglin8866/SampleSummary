package com.sample.summary.paging.ui.article

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sample.summary.paging.data.model.Article
import com.sample.summary.paging.data.repository.ArticleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    repository: ArticleRepository
) : ViewModel() {

    /*val articleFlow: Flow<PagingData<Article>> =
        repository.getArticlePagingFlow().cachedIn(viewModelScope)*/
}
