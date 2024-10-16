package com.marat.hvatit.playlistmaker2.common

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.marat.hvatit.playlistmaker2.R
import com.marat.hvatit.playlistmaker2.presentation.utils.GlideHelper
import java.io.File

class GlideHelperImpl : GlideHelper {

    override fun setImage(
        context: Context,
        url: String,
        actplayerCover: ImageView,
        roundedCornersImage: Int,
    ) {
        Glide.with(context)
            .load(url)
            .placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(roundedCornersImage))
            .into(actplayerCover)
    }

    override fun setImageDb(
        context: Context,
        covername: String,
        imageView: ImageView,
        roundedCornersImage: Int
    ) {
        val filePath = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            "Myalbum"
        )
        val file = File(filePath, "$covername.jpg")
        loadImage(imageView, file = file, roundedCornersImage = roundedCornersImage)
    }

    override fun setImageDb(
        context: Context,
        file: Uri,
        imageView: ImageView,
        roundedCornersImage: Int
    ) {
        loadImage(imageView, uri = file, roundedCornersImage = roundedCornersImage)
    }

    private fun loadImage(
        imageView: ImageView,
        file: File? = null,
        uri: Uri? = null,
        roundedCornersImage: Int
    ) {
        val placeholder = when (roundedCornersImage) {
            GlideHelper.HORIZONTAL_PLAYLIST_CORNER_RADIUS -> R.drawable.placeholder
            else -> R.drawable.placeholder_big
        }

        Glide.with(imageView.context)
            .load(file ?: uri) // Загружаем либо file, либо uri
            .placeholder(placeholder)
            .transform(RoundedCorners(roundedCornersImage))
            .into(imageView)
    }

    //из глайда можно вытянуть битмап
}