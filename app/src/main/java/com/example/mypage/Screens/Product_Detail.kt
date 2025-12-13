package com.example.mypage.Screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


import com.example.mypage.navigation.Screen
import com.example.mypage.ui.theme.SpaceGrotesk
import coil.compose.AsyncImage
import com.example.mypage.models.Book
import com.example.mypage.models.getAuthorName
import com.example.mypage.models.getCoverUrl
import com.example.mypage.models.getLikes
import com.example.mypage.models.getPrice
import com.example.mypage.models.getRating
import com.example.mypage.models.getSummary
import com.example.mypage.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    navController: NavController,
    book: Book,
    recommendations: List<Book>,
    cartViewModel: CartViewModel
) {
    val context = LocalContext.current
    var isExpanded by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Book Details",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = SpaceGrotesk,
                        color = Color.Black,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                },
                actions = { Spacer(modifier = Modifier.width(48.dp)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .background(Color.White)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = book.title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 28.sp,
                color = Color.Black
            )
            Text(
                text = "by ${book.getAuthorName()}",
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = SpaceGrotesk,
                color = Color.Gray,
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // API Book Cover
                AsyncImage(
                    model = book.getCoverUrl(),
                    contentDescription = "Book Cover",
                    modifier = Modifier
                        .width(190.dp)
                        .height(255.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(22.dp))

                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(top = 4.dp)
                ) {
                    Button(
                        onClick = {
                            cartViewModel.addToCart(book.id)
                            Toast.makeText(
                                context,
                                "✓ ${book.title} added to cart",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFCFA32B)),
                        modifier = Modifier
                            .width(140.dp)
                            .height(40.dp),
                        shape = RoundedCornerShape(6.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                    ) {
                        Text(
                            "Add to Cart",
                            fontWeight = FontWeight.Medium,
                            fontSize = 15.sp,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(18.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Downloads",
                            tint = Color.Red,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "${book.getLikes()}",
                            fontSize = 15.sp,
                            fontFamily = SpaceGrotesk,
                            modifier = Modifier.padding(start = 4.dp),
                            color = Color(0xFF000000)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "★",
                            color = Color(0xFFF8D01F),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${book.getRating()}",
                            fontWeight = FontWeight.Bold,
                            fontFamily = SpaceGrotesk,
                            fontSize = 15.sp,
                            modifier = Modifier.padding(start = 6.dp),
                            color = Color(0xFF000000)
                        )
                    }
                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "$${book.getPrice()}",
                        fontFamily = SpaceGrotesk,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF000000)
                    )
                    Text(
                        text = "PRICE",
                        fontFamily = SpaceGrotesk,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "About This Book",
                fontFamily = SpaceGrotesk,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 4.dp),
                color = Color.Black
            )

            // Summary with Read More/Read Less
            val fullSummary = book.getSummary()
            val summaryPreview = if (fullSummary.length > 250) {
                fullSummary.substring(0, 250) + "..."
            } else {
                fullSummary
            }

            Text(
                text = if (isExpanded) fullSummary else summaryPreview,
                fontSize = 15.sp,
                color = Color(0xFF555555),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Read More / Read Less button
            if (fullSummary.length > 250) {
                Text(
                    text = if (isExpanded) "Read Less" else "Read More",
                    fontSize = 14.sp,
                    fontFamily = SpaceGrotesk,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0D9488),
                    modifier = Modifier
                        .clickable { isExpanded = !isExpanded }
                        .padding(bottom = 12.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            if (recommendations.isNotEmpty()) {
                Text(
                    text = "More Books",
                    fontFamily = SpaceGrotesk,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(6.dp))

                LazyRow(
                    contentPadding = PaddingValues(bottom = 8.dp)
                ) {
                    items(recommendations) { book ->
                        ApiBookRecommendationItem(
                            book = book,
                            onClick = {
                                navController.navigate(
                                    Screen.ProductDetail.passBookId(book.id)
                                )
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun ApiBookRecommendationItem(book: Book, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .width(180.dp)
            .height(255.dp)
            .padding(end = 12.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .background(Color.White)
                .height(255.dp)
        ) {
            AsyncImage(
                model = book.getCoverUrl(),
                contentDescription = book.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(255.dp)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}