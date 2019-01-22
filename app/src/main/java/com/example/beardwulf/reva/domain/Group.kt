package com.example.beardwulf.reva.domain

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

class Group : Serializable{

    @SerializedName("_id")
    var _id: String
    @SerializedName("teacherId")
    var teacherId: String?
    @SerializedName("name")
    var name: String?
    @SerializedName("code")
    var code: String?
    @SerializedName("imageString")
    var imageString: String?
    @SerializedName("description")
    var description: String?
    @SerializedName("answer")
    var answers: Objects?
    @SerializedName("categories")
    var categories : List<Category>
    constructor(_id: String, teacherId: String?, name: String?, code: String?, imageString: String?, description: String?, answers: Objects?, categories : List<Category>) {
        this._id = _id
        this.teacherId = teacherId
        this.name = name
        this.code = code
        this.imageString = imageString
        this.description = description
        this.answers = answers
        this.categories = categories
    }
}