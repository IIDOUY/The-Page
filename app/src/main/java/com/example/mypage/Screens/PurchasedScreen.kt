package com.example.mypage.Screens

// Screens/PurchasedScreen.kt


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.mypage.models.Book
import com.example.mypage.models.getAuthorName
import com.example.mypage.models.getCoverUrl
import com.example.mypage.navigation.Screen
import com.example.mypage.ui.theme.SpaceGrotesk
import com.example.mypage.viewmodel.PurchasedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PurchasedScreen(
    navController: NavController,
    allBooks: List<Book>,
    purchasedViewModel: PurchasedViewModel
) {
    val purchasedEntities by purchasedViewModel.purchasedBooks.collectAsState()

    // map bookId -> Book
    val purchasedBooks = remember(purchasedEntities, allBooks) {
        val ids = purchasedEntities.map { it.bookId }.toSet()
        allBooks.filter { ids.contains(it.id) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Books Read",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = SpaceGrotesk
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        if (purchasedBooks.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No books read yet",
                    fontSize = 16.sp,
                    fontFamily = SpaceGrotesk,
                    color = Color.Gray
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color.White),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(purchasedBooks) { book ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(
                                    Screen.BookReader.passBookId(book.id)
                                )
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = book.getCoverUrl(),
                            contentDescription = book.title,
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = book.title,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = SpaceGrotesk
                            )
                            Text(
                                text = "by ${book.getAuthorName()}",
                                fontSize = 12.sp,
                                fontFamily = SpaceGrotesk,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}
