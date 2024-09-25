package com.marat.hvatit.playlistmaker2.common

import android.content.Context
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

    override fun setImage(context: Context, covername: String, imageView: ImageView) {
        val filePath = File( Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Myalbum")
        val file = File(filePath, "$covername.jpg")
        Glide.with(imageView.context)
            .load(file)
            .into(imageView)
    }
    //из глайда можно вытянуть битмап
}