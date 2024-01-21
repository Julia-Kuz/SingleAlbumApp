package ru.netology.singlealbumapp.repository

import ru.netology.singlealbumapp.Album

interface Repository {
    fun getAllAsync(callback: GetMyCallback <Album>)

    interface GetMyCallback <T> {
        fun onSuccess(result: T) {}
        fun onError(e: Exception) {}
    }
}

