package com.example.ghazalapp.interFace

import android.graphics.Bitmap
import codes.side.andcolorpicker.model.IntegerHSLColor

interface BackgroundColorInterface {
    fun colorBackground(color: IntegerHSLColor, boolean: Boolean)
    fun colortextBackground(color: Int)
    fun textfont(font: Int)
    fun textsize(size: Float)
    fun galleryimage(image: Bitmap )

}