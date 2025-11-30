/*package com.example.mypage.Screens

package com.example.composedemo

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.composedemo.ui.theme.SpaceGrotesk


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePageScreen(navController: NavHostController) {
    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "The Page",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = SpaceGrotesk,
                    )
                },
                actions = {
                    IconButton(onClick = { /* Cart action */ }) {
                        Icon(
                            Icons.Outlined.ShoppingCart,
                            contentDescription = "Cart",
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
                .padding(top = 18.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            item {
                FeaturedBanner()
            }

            item {
                CustomSearchBar()
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Discover",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = SpaceGrotesk,
                        color = Color.Black
                    )
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_add),
                            contentDescription = "Add",
                            tint = Color.Black,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }

            items(sampleBooks.chunked(2)) { rowBooks ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowBooks.forEach { book ->
                        Box(modifier = Modifier.weight(1f)) {
                            DiscoverBookCard(book)
                        }
                    }
                    if (rowBooks.size < 2) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
            item {
                ExploreSellersSection()
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun FeaturedBanner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(220.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF1A1A2E))
    ) {
        // Image de fond (optionnel)
        Image(
            painter = painterResource(id = R.drawable.depth),
            contentDescription = "Featured Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 1.0f
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF0F3460).copy(alpha = 0.1f),
                            Color(0xFF1A1A2E).copy(alpha = 0.4f)
                        )
                    )
                )
        )

        Image(
            painter = painterResource(id = R.drawable.leveling),
            contentDescription = "Featured Image",
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp)
                .padding(vertical = 25.dp)
                .width(120.dp)
                .height(170.dp)
                .clip(RoundedCornerShape(16.dp))
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(16.dp),
                    clip = false
                ),
            contentScale = ContentScale.Crop,
            alpha = 1.0f
        )
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Popular Now",
                color = Color.White,
                fontSize = 12.sp,
                fontFamily = SpaceGrotesk,
            )

            Column {
                Text(
                    "SOLO",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = SpaceGrotesk,
                )
                Text(
                    "LEVELING S1",
                    color = Color.White,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = SpaceGrotesk,
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        "$120.99",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = SpaceGrotesk,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            "More Details",
                            color = Color.Black,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = SpaceGrotesk,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CustomSearchBar() {
    var searchText by remember { mutableStateOf("") }

    OutlinedTextField(
        value = searchText,
        onValueChange = { searchText = it },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        placeholder = {
            Text(
                "Search for anything...",
                color = Color.Gray,
                fontFamily = SpaceGrotesk,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )
        },
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "Search",
                tint = Color.Gray
            )
        },
        shape = RoundedCornerShape(29.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color(0xFFF5F5F5),
            focusedContainerColor = Color(0xFFF5F5F5),
            unfocusedBorderColor = Color.Transparent,
            focusedBorderColor = Color.Transparent,
            focusedTextColor = Color.Gray,
            unfocusedTextColor = Color.Gray,
            cursorColor = Color.Gray,
        ),
        singleLine = true,
        textStyle = LocalTextStyle.current.copy(
            fontFamily = SpaceGrotesk
        )
    )
}

data class Book(
    val title: String,
    val author: String,
    val genre: String,
    val price: String,
    val rating: String,
    val imageRes: Int
)

val sampleBooks = listOf(
    Book("Dragon's Quest", "Sarah Johnson", "Fantasy", "$16.99", "4.8/5", R.drawable.dragon),
    Book("Hero Academy", "Volume 15", "Shonen", "$11.99", "4.9/5", R.drawable.hero),
    Book("Water Moon", "Sarah Johnson", "Fantasy", "$16.99", "4.8/5", R.drawable.water),
    Book("Johan Irving", "Volume 15", "Shonen", "$11.99", "4.9/5", R.drawable.john),
    Book("Dragon's Quest", "Sarah Johnson", "Fantasy", "$16.99", "4.8/5", R.drawable.dragon),
    Book("Hero Academy", "Volume 15", "Shonen", "$11.99", "4.9/5", R.drawable.hero),

    )
data class Seller(
    val name: String,
    val description: String,
    val imageRes: Int,
    val isFavorite: Boolean = false
)

val sampleSellers = listOf(
    Seller("ALCHEMISED", "Reads & Realms Bookstore", R.drawable.achemised, false),
    Seller("Alex Manga", "Manga Wonderland", R.drawable.div, true),
    Seller("Book Haven", "Your Fantasy Destination", R.drawable.dragon, false),
)

@Composable
fun DiscoverBookCard(book: Book) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .padding(8.dp)
    ) {
        Image(
            painter = painterResource(id = book.imageRes),
            contentDescription = book.title,
            modifier = Modifier
                .height(160.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = book.genre,
            color = if (book.genre == "Fantasy") Color(0xFFB388EB) else Color(0xFFFFAB91),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = SpaceGrotesk,
            modifier = Modifier
                .background(
                    color = if (book.genre == "Fantasy")
                        Color(0xFFB388EB).copy(alpha = 0.15f)
                    else
                        Color(0xFFFFAB91).copy(alpha = 0.15f),
                    shape = RoundedCornerShape(13.dp)
                )
                .padding(horizontal = 8.dp, vertical = 4.dp)
        )


        Text(
            text = book.title,
            color = Color.Black,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = SpaceGrotesk
        )

        Text(
            text = "by ${book.author}",
            color = Color.Gray,
            fontSize = 12.sp,
            fontFamily = SpaceGrotesk
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "⭐ ${book.rating}",
            color = Color(0xFFFFD700),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = SpaceGrotesk
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = book.price,
                color = Color(0xFF0D9488),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                fontFamily = SpaceGrotesk
            )
            Spacer(modifier = Modifier.width(11.dp))
            Button(
                onClick = { /* Add to cart */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB8860B)),
                shape = RoundedCornerShape(20.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                modifier = Modifier
                    .width(110.dp)
                    .height(36.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = "Add",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = SpaceGrotesk,
                        textAlign = TextAlign.Center
                    )
                }
            }}
    }
}
@Composable
fun ExploreSellersSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        Text(
            text = "Explore sellers",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = SpaceGrotesk,
            color = Color.Black,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            sampleSellers.forEach { seller ->
                SellerCard(seller)
            }
        }
    }
}
@Composable
fun SellerCard(seller: Seller) {
    var isFavorite by remember { mutableStateOf(seller.isFavorite) }

    Box(
        modifier = Modifier
            .width(280.dp)
            .height(180.dp)
            .clip(RoundedCornerShape(16.dp))
    ) {
        // Background Image
        Image(
            painter = painterResource(id = seller.imageRes),
            contentDescription = seller.name,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 1.0f
        )

        // Dark overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.1f),
                            Color.Black.copy(alpha = 0.4f)
                        )
                    )
                )
        )

        // Favorite icon
        IconButton(
            onClick = { isFavorite = !isFavorite },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp)
        ) {
            Icon(
                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "Favorite",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }

        // Seller info
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Text(
                text = seller.name,
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = SpaceGrotesk
            )
            Text(
                text = seller.description,
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 14.sp,
                fontFamily = SpaceGrotesk
            )
        }
    }
}

// Preview
@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun HomePagePreview() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            // Prévisualisation sans NavController
            HomePageScreen(navController = NavHostController(LocalContext.current))
        }
    }
}*/