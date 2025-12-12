package com.example.mypage.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mypage.Data.CartManager
import com.example.mypage.models.Book
import com.example.mypage.models.getCoverUrl
import com.example.mypage.navigation.Screen
import com.example.mypage.ui.theme.SpaceGrotesk
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    // Get purchased books from CartManager (in real app, use database)
    val purchasedBooks = remember { CartManager.purchasedBooks }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shadowElevation = 4.dp,
                color = Color.White
            ) {
                TopAppBar(
                    title = {
                        Text(
                            text = "Profile",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = SpaceGrotesk,
                            color = Color.Black
                        )
                    },
                    actions = {
                        IconButton(onClick = { /* Edit profile */ }) {
                            Icon(
                                Icons.Outlined.Edit,
                                contentDescription = "Edit Profile",
                                tint = Color.Black
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White
                    )
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFFB8860B).copy(alpha = 0.1f),
                                    Color.White
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(
                                            Color(0xFFB8860B),
                                            Color(0xFFDAA520)
                                        )
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Filled.Person,
                                contentDescription = "Profile",
                                tint = Color.White,
                                modifier = Modifier.size(50.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Ayaou Khalid",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = SpaceGrotesk,
                            color = Color.Black
                        )

                        Text(
                            text = "khalidayaou147@email.com",
                            fontSize = 14.sp,
                            fontFamily = SpaceGrotesk,
                            color = Color.Gray
                        )
                    }
                }
            }


            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatCard(
                        value = purchasedBooks.size.toString(),
                        label = "Books Read",
                        icon = Icons.Filled.MenuBook
                    )
                    StatCard(
                        value = "8",
                        label = "Wishlist",
                        icon = Icons.Filled.Favorite
                    )
                    StatCard(
                        value = "156",
                        label = "Points",
                        icon = Icons.Filled.Star
                    )
                }
            }

            // My Books Section
            if (purchasedBooks.isNotEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 16.dp)
                    ) {
                        Text(
                            text = "My Books",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = SpaceGrotesk,
                            color = Color.Black,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                    }
                }

                items(purchasedBooks) { book ->
                    MyBookCard(
                        book = book,
                        onRead = {
                            navController.navigate(
                                Screen.BookReader.passBookId(book.id)
                            )
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "Account Settings",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = SpaceGrotesk,
                        color = Color.Black,
                        modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp)
                    )

                    ProfileMenuItem(
                        icon = Icons.Outlined.Person,
                        title = "Personal Information",
                        subtitle = "Update your details",
                        onClick = { }
                    )

                    ProfileMenuItem(
                        icon = Icons.Outlined.CreditCard,
                        title = "Payment Methods",
                        subtitle = "Manage your cards",
                        onClick = { }
                    )

                    ProfileMenuItem(
                        icon = Icons.Outlined.LocationOn,
                        title = "Delivery Address",
                        subtitle = "Manage shipping addresses",
                        onClick = { }
                    )

                    ProfileMenuItem(
                        icon = Icons.Outlined.Notifications,
                        title = "Notifications",
                        subtitle = "Preferences and alerts",
                        onClick = { }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Preferences",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = SpaceGrotesk,
                        color = Color.Black,
                        modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp)
                    )

                    ProfileMenuItem(
                        icon = Icons.Outlined.Language,
                        title = "Language",
                        subtitle = "English",
                        onClick = { }
                    )

                    ProfileMenuItem(
                        icon = Icons.Outlined.Palette,
                        title = "Theme",
                        subtitle = "Light mode",
                        onClick = { }
                    )

                    ProfileMenuItem(
                        icon = Icons.Outlined.Info,
                        title = "About",
                        subtitle = "Version 1.0.0",
                        onClick = { }
                    )

                    Spacer(modifier = Modifier.height(16.dp))


                    Button(
                        onClick = { /* Logout */ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFF3B30)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .padding(horizontal = 8.dp),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 2.dp
                        )
                    ) {
                        Icon(
                            Icons.Outlined.Logout,
                            contentDescription = "Logout",
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Logout",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = SpaceGrotesk,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
fun MyBookCard(
    book: Book,
    onRead: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Book Cover
            AsyncImage(
                model = book.getCoverUrl(),
                contentDescription = book.title,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = book.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = SpaceGrotesk,
                    color = Color.Black,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onRead,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0D9488)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Text(
                        "Read Now",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Icon(
                Icons.Filled.ChevronRight,
                contentDescription = "Open",
                tint = Color.Gray,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun StatCard(
    value: String,
    label: String,
    icon: ImageVector
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF5F5F5)
        ),
        modifier = Modifier.size(width = 100.dp, height = 100.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                icon,
                contentDescription = label,
                tint = Color(0xFFB8860B),
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = SpaceGrotesk,
                color = Color.Black
            )
            Text(
                text = label,
                fontSize = 11.sp,
                fontFamily = SpaceGrotesk,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFB8860B).copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = title,
                    tint = Color(0xFFB8860B),
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = SpaceGrotesk,
                    color = Color.Black
                )
                Text(
                    text = subtitle,
                    fontSize = 13.sp,
                    fontFamily = SpaceGrotesk,
                    color = Color.Gray
                )
            }

            Icon(
                Icons.Filled.ChevronRight,
                contentDescription = "Navigate",
                tint = Color.Gray,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}