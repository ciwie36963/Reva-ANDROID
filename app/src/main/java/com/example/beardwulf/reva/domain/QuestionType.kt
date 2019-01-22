package com.example.beardwulf.reva.domain

import com.google.gson.annotations.SerializedName

enum class QuestionType {

    @SerializedName("PHOTO")
    PHOTO,
    @SerializedName("TEXT")
    TEXT
}