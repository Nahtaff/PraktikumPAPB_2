package com.tifd.tugaspapb3.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.tifd.tugaspapb3.RetrofitClient
import com.tifd.tugaspapb3.UserGithub
import kotlinx.coroutines.launch

@Composable
fun GithubProfileScreen(
    username: String,
    navController: NavHostController,

    ) {
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
