package com.marat.hvatit.playlistmaker2.presentation.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.marat.hvatit.playlistmaker2.R

class IntentNavigatorImpl(private val context: Context) : IntentNavigator {
    override fun createIntent(action: ActionFilter, message: String) {

        val intentAction: Intent
        when (action) {
            ActionFilter.SHARE -> {
                intentAction = Intent(Intent.ACTION_SEND).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).apply {
                    putExtra(Intent.EXTRA_TEXT, message)
                    type = "text/plain"
                }
                context.startActivity(intentAction)
            }

            ActionFilter.SUPPORT -> {
                val tittleSend = context.getString(R.string.tittle_Send)
                val textSend = context.getString(R.string.text_Send)
                intentAction = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:")
                    putExtra(Intent.EXTRA_EMAIL, R.string.my_email)
                    putExtra(Intent.EXTRA_SUBJECT, tittleSend)
                    putExtra(Intent.EXTRA_TEXT, textSend)

                }
                context.startActivity(intentAction)
            }

            ActionFilter.USERAGREEMENT -> {
                val textuseragreement = context.getString(R.string.text_useragreement)
                intentAction = Intent(Intent.ACTION_VIEW, Uri.parse(textuseragreement))
                context.startActivity(intentAction)
            }
        }
    }
}