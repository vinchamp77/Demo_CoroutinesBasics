package com.example.coroutinesbasics.main

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.input.key.Key.Companion.Sleep
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.Delay
import kotlinx.coroutines.NonCancellable.isActive
import java.lang.Exception
import kotlin.coroutines.coroutineContext

class MainViewModel : ViewModel() {

    private val _data = mutableStateOf(-1)
    val data: Int
        get() = _data.value

    private var job: Job? = null

    fun onLaunchButtonClick() {
        viewModelScope.launch {
            log("Created new coroutine #1")
            job?.cancelAndJoin()

            job = launch {
                try {
                    log("Created new coroutine #2")
                    loadData()
                } catch (e: Exception) {
                    log("Failed to load data! Error: $e")
                }
            }
        }
    }

    fun onCancelButtonClick() {
        viewModelScope.launch {
            log("Created new coroutine #3")
            job?.cancelAndJoin()
            job = null
            _data.value = -1
        }
    }

    private suspend fun loadData() {
        withContext(Dispatchers.Default) {
            log("Switched thread to Dispatchers.Default")
            repeat(500) { index ->
                _data.value = index
                simulateLongRunningTask()

                yield()
            }
        }
    }

    private suspend fun simulateLongRunningTask() {
        Thread.sleep(200)
        yield() // ensure this coroutine is cancellable
        simulateBackgroundTask()
    }

    private suspend fun simulateBackgroundTask() {
        delay(200)
    }

    private suspend fun log(msg: String) {
        Log.d("vtsen", "[$coroutineContext]: $msg")
    }
}
