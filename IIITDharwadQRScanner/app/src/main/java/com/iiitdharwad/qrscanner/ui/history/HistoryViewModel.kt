package com.iiitdharwad.qrscanner.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iiitdharwad.qrscanner.data.database.StudentLog
import com.iiitdharwad.qrscanner.data.repository.StudentLogRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class HistoryStats(
    val totalRecords: Int = 0,
    val checkIns: Int = 0,
    val checkOuts: Int = 0
)

data class HistoryUiState(
    val logs: List<StudentLog> = emptyList(),
    val filteredLogs: List<StudentLog> = emptyList(),
    val searchQuery: String = "",
    val stats: HistoryStats = HistoryStats(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class HistoryViewModel(
    private val repository: StudentLogRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")

    init {
        // Observe all logs
        viewModelScope.launch {
            repository.getAllLogs().collect { logs ->
                _uiState.value = _uiState.value.copy(logs = logs)
                updateFilteredLogs()
                loadStats()
            }
        }

        // Observe search query changes
        viewModelScope.launch {
            _searchQuery
                .debounce(300) // Wait 300ms after user stops typing
                .collect { query ->
                    _uiState.value = _uiState.value.copy(searchQuery = query)
                    updateFilteredLogs()
                }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    private fun updateFilteredLogs() {
        val currentState = _uiState.value
        val filteredLogs = if (currentState.searchQuery.isBlank()) {
            currentState.logs
        } else {
            currentState.logs.filter { log ->
                log.name.contains(currentState.searchQuery, ignoreCase = true) ||
                log.regNo.contains(currentState.searchQuery, ignoreCase = true)
            }
        }

        _uiState.value = currentState.copy(filteredLogs = filteredLogs)
    }

    private fun loadStats() {
        viewModelScope.launch {
            try {
                val totalRecords = repository.getTotalCount()
                val checkIns = repository.getCheckInCount()
                val checkOuts = repository.getCheckOutCount()

                _uiState.value = _uiState.value.copy(
                    stats = HistoryStats(
                        totalRecords = totalRecords,
                        checkIns = checkIns,
                        checkOuts = checkOuts
                    )
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Failed to load statistics: ${e.message}"
                )
            }
        }
    }

    fun deleteLog(log: StudentLog) {
        viewModelScope.launch {
            try {
                repository.deleteLog(log)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Failed to delete record: ${e.message}"
                )
            }
        }
    }

    fun clearAllHistory() {
        viewModelScope.launch {
            try {
                repository.deleteAllLogs()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Failed to clear history: ${e.message}"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}