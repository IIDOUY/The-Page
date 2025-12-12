package com.example.mypage.models



data class BooksResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Book>
)

data class Book(
    val id: Int,
    val title: String,
    val authors: List<Author>,
    val cover_image: String?,
    val formats: Map<String, String>,
    val download_count: Int,
    val subjects: List<String>,
    val bookshelves: List<String>,
    val languages: List<String>,
    val copyright: Boolean?,
    val summaries: List<String>?,
)

data class Author(
    val name: String,
    val birth_year: Int?,
    val death_year: Int?
)


// Extension functions to convert API Book to UI-friendly properties
fun Book.getAuthorName(): String {
    return authors.firstOrNull()?.name ?: "Unknown Author"
}

fun Book.getCoverUrl(): String {
    return cover_image ?: formats["image/jpeg"] ?: ""
}

fun Book.getDescription(): String {
    return subjects.take(3).joinToString(" • ")
}

fun Book.getPrice(): Double {
    // Fake price based on download count (you can change this logic)
    return when {
        download_count > 10000 -> 12.99
        download_count > 5000 -> 9.99
        download_count > 1000 -> 7.99
        else -> 5.99
    }
}

fun Book.getRating(): Double {
    // Fake rating based on download count
    return when {
        download_count > 10000 -> 4.8
        download_count > 5000 -> 4.5
        download_count > 1000 -> 4.2
        else -> 4.0
    }
}

fun Book.getLikes(): Int {
    return download_count
}


// Add this corrected mapper function to your BookExtensions.kt

fun Book.getSummary(): String {
    return if (!summaries.isNullOrEmpty()) {  // Checks for null OR empty
        val summary = summaries.firstOrNull()
        if (summary != null) {
            if (summary.length > 500) {
                summary.substring(0, 500) + "..."
            } else {
                summary
            }
        } else {
            " error "
        }
    } else {
        " error " // Fallback to subjects description
    }
}