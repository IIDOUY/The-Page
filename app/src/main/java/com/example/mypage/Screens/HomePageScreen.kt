package com.example.mypage.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.mypage.Data.*
import com.example.mypage.navigation.Screen
import com.example.mypage.R
import com.example.mypage.ui.theme.SpaceGrotesk
import coil.compose.AsyncImage
import com.example.mypage.models.Book
import com.example.mypage.models.getAuthorName
import com.example.mypage.models.getCoverUrl
import com.example.mypage.models.getPrice
import com.example.mypage.models.getRating

// Category options for filtering
val BOOK_CATEGORIES = listOf(
    "All Books",
    "Science Fiction",
    "Fantasy",
    "Mystery",
    "Romance",
    "Children",
    "History",
    "Poetry",
    "Drama",
    "Cooking"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePageScreen(
    parentNavController: NavHostController,
    allBooks: List<Book>,
    navController: NavHostController
) {
    var searchText by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All Books") }
    var showCategoryMenu by remember { mutableStateOf(false) }

    // Filter books based on search and category
    val filteredBooks = remember(searchText, selectedCategory, allBooks) {
        var filtered = allBooks

        // Filter by category (topic)
        if (selectedCategory != "All Books") {
            filtered = filtered.filter { book ->
                book.subjects.any { subject ->
                    subject.contains(selectedCategory, ignoreCase = true)
                }
            }
        }

        // Filter by search text
        if (searchText.isNotBlank()) {
            filtered = filtered.filter { book ->
                book.title.contains(searchText, ignoreCase = true) ||
                        book.getAuthorName().contains(searchText, ignoreCase = true) ||
                        book.subjects.any { it.contains(searchText, ignoreCase = true) }
            }
        }

        filtered
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "The Page",
                        fontSize = 20.sp,
                        fontFamily = SpaceGrotesk,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.offset(y = (-12).dp)
                    )
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
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Featured Banner - from first API book
            if (allBooks.isNotEmpty()) {
                item {
                    ApiFeaturedBanner(book = allBooks.first())
                }
            }

            // Search Bar with Filter Button
            item {
                SearchBarWithFilter(
                    searchText = searchText,
                    onSearchChange = { searchText = it },
                    selectedCategory = selectedCategory,
                    showCategoryMenu = showCategoryMenu,
                    onShowCategoryMenuChange = { showCategoryMenu = it },
                    onCategorySelect = { category ->
                        selectedCategory = category
                        showCategoryMenu = false
                    }
                )
            }

            // Discover Section Header
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
                        fontFamily = SpaceGrotesk,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    IconButton(onClick = {
                        parentNavController.navigate(Screen.AllProducts.route)
                    }) {
                        Icon(
                            imageVector = Icons.Filled.GridView,
                            contentDescription = "show all",
                            tint = Color.Black,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // Show filtered books or empty message
            if (filteredBooks.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No books found",
                            fontSize = 16.sp,
                            fontFamily = SpaceGrotesk,
                            color = Color.Gray,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            } else {
                // API Books Grid - 2 items per row - NOW USES filteredBooks!
                items(filteredBooks.chunked(2)) { rowBooks ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        rowBooks.forEach { book ->
                            Box(
                                modifier = Modifier.weight(1f)
                            ) {
                                ApiDiscoverBookCard(
                                    book = book,
                                    coverModifier = Modifier
                                        .height(200.dp)
                                        .fillMaxWidth(),
                                    titleFontSize = 12.sp,
                                    authorFontSize = 10.sp
                                ) {
                                    parentNavController.navigate(
                                        Screen.ProductDetail.passBookId(book.id)
                                    )
                                }
                            }
                        }
                        if (rowBooks.size < 2) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            // Explore Sellers Section
            item {
                ExploreSellersSection()
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

// Search Bar with Filter Button
@Composable
fun SearchBarWithFilter(
    searchText: String,
    onSearchChange: (String) -> Unit,
    selectedCategory: String,
    showCategoryMenu: Boolean,
    onShowCategoryMenuChange: (Boolean) -> Unit,
    onCategorySelect: (String) -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Search TextField
            OutlinedTextField(
                value = searchText,
                onValueChange = onSearchChange,
                modifier = Modifier
                    .weight(1f)
                    .height(54.dp),
                placeholder = {
                    Text(
                        "Search books...",
                        color = Color.Gray,
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
                textStyle = LocalTextStyle.current.copy()
            )

            // Filter Button
            Box {
                IconButton(
                    onClick = { onShowCategoryMenuChange(!showCategoryMenu) },
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFFF5F5F5))
                        .size(54.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = "Filter",
                        tint = Color.Black
                    )
                }

                // Dropdown Menu
                DropdownMenu(
                    expanded = showCategoryMenu,
                    onDismissRequest = { onShowCategoryMenuChange(false) },
                    modifier = Modifier
                        .width(180.dp)
                        .background(Color.White)
                ) {
                    BOOK_CATEGORIES.forEach { category ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = category,
                                    fontSize = 14.sp,
                                    fontFamily = SpaceGrotesk,
                                    color = if (selectedCategory == category) Color(0xFF0D9488) else Color.Black,
                                    fontWeight = if (selectedCategory == category) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            onClick = {
                                onCategorySelect(category)
                            }
                        )
                    }
                }
            }
        }

        // Show active filter badge
        if (selectedCategory != "All Books") {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Surface(
                    color = Color(0xFF0D9488).copy(alpha = 0.1f),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = selectedCategory,
                            fontSize = 12.sp,
                            fontFamily = SpaceGrotesk,
                            color = Color(0xFF0D9488),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

// API-based Featured Banner
@Composable
fun ApiFeaturedBanner(book: Book) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(220.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF1A1A2E))
    ) {
        // Blurred background image
        AsyncImage(
            model = book.getCoverUrl(),
            contentDescription = "Featured Background",
            modifier = Modifier
                .fillMaxSize()
                .blur(20.dp),
            contentScale = ContentScale.Crop
        )

        // Gradient overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF0F3460).copy(alpha = 0.3f),
                            Color(0xFF1A1A2E).copy(alpha = 0.6f)
                        )
                    )
                )
        )

        // Sharp foreground image
        AsyncImage(
            model = book.getCoverUrl(),
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
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Featured",
                fontFamily = SpaceGrotesk,
                color = Color.White,
                fontSize = 12.sp,
            )

            Column {
                Text(
                    book.title.take(25),
                    color = Color.White,
                    fontFamily = SpaceGrotesk,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    "by ${book.getAuthorName()}",
                    color = Color.White.copy(alpha = 0.8f),
                    fontFamily = SpaceGrotesk,
                    fontSize = 12.sp,
                    maxLines = 1
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        "$${book.getPrice()}",
                        fontFamily = SpaceGrotesk,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
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
                            fontFamily = SpaceGrotesk,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }
        }
    }
}

// API-based Book Card
@Composable
fun ApiDiscoverBookCard(
    book: Book,
    coverModifier: Modifier = Modifier,
    titleFontSize: TextUnit = 14.sp,
    authorFontSize: TextUnit = 12.sp,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White)
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        // API Book Cover Image
        AsyncImage(
            model = book.getCoverUrl(),
            contentDescription = book.title,
            modifier = Modifier
                .height(160.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Genre/Subject
        if (book.subjects.isNotEmpty()) {
            Text(
                text = book.subjects.first().take(15),
                color = Color(0xFF4600FF),
                fontSize = 11.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                softWrap = false,
            )
        }

        // Title
        Text(
            text = book.title,
            color = Color.Black,
            fontSize = titleFontSize,
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            softWrap = true,
        )

        // Author
        Text(
            text = "by ${book.getAuthorName()}",
            color = Color.Gray,
            fontSize = authorFontSize,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            softWrap = false
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Rating
        Text(
            text = "⭐ ${book.getRating()}",
            color = Color(0xFFFFD700),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Price and Add Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$${book.getPrice()}",
                color = Color(0xFF0D9488),
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = { /* Add to cart */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB8860B)),
                shape = RoundedCornerShape(20.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                modifier = Modifier.height(32.dp)
            ) {
                Text(
                    text = "Add",
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

data class Seller(
    val name: String,
    val description: String,
    val imageRes: Int,
    val isFavorite: Boolean = false
)

val sampleSellers = listOf(
    Seller("ALCHEMISED", "Reads & Realms", R.drawable.cover1, false),
    Seller("Alex Manga", "Manga Wonderland", R.drawable.cover2, true),
    Seller("Book Haven", "Fantasy Destination", R.drawable.cover3, false),
)

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
        androidx.compose.foundation.Image(
            painter = painterResource(id = seller.imageRes),
            contentDescription = seller.name,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

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
            )
            Text(
                text = seller.description,
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 14.sp,
            )
        }
    }
}