package com.example.mypage.Screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mypage.navigation.Screen
import com.example.mypage.viewmodel.AuthState
import com.example.mypage.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: AuthViewModel = viewModel()
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current

    // Observer l'état d'authentification
    val authState by viewModel.authState.collectAsState()

    // Gérer les changements d'état
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Success -> {
                val message = (authState as AuthState.Success).message
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                viewModel.resetAuthState()
                navController.navigate(Screen.Main.route) {
                    popUpTo(Screen.Welcome.route) { inclusive = true }
                }
            }
            is AuthState.Error -> {
                val errorMessage = (authState as AuthState.Error).message
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                viewModel.resetAuthState()
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Sign up",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
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
                actions = {
                    Spacer(modifier = Modifier.width(48.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
            ) {

                Text(
                    text = "Unlock Your Next\nObsession!",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    lineHeight = 34.sp,
                    modifier = Modifier.padding(top = 24.dp, bottom = 32.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = firstName,
                        onValueChange = { firstName = it },
                        maxLines = 1,
                        label = { Text("First Name", fontSize = 14.sp, color = Color.Gray) },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFFF5F5F5),
                            unfocusedContainerColor = Color(0xFFF5F5F5),
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(8.dp),
                        enabled = authState !is AuthState.Loading
                    )

                    OutlinedTextField(
                        value = lastName,
                        onValueChange = { lastName = it },
                        maxLines = 1,
                        label = { Text("Last Name", fontSize = 14.sp, color = Color.Gray) },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFFF5F5F5),
                            unfocusedContainerColor = Color(0xFFF5F5F5),
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(8.dp),
                        enabled = authState !is AuthState.Loading
                    )
                }

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    maxLines = 1,
                    label = { Text("Email", fontSize = 14.sp, color = Color.Gray) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF5F5F5),
                        unfocusedContainerColor = Color(0xFFF5F5F5),
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(8.dp),
                    enabled = authState !is AuthState.Loading
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    maxLines = 1,
                    label = { Text("Password", fontSize = 14.sp, color = Color.Gray) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        TextButton(onClick = { passwordVisible = !passwordVisible }) {
                            Text(
                                text = if (passwordVisible) "Hide" else "Show",
                                fontSize = 12.sp,
                                color = Color(0xFF007AFF)
                            )
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF5F5F5),
                        unfocusedContainerColor = Color(0xFFF5F5F5),
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(8.dp),
                    enabled = authState !is AuthState.Loading
                )

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    maxLines = 1,
                    label = { Text("Confirm your Password", fontSize = 14.sp, color = Color.Gray) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        TextButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Text(
                                text = if (confirmPasswordVisible) "Hide" else "Show",
                                fontSize = 12.sp,
                                color = Color(0xFF007AFF)
                            )
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF5F5F5),
                        unfocusedContainerColor = Color(0xFFF5F5F5),
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(8.dp),
                    enabled = authState !is AuthState.Loading
                )

                // Bouton Sign Up avec Firebase
                Button(
                    onClick = {
                        when {
                            firstName.isBlank() -> {
                                Toast.makeText(context, "First name is required", Toast.LENGTH_SHORT).show()
                            }
                            lastName.isBlank() -> {
                                Toast.makeText(context, "Last name is required", Toast.LENGTH_SHORT).show()
                            }
                            email.isBlank() -> {
                                Toast.makeText(context, "Email is required", Toast.LENGTH_SHORT).show()
                            }
                            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                                Toast.makeText(context, "Invalid email address", Toast.LENGTH_SHORT).show()
                            }
                            password.isBlank() -> {
                                Toast.makeText(context, "Password is required", Toast.LENGTH_SHORT).show()
                            }
                            password.length < 6 -> {
                                Toast.makeText(context, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                            }
                            password != confirmPassword -> {
                                Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                            }
                            else -> {
                                // Combiner firstName et lastName en username
                                val username = "$firstName $lastName"
                                // Appel au ViewModel pour créer le compte Firebase
                                viewModel.signUp(username, email, password)
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFB8860B)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    enabled = authState !is AuthState.Loading
                ) {
                    if (authState is AuthState.Loading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text(
                            text = "Sign Up",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 24.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "I already have an account, I want to ",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "Sign in",
                        fontSize = 14.sp,
                        color = Color(0xFF007AFF),
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.clickable {
                            navController.navigate(Screen.Login.route)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(200.dp))
            }
        }
    }
}