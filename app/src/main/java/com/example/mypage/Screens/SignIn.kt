package com.example.mypage.Screens

import android.os.Build
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mypage.R
import com.example.mypage.navigation.Screen
import com.example.mypage.ui.theme.SpaceGrotesk
import com.example.mypage.viewmodel.AuthState
import com.example.mypage.viewmodel.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: AuthViewModel = viewModel()
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var passwordVisible by remember { mutableStateOf(false) }

    val webClientId = "99457157016-cbf7qcsdpqb3oei6cqkp6kag76jh4omo.apps.googleusercontent.com"
    val authState by viewModel.authState.collectAsState()




    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val idToken = account.idToken
            if (idToken != null) {
                viewModel.firebaseAuthWithGoogle(idToken)
            } else {
                Toast.makeText(context, "Google Sign-In failed: No ID token", Toast.LENGTH_SHORT).show()
            }
        } catch (e: ApiException) {
            Toast.makeText(context, "Google Sign-In failed: ${e.statusCode}", Toast.LENGTH_SHORT).show()
            val idToken = ""
            Log.d("GoogleSignIn", "ID Token: $idToken")

        }
    }



    LaunchedEffect(authState) {

        when (authState) {
            is AuthState.Success -> {
                Toast.makeText(context, (authState as AuthState.Success).message, Toast.LENGTH_SHORT).show()
                viewModel.resetAuthState()
                navController.navigate(Screen.Main.route) { popUpTo(Screen.Welcome.route) { inclusive = true } }
            }
            is AuthState.Error -> {
                Toast.makeText(context, (authState as AuthState.Error).message, Toast.LENGTH_SHORT).show()
                viewModel.resetAuthState()
            }
            else -> {}
        }
    }


    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.Black)
                    }
                },
                title = {
                    Text(
                        "Sign in", fontSize = 20.sp, fontWeight = FontWeight.Bold,
                        color = Color.Black, modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                actions = { Spacer(modifier = Modifier.width(48.dp)) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                "Welcome back\n\nReader!", fontSize = 35.sp, fontWeight = FontWeight.Normal,
                color = Color.Black, fontFamily = SpaceGrotesk, modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(20.dp))


            // Email TextField
            OutlinedTextField(
                value = email,
                onValueChange = { email = it; emailError = null },
                label = { Text("Email", fontSize = 14.sp, color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = emailError != null,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF5F5F5),
                    unfocusedContainerColor = Color(0xFFF5F5F5),
                    focusedBorderColor = if (emailError != null) Color.Red else Color.Transparent,
                    unfocusedBorderColor = if (emailError != null) Color.Red else Color.Transparent
                ),
                shape = RoundedCornerShape(8.dp),
                enabled = authState !is AuthState.Loading
            )
            if (emailError != null) {
                Text(emailError!!, color = Color.Red, fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.Start).padding(start = 8.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Password TextField
            OutlinedTextField(
                value = password,
                onValueChange = { password = it; passwordError = null },
                label = { Text("Password", fontSize = 14.sp, color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    Text(
                        if (passwordVisible) "Hide" else "Show", fontSize = 14.sp, color = Color(0xFF2196F3),
                        modifier = Modifier.clickable(interactionSource = remember { MutableInteractionSource() },
                            indication = LocalIndication.current) { passwordVisible = !passwordVisible }
                            .padding(end = 8.dp)
                    )
                },
                isError = passwordError != null,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF5F5F5),
                    unfocusedContainerColor = Color(0xFFF5F5F5),
                    focusedBorderColor = if (passwordError != null) Color.Red else Color.Transparent,
                    unfocusedBorderColor = if (passwordError != null) Color.Red else Color.Transparent
                ),
                shape = RoundedCornerShape(8.dp),
                enabled = authState !is AuthState.Loading
            )
            if (passwordError != null) {
                Text(passwordError!!, color = Color.Red, fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.Start).padding(start = 8.dp))
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Sign In Button
            Button(
                onClick = {
                    when {
                        email.isBlank() -> { emailError = "Email is required"; Toast.makeText(context, "Email is required", Toast.LENGTH_SHORT).show() }
                        !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> { emailError = "Invalid email address"; Toast.makeText(context, "Invalid email address", Toast.LENGTH_SHORT).show() }
                        password.isBlank() -> { passwordError = "Password is required"; Toast.makeText(context, "Password is required", Toast.LENGTH_SHORT).show() }
                        password.length < 6 -> { passwordError = "Password must be at least 6 characters"; Toast.makeText(context, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show() }
                        else -> { viewModel.signIn(email, password) }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB8860B)),
                shape = RoundedCornerShape(8.dp),
                enabled = authState !is AuthState.Loading
            ) {
                if (authState is AuthState.Loading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Sign In", color = Color.White, fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // OR Divider
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Divider(modifier = Modifier.weight(1f), color = Color.LightGray)
                Text("OR", modifier = Modifier.padding(horizontal = 16.dp), fontSize = 14.sp, color = Color.Gray)
                Divider(modifier = Modifier.weight(1f), color = Color.LightGray)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Google Sign-In Button
            // Google Sign-In Button
            OutlinedButton(
                onClick = {
                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(webClientId)
                        .requestEmail()
                        .build()
                    val googleSignInClient = GoogleSignIn.getClient(context, gso)

                    // 🔥 AJOUTEZ CETTE PARTIE - Déconnexion avant de lancer le sign-in
                    googleSignInClient.signOut().addOnCompleteListener {
                        googleSignInLauncher.launch(googleSignInClient.signInIntent)
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color.LightGray),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
                enabled = authState !is AuthState.Loading
            ) {
                Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                    Image(painter = painterResource(id = R.drawable.google), contentDescription = "Google Logo", modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Continue with Google", color = Color.Black, fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Sign Up navigation
            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                Text("I don't have an account, I want to ", fontSize = 14.sp, color = Color.Gray)
                Text("create one", fontSize = 14.sp, color = Color(0xFF2196F3),
                    modifier = Modifier.clickable(interactionSource = remember { MutableInteractionSource() },
                        indication = LocalIndication.current) { navController.navigate(Screen.SignUp.route) })
            }

            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}
