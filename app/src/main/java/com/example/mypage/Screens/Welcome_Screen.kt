package com.example.mypage.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mypage.viewmodel.AuthViewModel
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mypage.R
import com.example.mypage.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun welcome_page(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: AuthViewModel = viewModel()
) {

    val images = listOf(
        R.drawable.cover1,
        R.drawable.cover2,
        R.drawable.cover3,
        R.drawable.cover4,
        R.drawable.cover5,
        R.drawable.cover6,
        R.drawable.cover7,
        R.drawable.cover8,
        R.drawable.cover9,
        R.drawable.cover1,
        R.drawable.cover2,
        R.drawable.cover3,
        R.drawable.cover4,
        R.drawable.cover5,
        R.drawable.cover6
    )

    val isUserLoggedIn = viewModel.isUserLoggedIn()
    Scaffold(
        containerColor = Color(0xFFF5F5F0)
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                // CORRECTION 2: Utilise la taille réelle de la liste pour déterminer le nombre d'éléments.
                items(images.size) { index ->
                    // CORRECTION 3: Passe l'ID de l'image directement.
                    GridItem(imageId = images[index])
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color(0xEEFFFFFF),
                                Color(0xFFFFFFFF)
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(
                        start = 24.dp,
                        end = 24.dp,
                        top = 24.dp,
                        bottom = 0.dp
                    ),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Where Fandom Finds\nThe Page.",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 36.sp,
                    color = Color(0xFF000000)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "The only marketplace built for readers and collectors. " +
                            "Get instant access to new releases, " +
                            "rare editions, graphic novels, and the latest manga volumes.",
                    fontSize = 14.sp,
                    lineHeight = 22.sp,
                    color = Color(0xFF666666)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Conditional button display based on login state
                if (isUserLoggedIn) {
                    // Show "Get Started" and "Sign Out" for logged-in users
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = { navController.navigate(Screen.Main.route) },
                            modifier = Modifier
                                .weight(0.9f)
                                .height(48.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFB8860B)
                            )
                        ) {
                            Text(
                                "Get Started",
                                color = Color(0xFFFFFFFF)
                            )
                        }

                        Button(
                            onClick = {
                                viewModel.signOut()
                                navController.navigate(Screen.Welcome.route) {
                                    popUpTo(Screen.Welcome.route) { inclusive = true }
                                }
                            },
                            modifier = Modifier
                                .weight(0.6f)
                                .height(48.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFB8860B)
                            )
                        ) {
                            Text(
                                "Sign Out",
                                color = Color(0xFFFFFFFF)
                            )
                        }
                    }
                } else {
                    // Show "Sign In" and "Sign Up" for non-logged-in users
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = { navController.navigate(Screen.Login.route) },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFB8860B)
                            )
                        ) {
                            Text(
                                "Sign In",
                                color = Color(0xFFFFFFFF)
                            )
                        }

                        Button(
                            onClick = { navController.navigate(Screen.SignUp.route) },
                            modifier = Modifier
                                .weight(0.5f)
                                .height(48.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFB8860B)
                            )
                        ) {
                            Text(
                                "Sign Up",
                                color = Color(0xFFFFFFFF)
                            )
                        }
                    }
                }
            }
        }
    }
}

// CORRECTION 4: La fonction GridItem est maintenant plus simple et plus sûre.
@Composable
fun GridItem(imageId: Int) {
    Image(
        painter = painterResource(id = imageId),
        contentDescription = "Book cover",
        modifier = Modifier
            .aspectRatio(0.7f)
            .clip(RoundedCornerShape(8.dp)),
        contentScale = ContentScale.Crop
    )
}
