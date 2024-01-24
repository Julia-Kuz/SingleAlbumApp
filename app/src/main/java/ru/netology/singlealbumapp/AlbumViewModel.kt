package ru.netology.singlealbumapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.singlealbumapp.repository.Repository
import ru.netology.singlealbumapp.repository.RepositoryImpl

class AlbumViewModel: ViewModel() {

    private val repository: Repository = RepositoryImpl()

    private val _album = MutableLiveData(Album())
    val album: LiveData<Album> = _album

    private val _error = SingleLiveEvent<Unit>()
    val error: LiveData<Unit> = _error

    init {
        loadAlbum()
    }

    private fun loadAlbum() {
        repository.getAllAsync(object : Repository.GetMyCallback<Album> {
            override fun onSuccess(result: Album ) {
               _album.postValue(result)
            }
            override fun onError(e: Exception) {
                _error.postValue(Unit)
            }
        })
    }

    fun editTrackFalse (track: Track?) {
        val editedTrack = Track(track?.id, track?.file, isPlaying = false)
        val updatedTracks = _album.value?.tracks?.map {
            if ( it.id == track?.id) editedTrack else it
        }
        _album.value = _album.value?.copy(tracks = updatedTracks)
    }

    fun editTrackTrue (track: Track?) {
        val editedTrack = Track(track?.id, track?.file, isPlaying = true)
        val updatedTracks = _album.value?.tracks?.map {
            if ( it.id == track?.id) editedTrack else Track(it.id, it.file, isPlaying = false)
        }
        _album.value = _album.value?.copy(tracks = updatedTracks)
    }

    fun allStop () {
        val updatedTracks = _album.value?.tracks?.map {
            Track(it.id, it.file, isPlaying = false)
        }
        _album.value = _album.value?.copy(isAlbumPlaying = false, tracks = updatedTracks)
    }

    fun editAlbumFalse () {
        _album.value = _album.value?.copy(isAlbumPlaying = false)
    }

    fun editAlbumTrue () {
        _album.value = _album.value?.copy(isAlbumPlaying = true)
    }


}