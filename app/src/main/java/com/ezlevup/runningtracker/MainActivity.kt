package com.ezlevup.runningtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ezlevup.runningtracker.presentation.history.HistoryScreen
import com.ezlevup.runningtracker.presentation.home.HomeScreen
import com.ezlevup.runningtracker.ui.theme.RunningTrackerTheme
import com.ezlevup.runningtracker.util.Screen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RunningTrackerTheme {
                val navController = rememberNavController()
                val items =
                        listOf(
                                BottomNavItem("홈", Screen.Home.route, Icons.Default.Home),
                                BottomNavItem("기록", Screen.History.route, Icons.Default.History)
                        )

                Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        bottomBar = {
                            NavigationBar {
                                val navBackStackEntry by
                                        navController.currentBackStackEntryAsState()
                                val currentDestination = navBackStackEntry?.destination
                                items.forEach { item ->
                                    NavigationBarItem(
                                            icon = {
                                                Icon(item.icon, contentDescription = item.title)
                                            },
                                            label = { Text(item.title) },
                                            selected =
                                                    currentDestination?.hierarchy?.any {
                                                        it.route == item.route
                                                    } == true,
                                            onClick = {
                                                navController.navigate(item.route) {
                                                    popUpTo(
                                                            navController.graph
                                                                    .findStartDestination()
                                                                    .id
                                                    ) { saveState = true }
                                                    launchSingleTop = true
                                                    restoreState = true
                                                }
                                            }
                                    )
                                }
                            }
                        }
                ) { innerPadding ->
                    NavHost(
                            navController = navController,
                            startDestination = Screen.Home.route,
                            modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Screen.Home.route) { HomeScreen() }
                        composable(Screen.History.route) { HistoryScreen() }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HomeScreen()
}

data class BottomNavItem(val title: String, val route: String, val icon: ImageVector)
