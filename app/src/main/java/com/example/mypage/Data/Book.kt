package com.example.mypage.Data

import com.example.mypage.R

data class Book(
    val id: Int,
    val title: String,
    val genre: String,
    val author: String,
    val description: String,
    val price: Double,
    val likes: Int,
    val rating: Double,
    val imageRes: Int
)

fun getSampleBooks(): List<Book> = listOf(


        Book(
            id = 0,
            title = "Water Moon: A Novel",
            author = "Samantha Sotto Yambao",
            genre = "Fantasy",
            description = "Fans of Toshikazu Kawaguchi’s Before the Coffee Gets Cold series...",
            price = 16.99,
            likes = 1568,
            rating = 4.8,
            imageRes = R.drawable.cover2
        ),
        Book(
            id = 1,
            title = "The Academy",
            author = "Elin Hilderbrand",
            genre = "Romance",
            description = "An epic tale that blends fantasy and reality. A rare edition for serious collectors.",
            price = 18.50,
            likes = 980,
            rating = 4.6,
            imageRes = R.drawable.cover4
        ),
        Book(
            id = 2,
            title = "Queen Esther",
            author = "John Irving",
            genre = "Adventure",
            description = "The second adventure in the queen's rise to power...",
            price = 21.00,
            likes = 1350,
            rating = 4.7,
            imageRes = R.drawable.cover6
        ),
        Book(
            id = 3,
            title = "Attack on Titan",
            author = "6 Omnibus",
            genre = "Manga",
            description = "A manga omnibus with thrilling battles...",
            price = 24.99,
            likes = 1870,
            rating = 4.5,
            imageRes = R.drawable.cover3
        )
    )



