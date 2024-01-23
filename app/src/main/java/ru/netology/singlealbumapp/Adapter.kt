package ru.netology.singlealbumapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.singlealbumapp.databinding.OneTrackBinding


interface OnInteractionListener {
    fun play (track:Track)
}

class Adapter (private val onInteractionListener: OnInteractionListener):
    ListAdapter < Track , ViewHolder>(PlaceDiffCallback()) {

    companion object {
        var albumName = ""
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            OneTrackBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onInteractionListener, albumName)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val track = getItem(position)
        holder.bind(track)
    }
}

class ViewHolder(
    private val binding: OneTrackBinding,
    private val onInteractionListener: OnInteractionListener,
    private var albumName: String?
) : RecyclerView.ViewHolder(binding.root) {


    fun bind(track: Track) {
        binding.apply {

            oneAlbumName.text = albumName ?: ""
            oneTrackName.text = track.file

            if (track.isPlaying) {
                binding.playButton.setImageResource(R.drawable.ic_pause_circle_24)
            } else {
                binding.playButton.setImageResource(R.drawable.ic_play_circle_24)
            }

            binding.playButton.setOnClickListener {
                if (track.isPlaying) {
                    binding.playButton.setImageResource(R.drawable.ic_play_circle_24)
                } else {
                    binding.playButton.setImageResource(R.drawable.ic_pause_circle_24)
                }

                onInteractionListener.play(track)
            }

        }
    }
}

class PlaceDiffCallback : DiffUtil.ItemCallback<Track>() {
    override fun areItemsTheSame(oldItem: Track, newItem: Track): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Track, newItem: Track): Boolean {
        return oldItem == newItem
    }
}




