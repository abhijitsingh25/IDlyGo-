package com.iiitdharwad.qrscanner.utils

import java.text.SimpleDateFormat
import java.util.*

object DateTimeUtils {

    private val dateTimeFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

    /**
     * Gets current timestamp in milliseconds
     */
    fun getCurrentTimestamp(): Long {
        return System.currentTimeMillis()
    }

    /**
     * Formats timestamp to readable date-time string
     */
    fun formatDateTime(timestamp: Long): String {
        return dateTimeFormat.format(Date(timestamp))
    }

    /**
     * Formats timestamp to readable date string
     */
    fun formatDate(timestamp: Long): String {
        return dateFormat.format(Date(timestamp))
    }

    /**
     * Formats timestamp to readable time string
     */
    fun formatTime(timestamp: Long): String {
        return timeFormat.format(Date(timestamp))
    }

    /**
     * Gets current formatted date-time string
     */
    fun getCurrentFormattedDateTime(): String {
        return formatDateTime(getCurrentTimestamp())
    }

    /**
     * Checks if timestamp is today
     */
    fun isToday(timestamp: Long): Boolean {
        val today = Calendar.getInstance()
        val date = Calendar.getInstance().apply { timeInMillis = timestamp }

        return today.get(Calendar.YEAR) == date.get(Calendar.YEAR) &&
               today.get(Calendar.DAY_OF_YEAR) == date.get(Calendar.DAY_OF_YEAR)
    }
}