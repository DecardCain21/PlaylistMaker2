package com.marat.hvatit.playlistmaker2.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.marat.hvatit.playlistmaker2.R
import com.marat.hvatit.playlistmaker2.domain.models.Track
import com.marat.hvatit.playlistmaker2.presentation.utils.GlideHelper
import org.koin.java.KoinJavaComponent.inject

class TrackListAdapter(
) : RecyclerView.Adapter<TrackViewHolder>() {
    var clickTrackListener: ClickTrackListener? = null
    var longClickTrackListener: LongClickTrackListener? = null
    private var tracklist: List<Track> = emptyList()
    private val glide: GlideHelper by inject(GlideHelper::class.java)
    //Почему в адаптере требуется указать джава класс?
    //import org.koin.java.KoinJavaComponent.inject потому что RecyclerView класс Android написанный на Java?

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_cell, parent, false)
        return TrackViewHolder(view as ViewGroup, glide)
    }

    override fun getItemCount() = tracklist.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val item = tracklist[position]
        holder.bind(tracklist[position])
        holder.itemView.setOnClickListener {
            clickTrackListener?.addTrack(item)
        }
        holder.itemView.setOnLongClickListener {
            val pos = holder.adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                longClickTrackListener?.doWork(item)
            }
            true
        }
    }

    fun update(trackList: List<Track>) {
        this.tracklist = trackList
    }

    fun interface ClickTrackListener {
        fun addTrack(item: Track)

    }

    fun interface LongClickTrackListener {
        fun doWork(item: Track)
    }
}