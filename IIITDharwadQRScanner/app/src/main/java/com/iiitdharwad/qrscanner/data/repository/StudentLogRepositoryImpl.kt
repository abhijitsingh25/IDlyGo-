package com.iiitdharwad.qrscanner.data.repository

import com.iiitdharwad.qrscanner.data.database.StudentLog
import com.iiitdharwad.qrscanner.data.database.StudentLogDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

class StudentLogRepositoryImpl(
    private val dao: StudentLogDao
) : StudentLogRepository {

    override suspend fun insertLog(log: StudentLog) {
        dao.insertLog(log)
    }

    override fun getAllLogs(): Flow<List<StudentLog>> {
        return dao.getAllLogs()
    }

    override suspend fun getLogsForStudent(regNo: String): List<StudentLog> {
        return dao.getLogsForStudent(regNo)
    }

    override fun searchLogs(searchQuery: String): Flow<List<StudentLog>> {
        return dao.searchLogs(searchQuery)
    }

    override suspend fun getTotalCount(): Int {
        return dao.getTotalCount()
    }

    override suspend fun getCheckInCount(): Int {
        return dao.getCheckInCount()
    }

    override suspend fun getCheckOutCount(): Int {
        return dao.getCheckOutCount()
    }

    override suspend fun deleteLog(log: StudentLog) {
        dao.deleteLog(log)
    }

    override suspend fun deleteAllLogs() {
        dao.deleteAllLogs()
    }

    override suspend fun getLastLogForStudent(regNo: String): StudentLog? {
        return dao.getLastLogForStudent(regNo)
    }
}