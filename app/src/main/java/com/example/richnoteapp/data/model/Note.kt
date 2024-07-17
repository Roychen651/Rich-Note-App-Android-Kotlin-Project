package com.example.richnoteapp.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "content")
    var content: String,

    @ColumnInfo(name = "image")
    var photo: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
