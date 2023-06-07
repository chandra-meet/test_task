package com.example.to_dolist.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.util.*


@RequiresApi(Build.VERSION_CODES.O)
fun Date.isExpired(): Boolean {
    val currentTime = Date()
    if (currentTime.compareTo(this) < 0)
        return false
    else
        return true
}




