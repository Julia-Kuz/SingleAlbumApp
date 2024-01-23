package ru.netology.singlealbumapp

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import ru.netology.singlealbumapp.databinding.ActivityMainBinding
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    private val mediaObserver = MediaLifecycleObserver()

    private val viewModel: AlbumViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycle.addObserver(mediaObserver)



        fun playList(trackCurrent: Track) {
            val tracks = viewModel.album.value?.tracks
            if (tracks != null) {

                for ((index, track) in tracks.withIndex()) {
                    if (trackCurrent.id == track.id) {
                        var position = index
                        var i = 0
                        mediaObserver.apply {
                            viewModel.album.value?.tracks?.find { it == tracks[position + i] }?.isPlaying = true
                            viewModel.tracksEdited.value = viewModel.album.value?.tracks
                            //viewModel.album.value?.tracks?.find { it == tracks[position + i] }?.isPlaying = true
                            play(tracks[position + i].file)
                            player?.setOnCompletionListener {
                                viewModel.album.value?.tracks?.find { it == tracks[position + i] }?.isPlaying = false
                                viewModel.tracksEdited.value = viewModel.album.value?.tracks
                                //viewModel.album.value?.tracks?.find { it == tracks[position + i] }?.isPlaying = false
                                i++
                                if ((i + position) < tracks.size) {
                                    stop()
                                    viewModel.album.value?.tracks?.find { it == tracks[position + i] }?.isPlaying = true
                                    viewModel.tracksEdited.value = viewModel.album.value?.tracks
                                    //viewModel.album.value?.tracks?.find { it == tracks[position + i] }?.isPlaying = true
                                    play(tracks[position + i].file)

                                } else {
                                    stop()
                                    playList (tracks[0])
                                }
                            }
                        }
                    }
                }
            }
        }

        var trackId = -1

        val adapter = Adapter(object : OnInteractionListener {
            override fun play(track: Track) {
                val thisTrackId = track.id
                if (mediaObserver.player?.isPlaying == true) {
                    mediaObserver.apply {
                        viewModel.album.value?.tracks?.find { it == track }?.isPlaying = false
                        viewModel.tracksEdited.value = viewModel.album.value?.tracks
                        //viewModel.album.value?.tracks?.find { it == track }?.isPlaying = false
                        stop()
                        if (thisTrackId != trackId) {
                            trackId = thisTrackId
                            viewModel.album.value?.tracks?.find { it == track }?.isPlaying = true
                            viewModel.tracksEdited.value = viewModel.album.value?.tracks
                            //viewModel.album.value?.tracks?.find { it == track }?.isPlaying = true
                            playList(track)
                        }
                    }
                } else {
                    mediaObserver.apply {
                        trackId = thisTrackId
                        viewModel.album.value?.tracks?.find { it == track }?.isPlaying = true
                        viewModel.tracksEdited.value = viewModel.album.value?.tracks
                        //viewModel.album.value?.tracks?.find { it == track }?.isPlaying = true
                        playList(track)
                    }
                }
            }
        }
        )

        binding.recyclerList.adapter = adapter

        viewModel.album.observe(this) { album ->
            with(binding) {
                albumName.text = album.title
                artistName.text = album.artist
                published.text = album.published
                genre.text = album.genre
            }

            Adapter.albumName = album.title
            adapter.submitList(album.tracks)
        }

        viewModel.tracksEdited.observe(this) {
            adapter.submitList(it)
        }

        viewModel.error.observe(this) {
            Snackbar.make(binding.recyclerList, "", Snackbar.LENGTH_LONG)
                .setAnchorView(binding.recyclerList)
                .setTextMaxLines(3)
                .setText("Something went wrong")
                .setBackgroundTint(android.graphics.Color.rgb(0, 102, 255))
                .show()
        }


        binding.playList.setImageResource(R.drawable.ic_play_circle_48)

        binding.playList.setOnClickListener {
            if (mediaObserver.player?.isPlaying == true) {
                binding.playList.setImageResource(R.drawable.ic_play_circle_48)
                mediaObserver.stop()
            } else {
                val tracks = viewModel.album.value?.tracks
                if (tracks != null) {
                    binding.playList.setImageResource(R.drawable.ic_pause_circle_48)
                    playList(tracks[0])
                }
            }
        }


        //***********  по лекции **************
//        val button: ImageButton = findViewById<View>(R.id.playButton) as ImageButton
//        button.setImageResource(R.drawable.ic_pause_circle_24)
//
//        findViewById<View>(R.id.playButton).setOnClickListener {
//            mediaObserver.apply {
//                player?.setDataSource(
//                    "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"
//                )
//            }.play()
//        }
        //**************  **************  *******************

    }

}
