package com.iiitdharwad.qrscanner.data

enum class ScannerMode {
    CHECK_IN,
    CHECK_OUT;

    fun getDisplayName(): String {
        return when (this) {
            CHECK_IN -> "Check In"
            CHECK_OUT -> "Check Out"
        }
    }
}