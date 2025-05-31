package com.iiitdharwad.qrscanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.iiitdharwad.qrscanner.data.database.AppDatabase
import com.iiitdharwad.qrscanner.data.repository.StudentLogRepository
import com.iiitdharwad.qrscanner.data.repository.StudentLogRepositoryImpl
import com.iiitdharwad.qrscanner.ui.AppBottomBar
import com.iiitdharwad.qrscanner.ui.AppNavigation
import com.iiitdharwad.qrscanner.ui.IIITDharwadQRScannerTheme

class MainActivity : ComponentActivity() {

    private lateinit var repository: StudentLogRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize repository
        val database = AppDatabase.getDatabase(this)
        repository = StudentLogRepositoryImpl(database.studentLogDao())

        setContent {
            IIITDharwadQRScannerTheme {
                AppScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScreen() {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            AppBottomBar(navController = navController)
        }
    ) { paddingValues ->
        AppNavigation(
            navController = navController,
            modifier = Modifier.padding(paddingValues)
        )
    }
}