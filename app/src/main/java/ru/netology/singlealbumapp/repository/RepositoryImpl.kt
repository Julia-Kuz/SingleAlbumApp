package ru.netology.singlealbumapp.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import ru.netology.singlealbumapp.Album
import java.io.IOException
import java.util.concurrent.TimeUnit

class RepositoryImpl: Repository {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()

    private val gson = Gson()

    private val type = object : TypeToken<Album>() {}.type //нужно для парсинга

    companion object {
        private const val BASE_URL = "https://github.com/netology-code/andad-homeworks/raw/master/09_multimedia/data/album.json"
    }

    override fun getAllAsync(callback: Repository.GetMyCallback <Album>) {
        val request = Request.Builder()
            .url(BASE_URL)
            .build()      // создали запрос

        client.newCall(request)
            //при импорте интерфейса Callback нужно выбирать библиотеку okhttp3
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {  //ошибка соединения с сервером
                    callback.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    val responseString = response.body?.string()
                    try {
                        callback.onSuccess(gson.fromJson(responseString, type))
                    } catch (e: Exception) {   // ответ сервера с ошибкой, вернул не то, что нам нужно
                        callback.onError(e)
                    }
                }
            })
    }

}