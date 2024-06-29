package com.myapp.dreamystory.view.detailstory

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.myapp.dreamystory.data.UserRepository
import com.myapp.dreamystory.data.remote.response.Story

class DetailStoryViewModel(private val repository: UserRepository) : ViewModel() {
    val responseMessage: LiveData<String?> = repository.responseMessage

    val isLoading: LiveData<Boolean> = repository.isLoading

    val storyDetailResponse: LiveData<Story?> = repository.storyDetailResponse

    fun getStoryDetail(id: String) {
        repository.getStoryDetail(id)
    }
}