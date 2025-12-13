package com.example.mypage.Screens

import com.example.mypage.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

import com.example.mypage.navigation.Screen
import com.example.mypage.ui.theme.SpaceGrotesk
import coil.compose.AsyncImage

import com.example.mypage.models.Book
import com.example.mypage.models.getCoverUrl
import com.example.mypage.models.getPrice
import com.example.mypage.viewmodel.CartViewModel
import com.example.mypage.viewmodel.PurchasedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavController,
    cartViewModel: CartViewModel,
    purchasedViewModel: PurchasedViewModel,
    allBooks: List<Book>
) {
    var showDialog by remember { mutableStateOf(false) }

    val cartItems by cartViewModel.cartItems.collectAsState()

    // Join CartItemEntity (bookId, quantity) avec Book
    val cartBooks = cartItems.mapNotNull { item ->
        val book = allBooks.firstOrNull { it.id == item.bookId }
        book?.let { Pair(it, item.quantity) }
    }

    // Total basé sur la quantité et le prix
    val total = cartBooks.sumOf { (book, qty) ->
        book.getPrice() * qty
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            Surface(color = Color.White) {
                TopAppBar(
                    title = {
                        Text(
                            text = "My Cart",
                            fontWeight = FontWeight.Bold,
                            fontFamily = SpaceGrotesk,
                            fontSize = 20.sp,
                            modifier = Modifier.fillMaxWidth(),
                            color = Color.Black,
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White
                    )
                )
            }
        },
    ) { paddingValues ->

        if (cartBooks.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("🛒", fontSize = 64.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Your Cart Is Empty",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = SpaceGrotesk,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Add Some Incredible Books to start",
                        fontSize = 14.sp,
                        fontFamily = SpaceGrotesk,
                        color = Color.LightGray
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(paddingValues)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 20.dp,
                        bottom = 20.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Text(
                            text = "Unlock Your Next\nObsession!",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = SpaceGrotesk,
                            lineHeight = 28.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    items(cartBooks) { (book, quantity) ->
                        CartBookRow(
                            book = book,
                            quantity = quantity,
                            onClick = {
                                navController.navigate(
                                    Screen.ProductDetail.passBookId(book.id)
                                )
                            },
                            onRemove = {
                                cartViewModel.removeFromCart(book.id)   // ✅ Room + VM
                            }
                        )
                    }
                }

                // Bottom area with total and checkout button
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Total",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = SpaceGrotesk
                        )
                        Text(
                            text = "${"%.2f".format(total)} $",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = SpaceGrotesk,
                            color = Color(0xFF0D9488)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { showDialog = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFB8860B)
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        Text(
                            text = "Checkout",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            shape = RoundedCornerShape(20.dp),
            containerColor = Color.White,
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.check),
                    contentDescription = null,
                    tint = Color(0xFF22C55E),
                    modifier = Modifier.size(48.dp)
                )
            },
            title = {
                Text(
                    text = "Order Confirmed!",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Text(
                    text = "Your purchase has been successfully processed.",
                    fontSize = 16.sp,
                    color = Color.DarkGray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false

                        val currentItems = cartItems

                        currentItems.forEach { item ->
                            purchasedViewModel.addPurchased(item.bookId)
                        }
                        cartViewModel.clearCart()       //  vider la table cart_items
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFB8860B)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        "Continue",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            },
        )
    }
}
@Composable
fun CartBookRow(
    book: Book,
    quantity: Int,
    onClick: () -> Unit,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF8FAFC))
            .clickable { onClick() }
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = book.getCoverUrl(),
            contentDescription = book.title,
            modifier = Modifier
                .size(72.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = book.title,
                fontFamily = SpaceGrotesk,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                maxLines = 2
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${"%.2f".format(book.getPrice())} $  x $quantity",
                fontFamily = SpaceGrotesk,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color(0xFF0D9488)
            )
        }

        IconButton(onClick = onRemove) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Remove",
                tint = Color(0xFFEF4444)
            )
        }
    }
}
