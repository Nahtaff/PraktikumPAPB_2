package com.tifd.tugaspapb3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.tifd.tugaspapb3.UserGithub

class GithubProfile : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Panggil ProfileScreen dengan username yang ingin dicari
            ProfileScreen(username = "Nahtaff") // Ganti dengan username yang sesuai
        }
    }
}



@Composable
fun ProfileScreen(username: String) {
    var userProfile by remember { mutableStateOf<UserGithub?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                userProfile = RetrofitClient.api.getUserProfile(username)
            } catch (e: Exception) {
                isError = true
            }
            isLoading = false
        }
    }

    if (isLoading) {
        // Loading View
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (isError) {
        // Error View
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Error loading profile.")
        }
    } else {
        // Display User Profile
        userProfile?.let { profile ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = rememberAsyncImagePainter(profile.avatar_url),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .clip(CircleShape).border(2.dp, Color.Gray, CircleShape)
                        .size(150.dp)

                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Username: ${profile.login}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Name: ${profile.name ?: "N/A"}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Followers: ${profile.followers}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Following: ${profile.following}", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

