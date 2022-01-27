package com.example.coroutinesbasics.main

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coroutinesbasics.utils.Utils
import kotlinx.coroutines.*

class MainViewModel(
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {

    companion object {
        private const val TAG = "ViewModel"
        private const val INVALID_DATA = -1
        private const val DONE_LEFT_DATA = 10
        private const val DONE_RIGHT_DATA = 20
    }

    private val _leftData = mutableStateOf(INVALID_DATA)
    val leftData: Int get() = _leftData.value

    private val _rightData = mutableStateOf(INVALID_DATA)
    val rightData: Int get() = _rightData.value

    private var currentJob: Job? = null

    fun onButtonClick(useAsync: Boolean) {
        if(currentJob != null) return

        currentJob = viewModelScope.launch {
            Utils.log(TAG, "======= Created launch coroutine - onButtonClick() =======")

            val job1 = launch {
                Utils.log(TAG, "+++++++ Created sub-coroutine for - left data +++++++")
                loadData(useAsync, start = 0, end = 9, data = _leftData)
                _leftData.value = DONE_LEFT_DATA
                Utils.log(TAG,"Sub-launch left data - done!!!")
            }

            val job2 = launch {
                Utils.log(TAG, "+++++++ Created sub-coroutine for - right data +++++++")
                loadData(useAsync, start = 10, end = 19, data = _rightData)
                _rightData.value = DONE_RIGHT_DATA
                Utils.log(TAG,"Sub-launch right data - done!!!")
            }

            job1.join()
            job2.join()
            currentJob = null
            Utils.log(TAG,"Launch load data1 and data2 done!!!")
        }
    }

    fun onCancelButtonClick() {
        if (currentJob == null) return

        viewModelScope.launch {
            Utils.log(TAG, "======= Created cancel coroutine - onCancelButtonClick() =======")
            currentJob!!.cancelAndJoin()
            _leftData.value = INVALID_DATA
            _rightData.value = INVALID_DATA
            currentJob = null
            Utils.log(TAG, "onCancelButtonClick() -  done!!!")
        }
    }

    private suspend fun loadData(
        useAsync : Boolean,
        start: Int,
        end: Int,
        data: MutableState<Int>
    ) {
        withContext(dispatcher) {
            Utils.log(TAG,"Switched thread to $dispatcher")
            for(index in start..end) {

                if (useAsync) {
                    val deferred = async {
                        Utils.log(TAG, "+++++++ Created async coroutine - getData($index) +++++++")
                        getData(index)
                    }
                    data.value = deferred.await()

                } else {
                    data.value = getData(index)
                    allowCoroutineCancellable()
                }
            }
        }
    }

    private suspend fun getData(input: Int) : Int {
        simulateLongRunningTask()
        return input
    }

    private suspend fun simulateLongRunningTask() {
        simulateBlockingThreadTask()
        simulateNonBlockingThreadTask()
    }

    private suspend fun simulateBlockingThreadTask() {
        repeat(10) {
            Thread.sleep(20)
            allowCoroutineCancellable()
        }
    }

    private suspend fun simulateNonBlockingThreadTask() {
        delay(200)
    }

    private suspend fun allowCoroutineCancellable() {
        yield()
    }
}
