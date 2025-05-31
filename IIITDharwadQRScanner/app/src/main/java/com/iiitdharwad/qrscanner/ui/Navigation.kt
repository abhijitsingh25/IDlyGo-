package com.iiitdharwad.qrscanner.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.iiitdharwad.qrscanner.R
import com.iiitdharwad.qrscanner.ui.history.HistoryScreen
import com.iiitdharwad.qrscanner.ui.scanner.ScannerScreen

sealed class Screen(
    val route: String,
    val title: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    object Scanner : Screen(
        route = "scanner",
        title = R.string.nav_scanner,
        selectedIcon = Icons.Filled.QrCodeScanner,
        unselectedIcon = Icons.Outlined.QrCodeScanner
    )

    object History : Screen(
        route = "history",
        title = R.string.nav_history,
        selectedIcon = Icons.Filled.History,
        unselectedIcon = Icons.Outlined.History
    )
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Scanner.route,
        modifier = modifier
    ) {
        composable(Screen.Scanner.route) {
            ScannerScreen()
        }
        composable(Screen.History.route) {
            HistoryScreen()
        }
    }
}

@Composable
fun AppBottomBar(navController: NavHostController) {
    val screens = listOf(
        Screen.Scanner,
        Screen.History
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        screens.forEach { screen ->
            val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (selected) screen.selectedIcon else screen.unselectedIcon,
                        contentDescription = stringResource(id = screen.title)
                    )
                },
                label = { Text(stringResource(id = screen.title)) },
                selected = selected,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}