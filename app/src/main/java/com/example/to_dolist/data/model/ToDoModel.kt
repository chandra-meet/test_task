package com.example.to_dolist.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.to_dolist.util.DateConverterType
import kotlinx.android.parcel.Parcelize
import java.util.*

@Entity(tableName = "tblToDo")
@Parcelize
data class ToDoModel(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var title: String,
    var date: Date,
    var status: String,
) : Parcelable