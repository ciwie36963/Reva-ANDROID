package com.example.beardwulf.reva.domain

import com.google.gson.annotations.SerializedName

class Category {

    @SerializedName("name")
    var name: String

    constructor(name: String) {
        this.name= name
    }
}