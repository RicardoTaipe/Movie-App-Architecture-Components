package com.example.moviearchitecturecomponents.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moviearchitecturecomponents.databinding.MoviePosterBinding
import com.example.moviearchitecturecomponents.network.response.Result

class MoviesAdapter(private val clickListener: MoviesAdapterListener) :
    ListAdapter<Result, MoviesAdapter.ViewHolder>(ResultDiffCallback) {

    interface MoviesAdapterListener {
        fun onMovieClicked(movie: Result, imageView: ImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = MoviePosterBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }

    class ViewHolder(private val binding: MoviePosterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(result: Result, clickListener: MoviesAdapterListener) {
            ViewCompat.setTransitionName(binding.movieImage, result.id.toString())
            binding.apply {
                movie = result
                root.setOnClickListener {
                    clickListener.onMovieClicked(result, binding.movieImage)
                }
                ratingBar.rating = result.voteAverage?.toFloat()?.div(2) ?: 0f
                executePendingBindings()
            }
        }
    }
}

object ResultDiffCallback : DiffUtil.ItemCallback<Result>() {
    override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
        return oldItem == newItem
    }

}
