package com.marat.hvatit.playlistmaker2.presentation.audioplayer

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.marat.hvatit.playlistmaker2.R
import com.marat.hvatit.playlistmaker2.data.JsonParserImpl
import com.marat.hvatit.playlistmaker2.domain.models.Track
import com.marat.hvatit.playlistmaker2.presentation.adapters.PlaylistAdapter
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
    private lateinit var bottomSheetContainer: LinearLayout
    private lateinit var buttonAdd: ImageButton
    private lateinit var backgroundBottomSheet: CoordinatorLayout

    private val gsonParser: JsonParserImpl by inject()
    private val glide: GlideHelper by inject()
    private val playlistAdapter = PlaylistAdapter()
    private lateinit var recyclerView: RecyclerView


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

        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                // newState — новое состояние BottomSheet
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        backgroundBottomSheet.setBackgroundColor(Color.parseColor("#80000000"))
                        backgroundBottomSheet.isVisible = true

                    }

                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        backgroundBottomSheet.isVisible = false

                    }

                    BottomSheetBehavior.STATE_HIDDEN -> {
                        backgroundBottomSheet.isVisible = false

                    }

                    else -> {
                        // Остальные состояния не обрабатываем
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
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

        buttonAdd.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = playlistAdapter


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
        bottomSheetContainer = findViewById(R.id.bottom_sheet_container)
        buttonAdd = findViewById(R.id.button_addtoplaylist)
        backgroundBottomSheet = findViewById(R.id.bottom_sheet_parent)
        recyclerView = findViewById(R.id.playlists)

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