package com.boscatov.stackoverflow

import com.google.gson.annotations.SerializedName

data class Question (
    @SerializedName("title")
    val title: String,
    @SerializedName("is_answered")
    val answered: Boolean,
    @SerializedName("answer_count")
    val answerCount: Int,
    @SerializedName("score")
    val score: Int
)