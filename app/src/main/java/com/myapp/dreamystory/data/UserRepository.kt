package com.myapp.dreamystory.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.google.gson.Gson
import com.myapp.dreamystory.data.local.room.StoryDatabase
import com.myapp.dreamystory.data.pref.UserModel
import com.myapp.dreamystory.data.pref.UserPreference
import com.myapp.dreamystory.data.remote.ApiService
import com.myapp.dreamystory.data.remote.response.DetailStoryResponse
import com.myapp.dreamystory.data.remote.response.ListStoryItem
import com.myapp.dreamystory.data.remote.response.ListStoryResponse
import com.myapp.dreamystory.data.remote.response.LoginResponse
import com.myapp.dreamystory.data.remote.response.DefaultResponse
import com.myapp.dreamystory.data.remote.response.Story
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository private constructor(private val storyDatabase: StoryDatabase, private val apiService: ApiService, private val userPreference: UserPreference) {
    private val _responseMessage = MutableLiveData<String?>()
    val  responseMessage: LiveData<String?> = _responseMessage

    private val _responseSuccess = MutableLiveData<Boolean>()
    val  responseSuccess: LiveData<Boolean> = _responseSuccess

    private val _responseLogin = MutableLiveData<String?>()
    val  responseLogin: LiveData<String?> = _responseLogin

    private val _storyMapResponse = MutableLiveData<List<ListStoryItem>>()

    private val _storyDetailResponse = MutableLiveData<Story?>()
    val storyDetailResponse: MutableLiveData<Story?> = _storyDetailResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getSignupUser(name: String, email: String, password: String) {
        _isLoading.value = true

        val user = apiService.dataRegister(name, email, password)
        user.enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(
                call: Call<DefaultResponse>,
                response: Response<DefaultResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _responseSuccess.value = true
                    _responseMessage.value = response.body()?.message ?: "Your Registration is Successful"
                } else {
                    _responseSuccess.value = false
                    val errorRegistration = response.errorBody()?.string()
                    if (errorRegistration != null) {
                        val errorResponse = Gson().fromJson(errorRegistration, DefaultResponse::class.java)
                        _responseMessage.value = errorResponse.message
                    } else {
                        _responseMessage.value = "Registration failed: ${response.message()}"
                    }
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                _isLoading.value = false
                _responseSuccess.value = false
            }
        })
    }

    fun getLoginUser(email: String, password: String) {
        _isLoading.value = true
        val user = apiService.dataLogin(email, password)
        user.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _responseSuccess.value = true
                    _responseLogin.value = response.body()?.loginResult?.token
                } else {
                    val errorLogin = response.errorBody()?.string()
                    if (errorLogin != null) {
                        val errorResponse = Gson().fromJson(errorLogin, LoginResponse::class.java)
                        _responseSuccess.value = false
                        _responseMessage.value = errorResponse.message
                    } else {
                        _responseMessage.value = "Login failed: ${response.message()}"
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                _responseSuccess.value = false
            }
        })
    }

    suspend fun saveDataSession(user: UserModel) {
        userPreference.saveDataSession(user)
    }

    fun getDataSession(): Flow<UserModel> {
        return userPreference.getDataSession()
    }

    fun getLoginToken(): LiveData<String> = userPreference.getLoginToken().asLiveData()

    suspend fun userLogout() {
        userPreference.userLogout()
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getListStory(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService),
            pagingSourceFactory = {
                storyDatabase.storiesDao().getAllStories()
            }
        ).liveData
    }

    fun getStoriesLocation(location: Int): LiveData<List<ListStoryItem>> {
        _isLoading.value = true

        val data = apiService.getStoriesWithLocation(location)
        data.enqueue(object : Callback<ListStoryResponse> {
            override fun onResponse(
                call: Call<ListStoryResponse>,
                response: Response<ListStoryResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _storyMapResponse.value = response.body()?.listStory
                } else {
                    _responseMessage.value = "Failed: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<ListStoryResponse>, t: Throwable) {
                _isLoading.value = false
                _responseMessage.value = "Failed: ${t.message.toString()}"
            }
        })
        return _storyMapResponse
    }

    fun getStoryDetail(id: String) {
        _isLoading.value = true

        val data = apiService.getDetailStory(id)
        data.enqueue(object : Callback<DetailStoryResponse> {
            override fun onResponse(
                call: Call<DetailStoryResponse>,
                response: Response<DetailStoryResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _storyDetailResponse.value = response.body()?.story
                } else {
                    _responseMessage.value = "Failed: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<DetailStoryResponse>, t: Throwable) {
                _isLoading.value = false
                _responseMessage.value = "Failed: ${t.message.toString()}"
            }
        })
    }

    fun getNewStory(file: MultipartBody.Part, description: RequestBody, lat: Double?, lon: Double?) {
        _isLoading.value = true

        val user = apiService.uploadStory(file, description, lat, lon)
        user.enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(
                call: Call<DefaultResponse>,
                response: Response<DefaultResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _responseSuccess.value = true
                    _responseMessage.value = response.body()?.message ?: "Successfully uploaded your story!"
                } else {
                    _responseSuccess.value = false
                    val errorUploadStory = response.errorBody()?.string()
                    if (errorUploadStory != null) {
                        val errorResponse = Gson().fromJson(errorUploadStory, DefaultResponse::class.java)
                        _responseMessage.value = errorResponse.message
                    } else {
                        _responseMessage.value = "Registration failed: ${response.message()}"
                    }
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                _isLoading.value = false
                _responseSuccess.value = false
                _responseMessage.value = "Failed: ${t.message.toString()}"
            }
        })
    }

    companion object {
        fun getInstance(
            storyDatabase: StoryDatabase,
            apiService: ApiService,
            userPreference: UserPreference
        ) = UserRepository(storyDatabase, apiService, userPreference)
    }
}