package com.marat.hvatit.playlistmaker2.presentation.audioplayer

import com.marat.hvatit.playlistmaker2.domain.api.AudioPlayerCallback

interface AudioPlayerController {

    var activityCallBack : AudioPlayerCallback?

    var previewUrl: String?

    fun stateControl(): MediaPlayerState

    fun getCurrentTime(): String

    fun destroyActivity()

    fun pauseActivity()

}