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
        private const val DONE_DATA1 = 10
        private const val DONE_DATA2 = 20
    }

    private val _data1 = mutableStateOf(INVALID_DATA)
    val data1: Int get() = _data1.value

    private val _data2 = mutableStateOf(INVALID_DATA)
    val data2: Int get() = _data2.value

    private var currentJob: Job? = null

    fun onButtonClick(useAsync: Boolean) {
        if(currentJob != null) return

        currentJob = viewModelScope.launch {
            Utils.log(TAG, "======= Created launch coroutine - onButtonClick() =======")

            val job1 = launch {
                Utils.log(TAG, "+++++++ Created sub-coroutine for - data1 +++++++")
                loadData(useAsync, start = 0, end = 9, data = _data1)
                _data1.value = DONE_DATA1
                Utils.log(TAG,"Sub-launch data1 - done!!!")
            }

            val job2 = launch {
                Utils.log(TAG, "+++++++ Created sub-coroutine for - data2 +++++++")
                loadData(useAsync, start = 10, end = 19, data = _data2)
                _data2.value = DONE_DATA2
                Utils.log(TAG,"Sub-launch data2 - done!!!")
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
            _data1.value = INVALID_DATA
            _data2.value = INVALID_DATA
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
