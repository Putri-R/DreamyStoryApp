package com.myapp.dreamystory.view.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.myapp.dreamystory.data.UserRepository
import com.myapp.dreamystory.data.remote.response.ListStoryItem

class MapsViewModel (private val repository: UserRepository) : ViewModel() {
    val isLoading: LiveData<Boolean> = repository.isLoading

    fun getLoginToken(): LiveData<String> = repository.getLoginToken()

    fun getStoriesLocation(location: Int): LiveData<List<ListStoryItem>> {
        return repository.getStoriesLocation(location)
    }
}