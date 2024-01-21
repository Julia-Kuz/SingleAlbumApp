//package ru.netology.singlealbumapp
//
//import android.os.Bundle
//import androidx.activity.viewModels
//import androidx.appcompat.app.AppCompatActivity
//import androidx.lifecycle.ViewModelProvider
//import ru.netology.singlealbumapp.databinding.OneTrackBinding
//
//class TrackOneActivity : AppCompatActivity() {
//
//    private val mediaObserver = MediaLifecycleObserver()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//       val viewModel = ViewModelProvider(this)[AlbumViewModel::class.java]
//
//        val binding = OneTrackBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        viewModel.album.observe(this) {album ->
//            with (binding) {
//               oneAlbumName.text = album.title
//            }
//        }
//    }
//}