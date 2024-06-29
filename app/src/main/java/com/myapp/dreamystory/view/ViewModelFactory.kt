package com.myapp.dreamystory.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.myapp.dreamystory.data.UserRepository
import com.myapp.dreamystory.di.Injection
import com.myapp.dreamystory.view.addstory.AddStoryViewModel
import com.myapp.dreamystory.view.detailstory.DetailStoryViewModel
import com.myapp.dreamystory.view.login.LoginViewModel
import com.myapp.dreamystory.view.main.MainViewModel
import com.myapp.dreamystory.view.maps.MapsViewModel
import com.myapp.dreamystory.view.signup.SignupViewModel

class ViewModelFactory(private val repository: UserRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                SignupViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DetailStoryViewModel::class.java) -> {
                DetailStoryViewModel(repository) as T
            }
            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> {
                AddStoryViewModel(repository) as T
            }
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        fun getInstance(context: Context) = ViewModelFactory(Injection.provideRepository(context))
    }
}