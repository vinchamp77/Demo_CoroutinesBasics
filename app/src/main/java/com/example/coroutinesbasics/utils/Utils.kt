package com.example.coroutinesbasics.utils

import android.util.Log
import kotlin.coroutines.coroutineContext

object Utils {
    suspend fun log(tag: String, msg: String) {
        Log.d(tag, "$coroutineContext: $msg")
    }
}