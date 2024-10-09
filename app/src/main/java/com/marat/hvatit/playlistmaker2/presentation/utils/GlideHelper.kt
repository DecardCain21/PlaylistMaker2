package com.marat.hvatit.playlistmaker2.presentation.utils

import android.content.Context
import android.net.Uri
import android.widget.ImageView

interface GlideHelper {

    fun setImage(
        context: Context,
        url: String,
        actplayerCover: ImageView,
        roundedCornersImage: Int = DEFAULT_CORNER_RADIUS,
    )

    fun setImageDb(
        context: Context,
        file: String,
        imageView: ImageView,
        roundedCornersImage: Int
    )

    fun setImageDb(
        context: Context,
        file: Uri,
        imageView: ImageView,
        roundedCornersImage: Int
    )

    companion object {

        fun String.addQuality(): String {
            return this.replaceAfterLast('/', "512x512bb.jpg")
        }

        const val DEFAULT_CORNER_RADIUS = 10
        const val VERTICAL_PLAYLIST_CORNER_RADIUS = 8
        const val HORIZONTAL_PLAYLIST_CORNER_RADIUS = 2
    }

}