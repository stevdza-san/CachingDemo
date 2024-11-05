package com.stevdza_san.demo

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stevdza_san.demo.data.PostSDK
import com.stevdza_san.demo.domain.Post
import com.stevdza_san.demo.domain.RequestState
import kotlinx.coroutines.launch

typealias CachedPosts = MutableState<RequestState<List<Post>>>

class MainViewModel(
    private val sdk: PostSDK
) : ViewModel() {
    var allPosts: CachedPosts = mutableStateOf(RequestState.Idle)
        private set

    init {
        viewModelScope.launch {
            allPosts.value = sdk.getAllPosts()
        }
    }
}