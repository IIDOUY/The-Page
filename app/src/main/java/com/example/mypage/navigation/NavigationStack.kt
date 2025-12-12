package com.example.mypage.navigation


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mypage.BottomNavHost
import com.example.mypage.Screens.*
import com.example.mypage.network.GutendexApiService
import com.example.mypage.repository.BookRepository
import com.example.mypage.viewmodel.BooksViewModel
import com.example.mypage.viewmodel.BooksViewModelFactory

@Composable
fun NavigationStack() {
    val navController = rememberNavController()

    // Initialize API, Repository, and ViewModel
    val apiService = GutendexApiService.create()
    val repository = BookRepository(apiService)
    val booksViewModel: BooksViewModel = viewModel(
        factory = BooksViewModelFactory(repository)
    )

    // Collect UI State
    val uiState = booksViewModel.uiState.collectAsState()
    val allBooks = uiState.value.books

    NavHost(navController = navController, startDestination = Screen.Welcome.route) {
        // Welcome screen
        composable(Screen.Welcome.route) {
            welcome_page(navController = navController)
        }

        // All Products - with API data
        composable(Screen.AllProducts.route) {
            if (uiState.value.isLoading) {
                // Show loading
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (uiState.value.error != null) {
                // Show error
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Error: ${uiState.value.error}")
                }
            } else {
                AllProductsScreen(allBooks = allBooks, navController = navController)
            }
        }

        // Cart
        composable(Screen.Cart.route) {
            CartScreen(navController = navController)
        }

        // Login
        composable(Screen.Login.route) {
            LoginScreen(navController = navController)
        }

        // SignUp
        composable(Screen.Create.route) {
            SignUpScreen(navController = navController)
        }

        composable(Screen.SignUp.route) {
            SignUpScreen(navController = navController)
        }

        // Main/Home with bottom nav
        composable(Screen.Main.route) {
            BottomNavHost(parentNavController = navController, allBooks = allBooks)
        }

        // Product Detail - with API data
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
            val selectedBook = allBooks.find { it.id == bookId }

            if (selectedBook != null) {
                ProductDetailScreen(
                    navController = navController,
                    book = selectedBook,
                    recommendations = allBooks.filter { it.id != selectedBook.id }.take(5)
                )
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Book not found")
                }
            }
        }
        //Reader
        composable(
            route = Screen.BookReader.route,
            arguments = listOf(navArgument("bookId") { type = NavType.IntType })
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getInt("bookId") ?: 0
            // Get the book from your data source
            val book = allBooks.firstOrNull { it.id == bookId }
            if (book != null) {
                BookReaderScreen(
                    navController = navController,
                    book = book
                )
            }
        }
    }
}