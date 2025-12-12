package com.example.mypage.navigation

sealed class Screen(val route: String) {
    object AllProducts : Screen("all_products")
    object Cart: Screen("Cart")
    object Create : Screen("Create")
    object Login : Screen("login")
    object Welcome : Screen("welcome_screen")
    object SignUp : Screen("signup_screen")
    object Main : Screen("main_screen")
    object ProductDetail : Screen("product_detail_screen?bookId={bookId}") {
        fun passBookId(bookId: Int): String = "product_detail_screen?bookId=$bookId"

    }
    object BookReader : Screen("book_reader/{bookId}") {
        fun passBookId(bookId: Int) = "book_reader/$bookId"
    }
}

