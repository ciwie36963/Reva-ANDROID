package com.example.beardwulf.reva.domain

import com.google.gson.annotations.SerializedName
import java.util.*

class Question {

    @SerializedName("body")
    var body : String
    @SerializedName("posted")
    var posted: Date
    @SerializedName("possibleAnswers")
    var possibleAnswers: Array<String>
    @SerializedName("counter")
    var counter : Int
    @SerializedName("type")
    var type : QuestionType

    constructor(body: String, posted: Date, possibleAnswers: Array<String>, counter : Int, type : QuestionType) {
        this.body=body
        this.posted=posted
        this.possibleAnswers = possibleAnswers
        this.counter = counter
        this.type = type
    }
}