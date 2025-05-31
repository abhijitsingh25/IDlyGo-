package com.iiitdharwad.qrscanner.ui.scanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iiitdharwad.qrscanner.data.ScannerMode
import com.iiitdharwad.qrscanner.data.Student
import com.iiitdharwad.qrscanner.data.database.StudentLog
import com.iiitdharwad.qrscanner.data.repository.StudentLogRepository
import com.iiitdharwad.qrscanner.utils.DateTimeUtils
import com.iiitdharwad.qrscanner.utils.QRCodeParser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ScannerUiState(
    val isScanning: Boolean = false,
    val scannedStudent: Student? = null,
    val scannerMode: ScannerMode = ScannerMode.CHECK_IN,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

class ScannerViewModel(
    private val repository: StudentLogRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScannerUiState())
    val uiState: StateFlow<ScannerUiState> = _uiState.asStateFlow()

    fun startScanning() {
        _uiState.value = _uiState.value.copy(
            isScanning = true,
            errorMessage = null,
            successMessage = null
        )
    }

    fun stopScanning() {
        _uiState.value = _uiState.value.copy(isScanning = false)
    }

    fun onQRCodeDetected(qrContent: String) {
        val student = QRCodeParser.parseQRCode(qrContent)
        if (student != null) {
            _uiState.value = _uiState.value.copy(
                isScanning = false,
                scannedStudent = student,
                errorMessage = null
            )
        } else {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Invalid QR code format"
            )
        }
    }

    fun confirmAction() {
        val currentState = _uiState.value
        val student = currentState.scannedStudent ?: return

        _uiState.value = currentState.copy(isLoading = true)

        viewModelScope.launch {
            try {
                val timestamp = DateTimeUtils.getCurrentTimestamp()
                val log = StudentLog(
                    name = student.name,
                    regNo = student.regNo,
                    timestamp = timestamp,
                    action = currentState.scannerMode.getDisplayName(),
                    formattedTime = DateTimeUtils.formatDateTime(timestamp)
                )

                repository.insertLog(log)

                val successMsg = if (currentState.scannerMode == ScannerMode.CHECK_IN) {
                    "Successfully checked in ${student.name}"
                } else {
                    "Successfully checked out ${student.name}"
                }

                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    scannedStudent = null,
                    successMessage = successMsg
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to save record: ${e.message}"
                )
            }
        }
    }

    fun cancelAction() {
        _uiState.value = _uiState.value.copy(
            scannedStudent = null,
            errorMessage = null
        )
    }

    fun switchMode() {
        val newMode = if (_uiState.value.scannerMode == ScannerMode.CHECK_IN) {
            ScannerMode.CHECK_OUT
        } else {
            ScannerMode.CHECK_IN
        }

        _uiState.value = _uiState.value.copy(
            scannerMode = newMode,
            errorMessage = null,
            successMessage = null
        )
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null,
            successMessage = null
        )
    }
}