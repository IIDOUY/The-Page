package com.example.mypage.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mypage.BottomNavHost
import com.example.mypage.Screens.*
import com.example.mypage.database.AppDatabase
import com.example.mypage.network.GutendexApiService
import com.example.mypage.repository.BookRepository
import com.example.mypage.repository.CartRepository
import com.example.mypage.repository.FavoritesRepository
import com.example.mypage.repository.PurchasedRepository
import com.example.mypage.viewmodel.BooksViewModel
import com.example.mypage.viewmodel.BooksViewModelFactory
import com.example.mypage.viewmodel.CartViewModel
import com.example.mypage.viewmodel.CartViewModelFactory
import com.example.mypage.viewmodel.FavoritesViewModel
import com.example.mypage.viewmodel.FavoritesViewModelFactory
import com.example.mypage.viewmodel.PurchasedViewModel
import com.example.mypage.viewmodel.PurchasedViewModelFactory

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun NavigationStack() {
    val navController = rememberNavController()

    // API + BooksViewModel
    val apiService = GutendexApiService.create()
    val repository = BookRepository(apiService)
    val booksViewModel: BooksViewModel = viewModel(
        factory = BooksViewModelFactory(repository)
    )
    val uiState by booksViewModel.uiState.collectAsState()
    val allBooks = uiState.books

    // DB
    val context = LocalContext.current
    val db = remember { AppDatabase.getDatabase(context) }



    val purchasedRepository = remember { PurchasedRepository(db.purchasedBookDao()) }
    val purchasedViewModel: PurchasedViewModel = viewModel(
        factory = PurchasedViewModelFactory(purchasedRepository)
    )

    // Favoris
    val favoritesRepository = remember { FavoritesRepository(db.favoriteBookDao()) }
    val favoritesViewModel: FavoritesViewModel = viewModel(
        factory = FavoritesViewModelFactory(favoritesRepository)
    )

    // Panier
    val cartRepository = remember { CartRepository(db.cartItemDao()) }
    val cartViewModel: CartViewModel = viewModel(
        factory = CartViewModelFactory(cartRepository)
    )

    // Les livres sont prêts → on peut afficher toute la navigation normale
    NavHost(navController = navController, startDestination = Screen.Welcome.route) {

        composable(Screen.Welcome.route) {
            welcome_page(navController = navController)
        }
        composable(Screen.Purchased.route) {
            PurchasedScreen(
                navController = navController,
                allBooks = allBooks,
                purchasedViewModel = purchasedViewModel
            )
        }


        composable(Screen.Favorites.route) {
            val favoriteIds by favoritesViewModel.favoriteIds.collectAsState()
            val favoriteBooks = allBooks.filter { favoriteIds.contains(it.id) }

            FavoritesScreen(
                navController = navController,
                favoriteBooks = favoriteBooks
            )
        }

        composable(Screen.AllProducts.route) {
            AllProductsScreen(allBooks = allBooks, navController = navController)
        }

        composable(Screen.Cart.route) {
            CartScreen(
                navController = navController,
                cartViewModel = cartViewModel,
                purchasedViewModel = purchasedViewModel,
                allBooks = allBooks
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(navController = navController)
        }

        composable(Screen.Create.route) {
            SignUpScreen(navController = navController)
        }

        composable(Screen.SignUp.route) {
            SignUpScreen(navController = navController)
        }

        composable(Screen.Main.route) {
            BottomNavHost(
                parentNavController = navController,
                allBooks = allBooks,
                favoritesViewModel = favoritesViewModel,
                purchasedViewModel = purchasedViewModel,
                cartViewModel = cartViewModel
            )
        }

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
                    recommendations = allBooks.filter { it.id != selectedBook.id }.take(5),
                    cartViewModel = cartViewModel
                )
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Book not found")
                }
            }
        }

        composable(
            route = Screen.BookReader.route,
            arguments = listOf(navArgument("bookId") { type = NavType.IntType })
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getInt("bookId") ?: 0
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
