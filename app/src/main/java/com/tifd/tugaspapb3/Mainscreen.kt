package com.tifd.tugaspapb3

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.tifd.tugaspapb3.bottomnavigationbar
import com.tifd.tugaspapb3.navbar.botnavbar
import com.tifd.tugaspapb3.ui.theme.TugasPAPB3Theme

class MainScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TugasPAPB3Theme {
                MainScreen()
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { bottomnavigationbar(navController = navController) }
    ) {
        botnavbar(navController = navController)
    }
}

