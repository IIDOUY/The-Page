package com.example.mypage

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mypage.Screens.BookReaderScreen
import com.example.mypage.Screens.CartScreen
import com.example.mypage.Screens.HomePageScreen
import com.example.mypage.Screens.ProfileScreen
import com.example.mypage.Screens.SettingsScreen
import com.example.mypage.components.BottomNavigationBar
import com.example.mypage.models.Book
import com.example.mypage.navigation.MainScreen
import com.example.mypage.navigation.NavigationStack
import com.example.mypage.navigation.Screen
import com.example.mypage.ui.theme.MYPAGETheme
import com.example.mypage.viewmodel.CartViewModel
import com.example.mypage.viewmodel.FavoritesViewModel
import com.example.mypage.viewmodel.PurchasedViewModel

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()

        setContent {
            MYPAGETheme {
                Surface(color = Color.White) {
                    NavigationStack()
                }
            }
        }
    }
    private companion object {
        private const val BOOKS_CHANNEL_ID = "books_updates_channel"
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Books updates"
            val descriptionText = "Notifications about new books and recommendations"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(BOOKS_CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavHost(
    parentNavController: NavHostController,
    allBooks: List<Book>,
    favoritesViewModel: FavoritesViewModel,
    purchasedViewModel: PurchasedViewModel,
    cartViewModel: CartViewModel
) {
    // NavController utilisé par la bottom bar ET le NavHost
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding()
                )
        ) {
            NavHost(
                navController = navController,
                startDestination = MainScreen.Home.route,
                modifier = Modifier.fillMaxSize()
            ) {
                composable(MainScreen.Home.route) {
                    HomePageScreen(
                        parentNavController = parentNavController,
                        allBooks = allBooks,
                        navController = navController,
                        favoritesViewModel = favoritesViewModel
                    )
                }

                composable(MainScreen.Cart.route) {
                    CartScreen(
                        navController = parentNavController,
                        cartViewModel = cartViewModel,
                        purchasedViewModel = purchasedViewModel,
                        allBooks = allBooks
                    )
                }

                composable(MainScreen.Profile.route) {
                    ProfileScreen(
                        navController = parentNavController,
                        purchasedViewModel = purchasedViewModel,
                        favoritesViewModel = favoritesViewModel
                    )
                }

                composable(MainScreen.Settings.route) {
                    SettingsScreen()
                }

                composable(
                    route = Screen.BookReader.route,
                    arguments = listOf(
                        navArgument("bookId") { type = NavType.IntType }
                    )
                ) { backStackEntry ->
                    val bookId = backStackEntry.arguments?.getInt("bookId") ?: 0
                    val book = allBooks.firstOrNull { it.id == bookId }
                    if (book != null) {
                        BookReaderScreen(
                            navController = parentNavController,
                            book = book
                        )
                    }
                }
            }
        }
    }
}
