package com.myapp.dreamystory.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapp.dreamystory.data.UserRepository
import com.myapp.dreamystory.data.pref.UserModel
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    val responseMessage: LiveData<String?> = repository.responseMessage

    val isLoading: LiveData<Boolean> = repository.isLoading

    val responseSuccess: LiveData<Boolean> = repository.responseSuccess

    val responseLogin: LiveData<String?> = repository.responseLogin

    fun saveDataSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveDataSession(user)
        }
    }

    fun getLoginUser(email: String, password: String) {
        repository.getLoginUser(email, password)
    }
}