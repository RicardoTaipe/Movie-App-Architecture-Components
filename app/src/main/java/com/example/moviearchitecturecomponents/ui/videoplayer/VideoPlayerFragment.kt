package com.example.moviearchitecturecomponents.ui.videoplayer

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.moviearchitecturecomponents.databinding.FragmentVideoplayerBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener

private const val VIDEO_ID = "VIDE0_ID"


class VideoPlayerFragment : DialogFragment() {

    private lateinit var binding: FragmentVideoplayerBinding

    private var videoId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            videoId = it.getString(VIDEO_ID)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            VideoPlayerFragment().apply {
                arguments = Bundle().apply {
                    putString(VIDEO_ID, param1)
                }
            }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            binding = FragmentVideoplayerBinding.inflate(layoutInflater)
            lifecycle.addObserver(binding.playerView)
            binding.playerView.enableBackgroundPlayback(false)
            binding.playerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo(videoId!!, 0f)
                }
            })
            builder.setView(binding.root)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.playerView.release()
    }
}