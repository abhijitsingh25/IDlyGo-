package com.iiitdharwad.qrscanner.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface StudentLogDao {

    @Insert
    suspend fun insertLog(log: StudentLog)

    @Query("SELECT * FROM student_logs ORDER BY timestamp DESC")
    fun getAllLogs(): Flow<List<StudentLog>>

    @Query("SELECT * FROM student_logs WHERE regNo = :regNo ORDER BY timestamp DESC")
    suspend fun getLogsForStudent(regNo: String): List<StudentLog>

    @Query("SELECT * FROM student_logs WHERE name LIKE '%' || :searchQuery || '%' OR regNo LIKE '%' || :searchQuery || '%' ORDER BY timestamp DESC")
    fun searchLogs(searchQuery: String): Flow<List<StudentLog>>

    @Query("SELECT COUNT(*) FROM student_logs")
    suspend fun getTotalCount(): Int

    @Query("SELECT COUNT(*) FROM student_logs WHERE action = 'Check In'")
    suspend fun getCheckInCount(): Int

    @Query("SELECT COUNT(*) FROM student_logs WHERE action = 'Check Out'")
    suspend fun getCheckOutCount(): Int

    @Delete
    suspend fun deleteLog(log: StudentLog)

    @Query("DELETE FROM student_logs")
    suspend fun deleteAllLogs()

    @Query("SELECT * FROM student_logs WHERE regNo = :regNo ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLastLogForStudent(regNo: String): StudentLog?
}