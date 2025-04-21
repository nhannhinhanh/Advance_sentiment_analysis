package com.example.text_classification

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.text_classification.data.api.NewsApiService
import com.example.text_classification.data.sentimentanalysis.SentimentAnalyzer
import com.example.text_classification.databinding.ActivityMainBinding
import com.example.text_classification.ui.NewsPagingAdapter
import com.example.text_classification.ui.NewsViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: NewsViewModel
    private lateinit var adapter: NewsPagingAdapter
    private lateinit var analyzer: SentimentAnalyzer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        analyzer = SentimentAnalyzer(this)
        adapter = NewsPagingAdapter(analyzer)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter


        val api = Retrofit.Builder()
            .baseUrl("https://newsapi.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApiService::class.java)

        viewModel = NewsViewModel(api)

        lifecycleScope.launch {
            viewModel.pagedNews.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }
    }

}