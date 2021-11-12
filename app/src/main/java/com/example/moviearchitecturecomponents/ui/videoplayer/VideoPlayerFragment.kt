package com.example.moviearchitecturecomponents.ui.videoplayer

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.moviearchitecturecomponents.R
import com.example.moviearchitecturecomponents.databinding.FragmentVideoplayerBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener

class VideoPlayerFragment : DialogFragment() {

    private lateinit var binding: FragmentVideoplayerBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            binding = FragmentVideoplayerBinding.inflate(layoutInflater)
            lifecycle.addObserver(binding.playerView)

            binding.playerView.enableBackgroundPlayback(false)
            binding.playerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    val videoId = "XK-MIqHz5tU"
                    youTubePlayer.loadVideo(videoId, 0f)
                }
            })
            builder.setView(binding.root)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?,
//    ): View? {
//        binding = FragmentVideoplayerBinding.inflate(layoutInflater, container, false)
//        lifecycle.addObserver(binding.playerView)
//
//        binding.playerView.enableBackgroundPlayback(false)
//        binding.playerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
//            override fun onReady(youTubePlayer: YouTubePlayer) {
//                val videoId = "XK-MIqHz5tU"
//                youTubePlayer.loadVideo(videoId, 0f)
//            }
//        })
//        return binding.root
//    }
//
    override fun onDestroy() {
        super.onDestroy()
        binding.playerView.release()
    }
}