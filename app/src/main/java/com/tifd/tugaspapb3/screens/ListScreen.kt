package com.tifd.tugaspapb3.screens

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.google.firebase.firestore.FirebaseFirestore
import com.tifd.tugaspapb3.DataListView
import com.tifd.tugaspapb3.DataModel
import com.tifd.tugaspapb3.ErrorView
import com.tifd.tugaspapb3.LoadingView
import com.tifd.tugaspapb3.loadDataFromFirestore
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ListScreen(navController: NavHostController) {
    val db = FirebaseFirestore.getInstance()
    val context = LocalContext.current
    var dataList by remember { mutableStateOf(listOf<DataModel>()) }
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Mengambil data dari Firestore saat komponen diload
    LaunchedEffect(Unit) {
        loadDataFromFirestore(db, { data ->
            dataList = data
            isLoading = false
        }, {
            isLoading = false
            isError = true
            coroutineScope.launch {
                snackbarHostState.showSnackbar("Gagal memuat data.")
            }
        })
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Jadwal Kuliah") },

            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },

        ) {
        if (isLoading) {
            LoadingView()
        } else if (isError) {
            ErrorView()
        } else {
            DataListView(dataList)
        }
    }
}