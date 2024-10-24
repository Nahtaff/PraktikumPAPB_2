package com.tifd.tugaspapb3

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import com.tifd.tugaspapb3.ui.theme.TugasPAPB3Theme
import kotlinx.coroutines.launch

class ListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TugasPAPB3Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DataListScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DataListScreen() {
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
                actions = {
                    IconButton(onClick = {
                        // Navigasi ke GithubProfile saat tombol GitHub ditekan
                        val intent = Intent(context, GithubProfile::class.java)
                        context.startActivity(intent)
                    }) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle, // Icon GitHub
                            contentDescription = "Lihat Profil GitHub"
                        )
                    }
                }
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

@Composable
fun LoadingView() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
fun ErrorView() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Error loading data",  style = MaterialTheme.typography.bodyMedium)
    }
}





@Composable
fun DataListView(dataList: List<DataModel>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(dataList) { data ->
            DataCard(data)
        }
    }
}

fun loadDataFromFirestore(
    db: FirebaseFirestore,
    onSuccess: (List<DataModel>) -> Unit,
    onFailure: () -> Unit
) {
    db.collection("jadwal-kuliah")
        .get()
        .addOnSuccessListener { result ->
            val items = result.documents.mapNotNull { document ->
                try {
                    DataModel(
                        mata_kuliah = document.getString("mata_kuliah") ?: "-",
                        hari = Hari.safeValueOf(document.getString("hari")),
                        jam_mulai = document.getString("jam_mulai") ?: "-",
                        jam_selesai = document.getString("jam_selesai") ?: "-",
                        ruang = document.getString("ruang") ?: "-"
                    )
                } catch (e: Exception) {
                    null // Mengatasi error konversi
                }
            }
            onSuccess(items.sortedWith(
                compareBy<DataModel> { it.hari.urutan }
                    .thenBy { it.jam_mulai }
            ))
        }
        .addOnFailureListener {
            onFailure()
        }
}

@Composable
fun DataCard(data: DataModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Mata Kuliah: ${data.mata_kuliah}", style = MaterialTheme.typography.bodyMedium)
            Text("Hari: ${data.hari.name}", style = MaterialTheme.typography.bodyMedium)
            Text("Jam: ${data.jam_mulai} - ${data.jam_selesai}", style = MaterialTheme.typography.bodyMedium)
            Text("Ruang: ${data.ruang}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

data class DataModel(
    val mata_kuliah: String,
    val hari: Hari,
    val jam_mulai: String,
    val jam_selesai: String,
    val ruang: String
)

enum class Hari(val urutan: Int) {
    SENIN(1),
    SELASA(2),
    RABU(3),
    KAMIS(4),
    JUMAT(5),
    SABTU(6),
    MINGGU(7);

    companion object {
        fun safeValueOf(value: String?): Hari {
            return values().find { it.name.equals(value, ignoreCase = true) } ?: SENIN
        }
    }
}
