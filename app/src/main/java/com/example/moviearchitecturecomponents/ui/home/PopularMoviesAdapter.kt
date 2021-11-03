package com.example.moviearchitecturecomponents.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviearchitecturecomponents.R
import com.example.moviearchitecturecomponents.databinding.MoviePosterBinding
import com.example.moviearchitecturecomponents.network.response.Result

class PopularMoviesAdapter(val context: Context?, private val clickListener: MovieClickListener) :
    RecyclerView.Adapter<PopularMoviesAdapter.ViewHolder>() {
    var dataSet = listOf<Result>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = MoviePosterBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(context, dataSet[position], clickListener)
    }

    override fun getItemCount() = dataSet.size

    class ViewHolder(private val binding: MoviePosterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(context: Context?, result: Result, clickListener: MovieClickListener) {
            ViewCompat.setTransitionName(binding.movieImage, result.id.toString())
            binding.root.setOnClickListener {
                clickListener.onClick(result, binding.movieImage)
            }
            binding.movieTitle.text = result.title
            Glide.with(context!!)
                .load("https://image.tmdb.org/t/p/original" + result.backdropPath)
                .placeholder(R.drawable.image_placeholder)
                .into(binding.movieImage)
            binding.ratingValue.text = result.voteAverage?.toString()
            binding.ratingBar.rating = result.voteAverage?.toFloat()!!.div(2)
            binding.executePendingBindings()
        }

    }
}