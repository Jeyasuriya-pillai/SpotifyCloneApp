package com.example.spotify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.spotify.navigation.NavGraph
import com.example.spotify.navigation.Screen
import com.example.spotify.ui.theme.* // Isse SpotifyGreen, SpotifyDarkGray etc. mil jayenge

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpotifyCloneTheme {
                SpotifyApp()
            }
        }
    }
}

@Composable
fun SpotifyApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomNavItems = listOf(
        Triple(Screen.Home.route, Icons.Default.Home, "Home"),
        Triple(Screen.Search.route, Icons.Default.Search, "Search"),
        Triple(Screen.Library.route, Icons.Default.LibraryMusic, "Library"),
    )

    Scaffold(
        bottomBar = {
            if (currentRoute != Screen.Player.route) {
                NavigationBar(containerColor = SpotifyDarkGray) {
                    bottomNavItems.forEach { (route, icon, label) ->
                        NavigationBarItem(
                            selected = currentRoute == route,
                            onClick = {
                                navController.navigate(route) {
                                    popUpTo(Screen.Home.route) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(icon, contentDescription = label) },
                            label = { Text(label) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = SpotifyGreen,
                                selectedTextColor = SpotifyGreen,
                                unselectedIconColor = SpotifyLightGray,
                                unselectedTextColor = SpotifyLightGray,
                                indicatorColor = SpotifyDarkGray
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)) {
            NavGraph(navController = navController)
        }
    }
}