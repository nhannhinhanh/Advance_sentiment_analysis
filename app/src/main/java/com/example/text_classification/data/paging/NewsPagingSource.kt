package com.example.text_classification.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.text_classification.data.api.NewsApiService
import com.example.text_classification.data.model.Article
import retrofit2.HttpException
import java.io.IOException

class NewsPagingSource(
    private val api: NewsApiService,
    private val apiKey: String
) : PagingSource<Int, Article>() {

    companion object {
        private const val STARTING_PAGE_INDEX = 1
        private const val QUERY = "apple"
        private const val FROM_DATE = "2025-03-25"
        private const val SORT_BY = "publishedAt"
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val currentPage = params.key ?: STARTING_PAGE_INDEX

        return try {
            val response = api.getEverything(
                query = QUERY,
                from = FROM_DATE,
                sortBy = SORT_BY,
                page = currentPage,
                apiKey = apiKey
            )

            val articles = response.articles.orEmpty()
            val nextPage = if (articles.isEmpty()) null else currentPage + 1
            val prevPage = if (currentPage == STARTING_PAGE_INDEX) null else currentPage - 1

            LoadResult.Page(
                data = articles,
                prevKey = prevPage,
                nextKey = nextPage
            )

        } catch (e: IOException) {
            // Network error, such as no internet connection
            LoadResult.Error(e)
        } catch (e: HttpException) {
            // Server error (non-2xx responses)
            LoadResult.Error(e)
        } catch (e: Exception) {
            // Fallback for other unexpected exceptions
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        // Try to find the closest page to anchor position and calculate key
        return state.anchorPosition?.let { position ->
            val page = state.closestPageToPosition(position)
            page?.prevKey?.plus(1) ?: page?.nextKey?.minus(1)
        }
    }
}
