package com.example.mypage.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mypage.BottomNavHost
import com.example.mypage.Data.getSampleBooks
import com.example.mypage.HomePageScreen.HomePageScreen
import com.example.mypage.Screens.*

@Composable
fun NavigationStack() {
    val navController = rememberNavController()
    val allBooks = getSampleBooks()
    NavHost(navController = navController, startDestination = Screen.Welcome.route) {
        // Welcome screen: navigates to SignUp or Home
        composable(Screen.Welcome.route) {
            welcome_page(navController = navController,)
            // Or: welcome_page(navController) - adapt the name to your implementation!
        }
        composable(Screen.AllProducts.route) {
            AllProductsScreen(allBooks = allBooks, navController = navController)
        }


        composable(Screen.Cart.route) {
            CartScreen(navController = navController,)
        }
       //navigates to Sign In
        composable(Screen.Login.route) {
            LoginScreen(navController = navController,)
        }

        composable(Screen.Create.route) {
            SignUpScreen(navController = navController,)
        }
        // SignUp screen: back to welcome or forward to Home
        composable(Screen.SignUp.route) {
            SignUpScreen(navController = navController,)
        }
        // Main/Home page
        composable(Screen.Main.route) {
            BottomNavHost(parentNavController = navController, allBooks = allBooks)

        }
        // Product detail with bookId
        composable(
            route = "product_detail_screen?bookId={bookId}",
            arguments = listOf(
                navArgument("bookId") {
                    type = NavType.IntType
                    nullable = false
                }
            )
        ) { entry ->
            val bookId = entry.arguments?.getInt("bookId") ?: 0
            val selectedBook = allBooks.find { it.id == bookId } ?: allBooks.first()
            ProductDetailScreen(
                navController = navController,
                book = selectedBook,
                recommendations = allBooks.filter { it.id != selectedBook.id }
            )
        }
    }
}
