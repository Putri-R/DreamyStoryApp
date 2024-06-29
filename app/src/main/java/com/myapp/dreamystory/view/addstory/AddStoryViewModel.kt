package com.myapp.dreamystory.view.addstory

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.myapp.dreamystory.data.UserRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(private val repository: UserRepository) : ViewModel() {
    val isLoading: LiveData<Boolean> = repository.isLoading

    fun getLoginToken(): LiveData<String> = repository.getLoginToken()

    fun getNewStory(file: MultipartBody.Part, description: RequestBody, lat: Double?, lon: Double?) {
        repository.getNewStory(file, description, lat, lon)
    }
}