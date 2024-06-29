package com.myapp.dreamystory.view.detailstory

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.myapp.dreamystory.data.remote.response.Story
import com.myapp.dreamystory.databinding.ActivityDetailStoryBinding
import com.myapp.dreamystory.view.ViewModelFactory
import com.myapp.dreamystory.view.utils.toRelativeTime

class DetailStoryActivity : AppCompatActivity() {
    private val viewModel by viewModels<DetailStoryViewModel> {
        ViewModelFactory.getInstance(this)
    }
    
    private lateinit var binding: ActivityDetailStoryBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.show()
        actionBar?.title = "Detail Story"
        
        val id = intent.getStringExtra(DETAIL_STORY)
        
        if (id != null) {
            viewModel.getStoryDetail(id)
        }
        
        viewModel.storyDetailResponse.observe(this) {
            setDetailData(it)
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        viewModel.responseMessage.observe(this) {data->
            if (data == null) {
                Toast.makeText(this, "Failed to fetch data", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.responseMessage.observe(this) { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun  setDetailData(dataStory: Story?) {
        binding.apply { 
            Glide.with(this@DetailStoryActivity)
                .load(dataStory?.photoUrl)
                .into(ivDetailPhoto)
            tvDetailName.text = dataStory?.name
            tvDetailDate.text = dataStory?.createdAt?.toRelativeTime()
            tvDetailDescription.text = dataStory?.description
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    
    companion object {
        const val DETAIL_STORY = "detail_story"
    }
}