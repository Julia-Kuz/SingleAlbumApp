package ru.netology.singlealbumapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.singlealbumapp.repository.Repository
import ru.netology.singlealbumapp.repository.RepositoryImpl

class AlbumViewModel: ViewModel() {

    private val repository: Repository = RepositoryImpl()

    val album : MutableLiveData <Album> by lazy {
        MutableLiveData <Album>()
    }

    val tracksEdited: MutableLiveData <List<Track>> by lazy {
        MutableLiveData <List<Track>>()
    }

    private val _error = SingleLiveEvent<Unit>()
    val error: LiveData<Unit> = _error

    init {
        loadAlbum()
    }

    private fun loadAlbum() {
        repository.getAllAsync(object : Repository.GetMyCallback<Album> {
            override fun onSuccess(result: Album ) {
               album.postValue(result)
            }
            override fun onError(e: Exception) {
                _error.postValue(Unit)
            }
        })
    }

}