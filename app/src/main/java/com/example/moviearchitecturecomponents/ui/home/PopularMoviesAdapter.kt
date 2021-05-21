package com.example.moviearchitecturecomponents.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviearchitecturecomponents.databinding.MoviePosterBinding
import com.example.moviearchitecturecomponents.network.response.Result
import com.example.moviearchitecturecomponents.ui.slide.SlideAdapter

class PopularMoviesAdapter(val context: Context?) :
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
        holder.bind(context, dataSet[position])
    }

    override fun getItemCount() = dataSet.size

    class ViewHolder(val binding: MoviePosterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(context: Context?, result: Result) {
            binding.movieTitle.text = result.title
            Glide.with(context!!)
                .load("https://image.tmdb.org/t/p/original" + result.backdropPath)
                .into(binding.movieImage)
            binding.ratingValue.text=result.voteAverage.toString()
            binding.ratingBar.rating= result.voteAverage?.toFloat()!!.div(2)
            binding.executePendingBindings()
        }

    }
}