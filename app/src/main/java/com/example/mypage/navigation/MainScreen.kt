package com.example.mypage.navigation


sealed class MainScreen(val route: String) {
    object Home : MainScreen("home_screen")
    object Cart : MainScreen("cart_screen")
    object Profile : MainScreen("profile_screen")
    object Settings : MainScreen("settings_screen")

}