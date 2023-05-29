package com.example.to_dolist.util

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.*


@RequiresApi(Build.VERSION_CODES.O)
fun String.isExpired(): Boolean {
    val format = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val time = Calendar.getInstance()
    val date = format.format(time.time)
    val date1 = format.parse(date)
    val date2 = format.parse(this)


    if (date1.compareTo(date2) < 0)
        return false
    else
        return true
}

//    return this. < currentTimeMillis



