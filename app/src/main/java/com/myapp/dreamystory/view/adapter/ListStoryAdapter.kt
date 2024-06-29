package com.myapp.dreamystory.view.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.myapp.dreamystory.R
import com.myapp.dreamystory.data.remote.response.ListStoryItem
import com.myapp.dreamystory.databinding.ItemLayoutBinding
import com.myapp.dreamystory.view.detailstory.DetailStoryActivity
import com.myapp.dreamystory.view.utils.toRelativeTime

class ListStoryAdapter : PagingDataAdapter<ListStoryItem, ListStoryAdapter.ListViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val stories = getItem(position)
        if (stories != null) {
            holder.bind(stories)
        }
    }

    inner class ListViewHolder(private val binding: ItemLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(stories: ListStoryItem) {
            binding.apply {
                stories.photoUrl?.let {
                    Glide.with(itemView.context)
                        .load(it)
                        .error(R.drawable.dreamy_story)
                        .into(ivItemPhoto)
                }
                tvItemName.text = stories.name
                tvItemTime.text = stories.createdAt?.toRelativeTime()
                tvItemDescription.text = stories.description

                itemView.setOnClickListener {
                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            itemView.context as Activity,
                            Pair(ivItemPhoto, "image"),
                            Pair(tvItemName, "name"),
                            Pair(tvItemTime, "time"),
                            Pair(tvItemDescription, "desc"),
                        )

                    val intent = Intent(itemView.context, DetailStoryActivity::class.java)
                    intent.putExtra(DetailStoryActivity.DETAIL_STORY, stories.id)
                    itemView.context.startActivity(intent, optionsCompat.toBundle())
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}