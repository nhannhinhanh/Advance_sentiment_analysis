package com.example.text_classification.data.utils

import android.content.Context
import com.google.mediapipe.tasks.text.textclassifier.TextClassifier
import com.google.mediapipe.tasks.text.textclassifier.TextClassifierResult
import com.google.mediapipe.tasks.core.BaseOptions

class SentimentAnalyzer(context: Context) {

    private val textClassifier: TextClassifier

    init {
        val modelPath = "bert_classifier.tflite"
        val baseOptions = BaseOptions.builder()
            .setModelAssetPath(modelPath)
            .build()

        val classifierOptions = TextClassifier.TextClassifierOptions.builder()
            .setBaseOptions(baseOptions)
            .build()

        textClassifier = TextClassifier.createFromOptions(context, classifierOptions)
    }

    fun analyze(text: String): String {
        return try {
            val result = textClassifier.classify(text)
            val topCategory = result.classificationResult()
                .classifications()
                .firstOrNull()
                ?.categories()
                ?.firstOrNull()

            topCategory?.let {
                "${it.categoryName()} ${it.displayName()}"
            } ?: "Unknown"
        } catch (e: Exception) {
            "Unknown"
        }
    }
}
