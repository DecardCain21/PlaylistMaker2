package com.marat.hvatit.playlistmaker2.presentation.audioplayer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.marat.hvatit.playlistmaker2.R
import com.marat.hvatit.playlistmaker2.creator.Creator
import com.marat.hvatit.playlistmaker2.domain.api.AudioPlayerCallback
import com.marat.hvatit.playlistmaker2.domain.api.interactors.AudioPlayerInteractor
import com.marat.hvatit.playlistmaker2.domain.models.Track
import com.marat.hvatit.playlistmaker2.presentation.utils.GlideHelper.Companion.addQuality
import java.text.SimpleDateFormat
import java.util.Locale


private const val TAG = "AudioplayerActivity"

class AudioplayerActivity : AppCompatActivity(),
    AudioPlayerCallback {

    private lateinit var intent: Intent
    private val simpleDateFormat: SimpleDateFormat =
        SimpleDateFormat("mm:ss", Locale.getDefault())


    private lateinit var actplayerCover: ImageView
    private lateinit var countryvalue: TextView
    private lateinit var genrevalue: TextView
    private lateinit var yearvalue: TextView
    private lateinit var albumvalue: TextView
    private lateinit var durationvalue: TextView
    private lateinit var artistName: TextView
    private lateinit var trackName: TextView
    private lateinit var buttonPlay: ImageButton
    private lateinit var priviewTimer: TextView
    private lateinit var priviewUrl: String

    private val creator: Creator = Creator
    private lateinit var interactor: AudioPlayerInteractor
    private val gson = creator.provideJsonParser()
    private val glide = creator.provideGlideHelper()
    private lateinit var viewModel: AudioViewModel


    companion object {

        fun getIntent(context: Context, message: String): Intent {
            return Intent(context, AudioplayerActivity::class.java).apply {
                putExtra(TAG, message)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audioplayer)
        intent = getIntent()
        val song = intent.getStringExtra("Track")
        val result: Track = gson.jsonToObject(
            song.toString(),
            Track::class.java
        )/*fromJson(song, Track::class.java)*/
        priviewUrl = result.previewUrl
        Log.e("previewUrl", "${result.previewUrl}")
        interactor = creator.provideAudioPlayer(priviewUrl, this)
        viewModel = ViewModelProvider(
            this,
            AudioViewModel.getViewModelFactory(interactor)
        )[AudioViewModel::class.java]
        //..............................................................
        initViews()
        setTextContent(result)

        val buttonBack = findViewById<View>(R.id.back)
        buttonBack.setOnClickListener {
            onBackPressed()
        }

        buttonPlay.isEnabled = false
        viewModel.playbackControl()

        buttonPlay.setOnClickListener {
            viewModel.playbackControl()
        }

        viewModel.getLoadingLiveData().observe(this) { playerState ->
            runOnUiThread {
                uiControl(playerState)
            }
        }

    }

    private fun initViews() {
        actplayerCover = findViewById(R.id.actplayer_cover)
        artistName = findViewById(R.id.actplayer_artist_name)
        trackName = findViewById(R.id.actplayer_track_name)
        countryvalue = findViewById(R.id.actplayer_countryvalue)
        genrevalue = findViewById(R.id.actplayer_genrevalue)
        yearvalue = findViewById(R.id.actplayer_yearvalue)
        albumvalue = findViewById(R.id.actplayer_albumvalue)
        durationvalue = findViewById(R.id.actplayer_durationvalue)
        buttonPlay = findViewById(R.id.actplayer_buttonplay)
        priviewTimer = findViewById(R.id.actplayer_tracktime)
    }

    private fun setTextContent(song: Track) {
        glide.setImage(
            context = this,
            url = song.artworkUrl100.addQuality(),
            actplayerCover = actplayerCover
        )
        durationvalue.text = simpleDateFormat.format(song.trackTimeMillis.toLong())
        artistName.text = song.artistName
        trackName.text = song.trackName
        countryvalue.text = song.country
        genrevalue.text = song.genre
        yearvalue.text = song.year.substring(0, song.year.indexOf("-"))
        albumvalue.text = song.album
        priviewTimer.text = getString(R.string.default_timer)
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopPlayer()
        buttonPlay.setBackgroundResource(R.drawable.button_play)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.destroyPlayer()
    }

    private fun uiControl(state: MediaPlayerState) {
        when (state) {

            MediaPlayerState.Default -> {
                buttonPlay.setBackgroundResource(R.drawable.button_play)
                buttonPlay.isEnabled = true
            }

            is MediaPlayerState.Paused -> {
                buttonPlay.setBackgroundResource(R.drawable.button_play)
                Log.e("MediaState", "is MediaPlayerState.Paused")
            }

            is MediaPlayerState.Playing -> {
                buttonPlay.setBackgroundResource(R.drawable.button_stop)
                priviewTimer.text = state.currentTime
            }

            MediaPlayerState.Prepared -> {
                buttonPlay.setBackgroundResource(R.drawable.button_play)
            }
        }
    }

    override fun trackIsDone() {
        viewModel.trackIsDone()
        priviewTimer.text = getString(R.string.isDone_timer)
    }
}