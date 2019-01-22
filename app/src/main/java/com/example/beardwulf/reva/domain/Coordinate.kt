package com.example.beardwulf.reva.domain

import com.google.gson.annotations.SerializedName

class Coordinate {

   @SerializedName("xCo")
    var xCo : Int
    @SerializedName("yCo")
    var yCo : Int

    constructor(xCo : Int, yCo : Int) {
        this.xCo = xCo
        this.yCo = yCo
    }
}