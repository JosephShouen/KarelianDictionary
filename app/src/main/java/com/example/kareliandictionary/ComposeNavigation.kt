package com.example.kareliandictionary

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


@Composable
fun NavGraph (navController: NavHostController, prefs: SharedPreferences){
    val firstStart = prefs.getBoolean("firstStart", true)
    NavHost(
        navController = navController,
        startDestination = if (firstStart) Screens.Start.route else Screens.Search.route)
    {
        composable(route = "start_screen"){
            StartScreen(prefs, navController)
        }
        composable(route = "search_screen"){
            SearchScreen(prefs)
        }
    }
}
