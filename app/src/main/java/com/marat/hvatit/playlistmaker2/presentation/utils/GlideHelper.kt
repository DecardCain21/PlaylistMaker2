package com.marat.hvatit.playlistmaker2.presentation.utils

import android.content.Context
import android.widget.ImageView

interface GlideHelper {

    fun setImage(
        context: Context,
        url: String,
        actplayerCover: ImageView,
        roundedCornersImage: Int = DEFAULT_CORNER_RADIUS,
    )

    fun setImage(
        context: Context,
        file: String,
        imageView: ImageView
    )

    companion object {

        fun String.addQuality(): String {
            return this.replaceAfterLast('/', "512x512bb.jpg")
        }

        const val DEFAULT_CORNER_RADIUS = 10
    }

}