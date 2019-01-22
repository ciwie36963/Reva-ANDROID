package com.example.beardwulf.reva.domain

import android.arch.lifecycle.ViewModel
import android.graphics.Bitmap
import android.net.Uri

//Photoviewmodel class om zeker te zijn dat de getrokken foto wordt behouden bij rotatie
class PhotoViewModel : ViewModel() {

    public var photo: Bitmap = Bitmap.createBitmap(306, 306, Bitmap.Config.ARGB_8888)
    public lateinit var photoUri: Uri
    public var isValue = false

}