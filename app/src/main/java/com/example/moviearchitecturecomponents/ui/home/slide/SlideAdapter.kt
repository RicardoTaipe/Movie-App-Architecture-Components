package com.example.moviearchitecturecomponents.ui.home.slide

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.moviearchitecturecomponents.databinding.SlideItemBinding
import com.example.moviearchitecturecomponents.network.response.Result
import com.example.moviearchitecturecomponents.ui.home.ResultDiffCallback

class SlideAdapter(private val clickListener: SliderAdapterListener) :
    ListAdapter<Result, SlideAdapter.ViewHolder>(ResultDiffCallback) {

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
        viewHolder.bind(getItem(position), clickListener)
    }

    class ViewHolder(private val binding: SlideItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(slide: Result, clickListener: SliderAdapterListener) {
            binding.run {
                binding.slide = slide
                ViewCompat.setTransitionName(slideImage, slide.id.toString())

                root.setOnClickListener {
                    clickListener.onMovieSlideClicked(slide, binding.slideImage)
                }

                executePendingBindings()
            }
        }
    }

}