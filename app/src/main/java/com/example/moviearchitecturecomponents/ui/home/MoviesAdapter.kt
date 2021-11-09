package com.example.moviearchitecturecomponents.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.moviearchitecturecomponents.databinding.MoviePosterBinding
import com.example.moviearchitecturecomponents.network.NetworkConstants
import com.example.moviearchitecturecomponents.network.response.Result
import com.example.moviearchitecturecomponents.util.ImageUtil

class MoviesAdapter(private val clickListener: MoviesAdapterListener) :
    RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {
    var dataSet = listOf<Result>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    interface MoviesAdapterListener {
        fun onMovieClicked(movie: Result, imageView: ImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = MoviePosterBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataSet[position], clickListener)
    }

    override fun getItemCount() = dataSet.size

    class ViewHolder(private val binding: MoviePosterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(result: Result, clickListener: MoviesAdapterListener) {
            val context = itemView.context;
            ViewCompat.setTransitionName(binding.movieImage, result.id.toString())
            binding.root.setOnClickListener {
                clickListener.onMovieClicked(result, binding.movieImage)
            }
            binding.movieTitle.text = result.title
            ImageUtil.setImageFromUrl(binding.movieImage,
                "${NetworkConstants.IMAGE_URL_PATH}${result?.backdropPath}")
            binding.ratingValue.text = result.voteAverage?.toString()
            binding.ratingBar.rating = result.voteAverage?.toFloat()!!.div(2)
            binding.executePendingBindings()
        }

    }
}