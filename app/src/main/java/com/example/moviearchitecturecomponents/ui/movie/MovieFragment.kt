package com.example.moviearchitecturecomponents.ui.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.example.moviearchitecturecomponents.MainActivity
import com.example.moviearchitecturecomponents.R
import com.example.moviearchitecturecomponents.databinding.FragmentMovieBinding

class MovieFragment : Fragment() {

    private lateinit var movieViewModel: MovieViewModel
    private lateinit var binding: FragmentMovieBinding
    val args: MovieFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentMovieBinding.inflate(inflater, container, false)
        movieViewModel =
            ViewModelProvider(this).get(MovieViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        ViewCompat.setTransitionName(binding.detailMovieImage, args.selectedMovie?.id.toString())
        Glide.with(requireContext())
            .load("https://image.tmdb.org/t/p/original" + args.selectedMovie?.backdropPath)
            .placeholder(R.drawable.image_placeholder)
            .into(binding.detailMovieImage)
        Glide.with(requireContext())
            .load("https://image.tmdb.org/t/p/original" + args.selectedMovie?.posterPath)
            .placeholder(R.drawable.image_placeholder)
            .into(binding.detailBackgroundMovie)
        binding.detailMovieTitle.text = args.selectedMovie?.title
        binding.detailMovieDesc.text=args.selectedMovie?.overview
        (activity as MainActivity).setActionBarTitle(args.selectedMovie?.title)
        startPostponedEnterTransition()
        binding.executePendingBindings()
    }
}