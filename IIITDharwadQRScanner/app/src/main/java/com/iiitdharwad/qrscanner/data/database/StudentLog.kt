package com.iiitdharwad.qrscanner.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(
    tableName = "student_logs",
    indices = [Index(value = ["regNo"]), Index(value = ["timestamp"])]
)
data class StudentLog(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val regNo: String,
    val timestamp: Long,
    val action: String, // "Check In" or "Check Out"
    val formattedTime: String
)