package com.example.beardwulf.reva.interfaces

import android.graphics.Bitmap
import android.net.Uri
import com.example.beardwulf.reva.domain.Exhibitor

/**
 * TODO
 */
interface QuestionCallbacks {
    fun setNextExhibitor()
    fun determineNextMove()
    val firstQuestion : Boolean
}