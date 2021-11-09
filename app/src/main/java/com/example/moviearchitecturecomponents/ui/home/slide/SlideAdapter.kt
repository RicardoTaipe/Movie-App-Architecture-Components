package com.example.moviearchitecturecomponents.ui.home.slide

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviearchitecturecomponents.databinding.SlideItemBinding
import com.example.moviearchitecturecomponents.network.NetworkConstants
import com.example.moviearchitecturecomponents.network.response.Result
import com.example.moviearchitecturecomponents.ui.home.MovieClickListener
import com.example.moviearchitecturecomponents.util.ImageUtil

class SlideAdapter(private val clickListener: SliderAdapterListener) :
    RecyclerView.Adapter<SlideAdapter.ViewHolder>() {
    var dataSet = listOf<Result>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    interface SliderAdapterListener {
        fun onMovieSlideClicked(movie: Result, imageView: ImageView)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(viewGroup.context)
        val binding = SlideItemBinding.inflate(layoutInflater, viewGroup, false)
        return ViewHolder(binding)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(dataSet[position], clickListener)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

    class ViewHolder(private val binding: SlideItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val context = itemView.context

        fun bind(slide: Result, clickListener: SliderAdapterListener) {
            binding.run {
                ViewCompat.setTransitionName(slideImage, slide.id.toString())

                slideTitle.text = slide.title

                root.setOnClickListener {
                    clickListener.onMovieSlideClicked(slide, binding.slideImage)
                }

                ImageUtil.setImageFromUrl(slideImage,
                    "${NetworkConstants.IMAGE_URL_PATH}${slide.backdropPath}")
                executePendingBindings()
            }
        }
    }

}