package com.myapp.dreamystory.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.myapp.dreamystory.data.UserRepository
import com.myapp.dreamystory.data.pref.UserModel
import com.myapp.dreamystory.data.remote.response.ListStoryItem
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository) : ViewModel() {
    val isLoading: LiveData<Boolean> = repository.isLoading

    fun getDataSession(): LiveData<UserModel> {
        return repository.getDataSession().asLiveData()
    }

    fun userLogout() {
        viewModelScope.launch {
            repository.userLogout()
        }
    }

    fun getListStory(): LiveData<PagingData<ListStoryItem>> =
        repository.getListStory().cachedIn(viewModelScope)
}
