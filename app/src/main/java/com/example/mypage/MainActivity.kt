package com.example.mypage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mypage.Screens.CartScreen
import com.example.mypage.Screens.ProfileScreen

import com.example.mypage.ui.theme.MYPAGETheme
import com.example.mypage.navigation.MainScreen
import com.example.mypage.navigation.NavigationStack
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mypage.Data.Book
import com.example.mypage.Screens.HomePageScreen
import com.example.mypage.Screens.SettingsScreen

import com.example.mypage.components.BottomNavigationBar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MYPAGETheme {
                NavigationStack()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavHost(parentNavController: NavHostController, allBooks: List<Book>) {
    val navController = rememberNavController()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF))
    ) {

        NavHost(
            navController = navController,
            startDestination = MainScreen.Home.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 56.dp)
        ) {
            composable(MainScreen.Home.route) {
                HomePageScreen(
                    parentNavController = parentNavController,
                    allBooks = allBooks,
                    navController = navController
                )
            }
            composable(MainScreen.Cart.route) {
                CartScreen(
                    navController = parentNavController,
                  
                )
            }
            composable(MainScreen.Profile.route) {
                ProfileScreen()
            }
            composable(MainScreen.Settings.route) {
                SettingsScreen()
            }

        }


        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            BottomNavigationBar(navController)
        }
    }
}

