package com.example.moviearchitecturecomponents.ui.movie.cast

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.moviearchitecturecomponents.databinding.CastItemBinding
import com.example.moviearchitecturecomponents.network.NetworkConstants
import com.example.moviearchitecturecomponents.network.response.Cast
import com.example.moviearchitecturecomponents.util.ImageUtil

class CastAdapter :
    RecyclerView.Adapter<CastAdapter.ViewHolder>() {
    var dataSet = listOf<Cast>()
        set(value) {
            field = if (value.size > 10) {
                value.take(10)
            } else {
                value
            }
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = CastItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataSet[position])
    }

    override fun getItemCount() = dataSet.size

    class ViewHolder(private val binding: CastItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cast: Cast) {
            val context = itemView.context;
            binding.castName.text = cast.name
            binding.castCharacter.text = cast.character
            ImageUtil.setCircleImageFromUrl(binding.castImage,
                "${NetworkConstants.IMAGE_URL_PATH}${cast.profilePath}")
            binding.executePendingBindings()
        }

    }
}