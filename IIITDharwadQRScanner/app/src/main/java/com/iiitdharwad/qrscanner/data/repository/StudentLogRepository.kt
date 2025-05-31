package com.iiitdharwad.qrscanner.data.repository

import com.iiitdharwad.qrscanner.data.database.StudentLog
import kotlinx.coroutines.flow.Flow

interface StudentLogRepository {
    suspend fun insertLog(log: StudentLog)
    fun getAllLogs(): Flow<List<StudentLog>>
    suspend fun getLogsForStudent(regNo: String): List<StudentLog>
    fun searchLogs(searchQuery: String): Flow<List<StudentLog>>
    suspend fun getTotalCount(): Int
    suspend fun getCheckInCount(): Int
    suspend fun getCheckOutCount(): Int
    suspend fun deleteLog(log: StudentLog)
    suspend fun deleteAllLogs()
    suspend fun getLastLogForStudent(regNo: String): StudentLog?
}