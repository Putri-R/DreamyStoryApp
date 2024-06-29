package com.myapp.dreamystory.data

import android.location.Location
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.myapp.dreamystory.data.local.room.StoryDatabase
import com.myapp.dreamystory.data.remote.ApiService
import com.myapp.dreamystory.data.remote.response.DefaultResponse
import com.myapp.dreamystory.data.remote.response.DetailStoryResponse
import com.myapp.dreamystory.data.remote.response.ListStoryItem
import com.myapp.dreamystory.data.remote.response.ListStoryResponse
import com.myapp.dreamystory.data.remote.response.LoginResponse
import com.myapp.dreamystory.data.remote.response.LoginResult
import com.myapp.dreamystory.data.remote.response.Story
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody
import okio.Timeout
import org.junit.After
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback
import retrofit2.http.Query

@OptIn(ExperimentalCoroutinesApi::class)
@ExperimentalPagingApi
@RunWith(AndroidJUnit4::class)
class StoryRemoteMediatorTest {
    private var mockApi: ApiService = FakeApiService()
    private var mockDb: StoryDatabase = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        StoryDatabase::class.java
    ).allowMainThreadQueries().build()

    @Test
    fun refreshLoadReturnsSuccessResultWhenMoreDataIsPresent() = runTest {
        val remoteMediator = StoryRemoteMediator(
            mockDb,
            mockApi,
        )
        val pagingState = PagingState<Int, ListStoryItem>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @After
    fun tearDown() {
        mockDb.clearAllTables()
    }
}

class FakeApiService : ApiService {

    override fun dataRegister(name: String, email: String, password: String): Call<DefaultResponse> {
        val response = DefaultResponse(false, "Register success")
        val call = object : Call<DefaultResponse> {
            override fun execute(): Response<DefaultResponse> {
                return Response.success(response)
            }
            override fun enqueue(callback: Callback<DefaultResponse>) {
                callback.onResponse(this, Response.success(response))
            }
            override fun clone(): Call<DefaultResponse> {
                return this
            }
            override fun isExecuted(): Boolean {
                return true
            }
            override fun cancel() { }
            override fun isCanceled(): Boolean {
                return false
            }
            override fun request(): Request {
                TODO("Not yet implemented")
            }
            override fun timeout(): Timeout {
                TODO("Not yet implemented")
            }
        }
        return call
    }

    override fun dataLogin(email: String, password: String): Call<LoginResponse> {
        val response = LoginResponse(LoginResult(token = "mock_token"), false, "Login success")
        val call = object : Call<LoginResponse> {
            override fun execute(): Response<LoginResponse> {
                return Response.success(response)
            }
            override fun enqueue(callback: Callback<LoginResponse>) {
                callback.onResponse(this, Response.success(response))
            }
            override fun clone(): Call<LoginResponse> {
                return this
            }
            override fun isExecuted(): Boolean {
                return true
            }
            override fun cancel() { }
            override fun isCanceled(): Boolean {
                return false
            }
            override fun request(): Request {
                TODO("Not yet implemented")
            }
            override fun timeout(): Timeout {
                TODO("Not yet implemented")
            }
        }
        return call
    }

    override suspend fun getStories(page: Int, size: Int): ListStoryResponse {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val stories = ListStoryItem(
                i.toString(),
                "photoUrl + $i",
                "createdAt = $i",
                "name + $i",
                "description = $i",
                i.toDouble(),
                i.toDouble()
            )
            items.add(stories)
        }
        return ListStoryResponse(items.subList((page - 1) * size, (page - 1) * size + size))
    }

    override fun getDetailStory(id: String): Call<DetailStoryResponse> {
        val story = Story(
            "photoUrl",
            "createdAt",
            "name",
            "description",
            123.456,
            id,
            123.456
        )
        val response = DetailStoryResponse(false, "Get detail success", story)
        val call = object : Call<DetailStoryResponse> {
            override fun execute(): Response<DetailStoryResponse> {
                return Response.success(response)
            }
            override fun enqueue(callback: Callback<DetailStoryResponse>) {
                callback.onResponse(this, Response.success(response))
            }
            override fun clone(): Call<DetailStoryResponse> {
                return this
            }
            override fun isExecuted(): Boolean {
                return true
            }
            override fun cancel() { }
            override fun isCanceled(): Boolean {
                return false
            }
            override fun request(): Request {
                TODO("Not yet implemented")
            }
            override fun timeout(): Timeout {
                TODO("Not yet implemented")
            }
        }
        return call
    }

    override fun uploadStory(file: MultipartBody.Part, description: RequestBody, lat: Double?, lon: Double?): Call<DefaultResponse> {
        val response = DefaultResponse(false, "Upload story success")
        val call = object : Call<DefaultResponse> {
            override fun execute(): Response<DefaultResponse> {
                return Response.success(response)
            }
            override fun enqueue(callback: Callback<DefaultResponse>) {
                callback.onResponse(this, Response.success(response))
            }
            override fun clone(): Call<DefaultResponse> {
                return this
            }
            override fun isExecuted(): Boolean {
                return true
            }
            override fun cancel() { }
            override fun isCanceled(): Boolean {
                return false
            }
            override fun request(): Request {
                TODO("Not yet implemented")
            }
            override fun timeout(): Timeout {
                TODO("Not yet implemented")
            }
        }
        return call
    }

    override fun getStoriesWithLocation(location: Int): Call<ListStoryResponse> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val stories = ListStoryItem(
                i.toString(),
                "photoUrl + $i",
                "createdAt = $i",
                "name + $i",
                "description = $i",
                i.toDouble(),
                i.toDouble()
            )
            items.add(stories)
        }
        val response = Response.success(ListStoryResponse(items))
        val call = object : Call<ListStoryResponse> {
            override fun execute(): Response<ListStoryResponse> {
                return response
            }
            override fun enqueue(callback: Callback<ListStoryResponse>) {
                callback.onResponse(this, response)
            }
            override fun clone(): Call<ListStoryResponse> {
                return this
            }
            override fun isExecuted(): Boolean {
                return true
            }
            override fun cancel() { }
            override fun isCanceled(): Boolean {
                return false
            }
            override fun request(): Request {
                TODO("Not yet implemented")
            }
            override fun timeout(): Timeout {
                TODO("Not yet implemented")
            }
        }
        return call
    }
}