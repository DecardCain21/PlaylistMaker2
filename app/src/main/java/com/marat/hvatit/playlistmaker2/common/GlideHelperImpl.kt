package com.marat.hvatit.playlistmaker2.common

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.util.Log
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
        placeholder: Int
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
        roundedCornersImage: Int,
        placeholder: Int
    ) {
        val filePath = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            "Myalbum"
        )
        val file = File(filePath, "$covername.jpg")
        loadImage(
            imageView,
            file = file,
            roundedCornersImage = roundedCornersImage,
            placeholder = placeholder
        )
    }

    override fun setImageDb(
        context: Context,
        file: Uri,
        imageView: ImageView,
        roundedCornersImage: Int,
        placeholder: Int
    ) {
        loadImage(
            imageView,
            uri = file,
            roundedCornersImage = roundedCornersImage,
            placeholder = placeholder
        )
    }

    private fun loadImage(
        imageView: ImageView,
        file: File? = null,
        uri: Uri? = null,
        roundedCornersImage: Int,
        placeholder: Int
    ) {
        var result = 0
        if (placeholder != 0) {
            result = R.drawable.placeholder230
        } else {
            when (roundedCornersImage) {
                GlideHelper.HORIZONTAL_PLAYLIST_CORNER_RADIUS -> result = R.drawable.placeholder
                else -> result = R.drawable.placeholder_big
            }
        }
        Glide.with(imageView.context)
            .load(file ?: uri) // Загружаем либо file, либо uri
            .placeholder(result)
            .transform(RoundedCorners(roundedCornersImage))
            .into(imageView)
    }

    //из глайда можно вытянуть битмап
}