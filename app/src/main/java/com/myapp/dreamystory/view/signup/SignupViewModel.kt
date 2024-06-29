package com.myapp.dreamystory.view.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.myapp.dreamystory.data.UserRepository

class SignupViewModel(private val repository: UserRepository) : ViewModel() {
    val responseMessage: LiveData<String?> = repository.responseMessage

    val isLoading: LiveData<Boolean> = repository.isLoading

    val responseSuccess: LiveData<Boolean> = repository.responseSuccess

    fun getSignupUser(name: String, email: String, password: String) {
        repository.getSignupUser(name, email, password)
    }
}