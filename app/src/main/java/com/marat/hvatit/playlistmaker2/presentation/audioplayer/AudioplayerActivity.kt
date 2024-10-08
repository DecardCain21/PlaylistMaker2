package com.marat.hvatit.playlistmaker2.presentation.audioplayer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.marat.hvatit.playlistmaker2.R
import com.marat.hvatit.playlistmaker2.data.JsonParserImpl
import com.marat.hvatit.playlistmaker2.domain.models.Track
import com.marat.hvatit.playlistmaker2.presentation.utils.GlideHelper
import com.marat.hvatit.playlistmaker2.presentation.utils.GlideHelper.Companion.addQuality
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale


private const val TAG = "AudioplayerActivity"

class AudioplayerActivity : AppCompatActivity() {

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
    private var priviewUrl: String = ""
    private lateinit var buttonFavorite: ImageButton

    private val gsonParser: JsonParserImpl by inject()
    private val glide: GlideHelper by inject()


    private val viewModel: AudioViewModel by viewModel {
        parametersOf(priviewUrl)
    }


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
        val result: Track = gsonParser.jsonToObject(song.toString(), Track::class.java)
        priviewUrl = result.previewUrl
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

        viewModel.getFavoriteState().observe(this) { it ->
            stateFavorite(it)
        }


        viewModel.defaultFavoriteState(result)
        buttonFavorite.setOnClickListener {
            viewModel.setFavoriteState(result)
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
        buttonFavorite = findViewById(R.id.button_favorite)
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
        viewModel.onPausePlayer()
        buttonPlay.setBackgroundResource(R.drawable.button_play)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroyPlayer()
    }

    private fun stateFavorite(state: FavoriteState) {
        if (state is FavoriteState.IsFavorite && state.favorite) {
            buttonFavorite.setBackgroundResource(R.drawable.favorite_button_active)
        } else {
            buttonFavorite.setBackgroundResource(R.drawable.favorite_button_unactive)
        }
    }

    private fun uiControl(state: MediaPlayerState) {
        when (state) {
            is MediaPlayerState.Default -> {
                buttonPlay.setBackgroundResource(R.drawable.button_play)
            }

            is MediaPlayerState.Paused -> {
                buttonPlay.setBackgroundResource(R.drawable.button_play)
            }

            is MediaPlayerState.Playing -> {
                buttonPlay.setBackgroundResource(R.drawable.button_stop)
                priviewTimer.text = state.currentTime
            }

            is MediaPlayerState.Prepared -> {
                buttonPlay.setBackgroundResource(R.drawable.button_play)
                buttonPlay.isEnabled = true
            }

            is MediaPlayerState.Completed -> {
                buttonPlay.setBackgroundResource(R.drawable.button_play)
                priviewTimer.text = state.currentTime
            }

            is MediaPlayerState.Disconnected -> {
                Toast.makeText(this, "Disconnect_problem", Toast.LENGTH_LONG).show()
            }
        }
    }

}