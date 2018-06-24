package com.boscatov.stackoverflow

import com.google.gson.annotations.SerializedName

data class StackPage (
    @SerializedName("items")
    val questions: ArrayList<Question>
)