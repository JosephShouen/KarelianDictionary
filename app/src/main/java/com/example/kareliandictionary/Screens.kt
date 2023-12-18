package com.example.kareliandictionary

sealed class Screens(val route: String) {
    object Start: Screens("start_screen")
    object Search: Screens("search_screen")
}

