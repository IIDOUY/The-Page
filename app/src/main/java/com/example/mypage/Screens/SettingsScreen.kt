package com.example.mypage.Screens
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mypage.ui.theme.SpaceGrotesk

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    var notificationsEnabled by remember { mutableStateOf(true) }
    var darkModeEnabled by remember { mutableStateOf(false) }
    var autoDownloadEnabled by remember { mutableStateOf(false) }
    var newsletterEnabled by remember { mutableStateOf(true) }


    val backgroundColor = if (darkModeEnabled) Color(0xFF121212) else Color.White
    val textColor = if (darkModeEnabled) Color.White else Color.Black
    val cardColor = if (darkModeEnabled) Color(0xFF1E1E1E) else Color.White
    val subtitleColor = if (darkModeEnabled) Color.LightGray else Color.Gray

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shadowElevation = 4.dp,
                color = if (darkModeEnabled) Color(0xFF1E1E1E) else Color.White
            ) {
                TopAppBar(
                    title = {
                        Text(
                            text = "Settings",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = SpaceGrotesk,
                            color = textColor
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = if (darkModeEnabled) Color(0xFF1E1E1E) else Color.White
                    )
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(16.dp)) }


            item {
                SectionHeader(title = "General", isDarkMode = darkModeEnabled)
            }

            item {
                SettingsItem(
                    icon = Icons.Outlined.Language,
                    title = "Language",
                    subtitle = "English",
                    onClick = { },
                    isDarkMode = darkModeEnabled
                )
            }

            item {
                SettingsItem(
                    icon = Icons.Outlined.Public,
                    title = "Country/Region",
                    subtitle = "United States",
                    onClick = { },
                    isDarkMode = darkModeEnabled
                )
            }

            item {
                SettingsItem(
                    icon = Icons.Outlined.AttachMoney,
                    title = "Currency",
                    subtitle = "USD ($)",
                    onClick = { },
                    isDarkMode = darkModeEnabled
                )
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }


            item {
                SectionHeader(title = "Notifications", isDarkMode = darkModeEnabled)
            }

            item {
                SettingsSwitchItem(
                    icon = Icons.Outlined.Notifications,
                    title = "Push Notifications",
                    subtitle = "Get updates on orders and new releases",
                    checked = notificationsEnabled,
                    onCheckedChange = { notificationsEnabled = it },
                    isDarkMode = darkModeEnabled
                )
            }

            item {
                SettingsSwitchItem(
                    icon = Icons.Outlined.Email,
                    title = "Newsletter",
                    subtitle = "Receive weekly book recommendations",
                    checked = newsletterEnabled,
                    onCheckedChange = { newsletterEnabled = it },
                    isDarkMode = darkModeEnabled
                )
            }

            item {
                SettingsItem(
                    icon = Icons.Outlined.Tune,
                    title = "Notification Preferences",
                    subtitle = "Customize what you receive",
                    onClick = { },
                    isDarkMode = darkModeEnabled
                )
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }


            item {
                SectionHeader(title = "Appearance", isDarkMode = darkModeEnabled)
            }

            item {
                SettingsSwitchItem(
                    icon = Icons.Outlined.DarkMode,
                    title = "Dark Mode",
                    subtitle = "Enable dark theme",
                    checked = darkModeEnabled,
                    onCheckedChange = { darkModeEnabled = it },
                    isDarkMode = darkModeEnabled
                )
            }

            item {
                SettingsItem(
                    icon = Icons.Outlined.Palette,
                    title = "Theme Color",
                    subtitle = "Gold",
                    onClick = { },
                    isDarkMode = darkModeEnabled
                )
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }


            item {
                SectionHeader(title = "Reading Preferences", isDarkMode = darkModeEnabled)
            }

            item {
                SettingsSwitchItem(
                    icon = Icons.Outlined.Download,
                    title = "Auto-Download",
                    subtitle = "Download purchased books automatically",
                    checked = autoDownloadEnabled,
                    onCheckedChange = { autoDownloadEnabled = it },
                    isDarkMode = darkModeEnabled
                )
            }

            item {
                SettingsItem(
                    icon = Icons.Outlined.Bookmark,
                    title = "Reading History",
                    subtitle = "View your reading activity",
                    onClick = { },
                    isDarkMode = darkModeEnabled
                )
            }

            item {
                SettingsItem(
                    icon = Icons.Outlined.Category,
                    title = "Favorite Genres",
                    subtitle = "Manage your preferences",
                    onClick = { },
                    isDarkMode = darkModeEnabled
                )
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }


            item {
                SectionHeader(title = "Privacy & Security", isDarkMode = darkModeEnabled)
            }

            item {
                SettingsItem(
                    icon = Icons.Outlined.Security,
                    title = "Privacy Policy",
                    subtitle = "Read our privacy terms",
                    onClick = { },
                    isDarkMode = darkModeEnabled
                )
            }

            item {
                SettingsItem(
                    icon = Icons.Outlined.Lock,
                    title = "Change Password",
                    subtitle = "Update your security credentials",
                    onClick = { },
                    isDarkMode = darkModeEnabled
                )
            }

            item {
                SettingsItem(
                    icon = Icons.Outlined.VerifiedUser,
                    title = "Two-Factor Authentication",
                    subtitle = "Add extra security to your account",
                    onClick = { },
                    isDarkMode = darkModeEnabled
                )
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }


            item {
                SectionHeader(title = "Support", isDarkMode = darkModeEnabled)
            }

            item {
                SettingsItem(
                    icon = Icons.Outlined.Help,
                    title = "Help Center",
                    subtitle = "Get answers to your questions",
                    onClick = { },
                    isDarkMode = darkModeEnabled
                )
            }

            item {
                SettingsItem(
                    icon = Icons.Outlined.Mail,
                    title = "Contact Us",
                    subtitle = "support@thepage.com",
                    onClick = { },
                    isDarkMode = darkModeEnabled
                )
            }

            item {
                SettingsItem(
                    icon = Icons.Outlined.Star,
                    title = "Rate Us",
                    subtitle = "Share your feedback",
                    onClick = { },
                    isDarkMode = darkModeEnabled
                )
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }


            item {
                SectionHeader(title = "About", isDarkMode = darkModeEnabled)
            }

            item {
                SettingsItem(
                    icon = Icons.Outlined.Info,
                    title = "Terms of Service",
                    subtitle = "Read our terms and conditions",
                    onClick = { },
                    isDarkMode = darkModeEnabled
                )
            }

            item {
                SettingsItem(
                    icon = Icons.Outlined.Article,
                    title = "Licenses",
                    subtitle = "Open source licenses",
                    onClick = { },
                    isDarkMode = darkModeEnabled
                )
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "The Page",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = SpaceGrotesk,
                            color = if (darkModeEnabled) Color.White else Color.Black
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Version 1.0.0",
                            fontSize = 13.sp,
                            fontFamily = SpaceGrotesk,
                            color = if (darkModeEnabled) Color.LightGray else Color.Gray
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}

@Composable
fun SectionHeader(title: String, isDarkMode: Boolean = false) {
    Text(
        text = title,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = SpaceGrotesk,
        color = Color(0xFFB8860B),
        modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp)
    )
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    isDarkMode: Boolean = false
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDarkMode) Color(0xFF1E1E1E) else Color.White
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
                    color = if (isDarkMode) Color.White else Color.Black
                )
                Text(
                    text = subtitle,
                    fontSize = 13.sp,
                    fontFamily = SpaceGrotesk,
                    color = if (isDarkMode) Color.LightGray else Color.Gray
                )
            }

            Icon(
                Icons.Outlined.ChevronRight,
                contentDescription = "Navigate",
                tint = if (isDarkMode) Color.LightGray else Color.Gray,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun SettingsSwitchItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    isDarkMode: Boolean = false
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDarkMode) Color(0xFF1E1E1E) else Color.White
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
                    color = if (isDarkMode) Color.White else Color.Black
                )
                Text(
                    text = subtitle,
                    fontSize = 13.sp,
                    fontFamily = SpaceGrotesk,
                    color = if (isDarkMode) Color.LightGray else Color.Gray
                )
            }

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color(0xFFB8860B),
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = Color.LightGray
                )
            )
        }
    }
}