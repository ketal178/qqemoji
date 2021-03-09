package com.ketal.qemoji.utils

import android.util.Log
import com.ketal.qemoji.data.hostInfo

private const val TAG = "QEmoji"

fun loge(exception: Exception) {
    Log.e(TAG, "${hostInfo.hostName}:", exception)
}

fun loge(throwable: Throwable) {
    Log.e(TAG, "${hostInfo.hostName}:", throwable)
}

fun loge(msg: String) {
    Log.e(TAG, "${hostInfo.hostName}:$msg")
}

fun logi(info: String) {
    Log.i(TAG, "${hostInfo.hostName}:$info")
}

/* 日志工具类 Top-Level */