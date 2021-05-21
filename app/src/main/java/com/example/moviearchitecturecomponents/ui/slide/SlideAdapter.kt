package com.example.moviearchitecturecomponents.ui.slide

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviearchitecturecomponents.databinding.SlideItemBinding
import com.example.moviearchitecturecomponents.network.NetworkConstants
import com.example.moviearchitecturecomponents.network.response.Result

class SlideAdapter(val context: Context?) :
    RecyclerView.Adapter<SlideAdapter.ViewHolder>() {
    var dataSet = listOf<Result>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(viewGroup.context)
        val binding = SlideItemBinding.inflate(layoutInflater, viewGroup, false)
        return ViewHolder(binding)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(context, dataSet[position])
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

    class ViewHolder(val binding: SlideItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(context: Context?, slide: Result) {
            binding.slideTitle.text = slide.title
            Glide.with(context!!)
                .load("${NetworkConstants.IMAGE_URL_PATH}${slide.backdropPath}")
                .placeholder(ColorDrawable(Color.GRAY))
                .into(binding.slideImage)
            binding.executePendingBindings()
        }
    }

}