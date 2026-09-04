package com.mastermovie.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import android.content.SharedPreferences
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    private val prefs by lazy { getSharedPreferences("mm_prefs", Context.MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Prevent instant exit: override back press to show confirmation dialog
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Show confirmation
                showExitConfirm()
            }
        })

        setContent {
            MainScreen()
        }
    }

    private fun showExitConfirm() {
        // Use an Activity-level dialog: simple Toast fallback here; Compose shows real dialog
        // We'll show Compose dialog inside MainScreen using state saved in prefs
        // For now, broadcast via prefs flag
        prefs.edit().putBoolean("ask_exit", true).apply()
    }
}

@Composable
fun MainScreen() {
    val ctx = LocalContext.current
    val prefs = ctx.getSharedPreferences("mm_prefs", Context.MODE_PRIVATE)
    var selectedTab by rememberSaveable { mutableStateOf(prefs.getInt("last_tab", 0)) }
    val scope = rememberCoroutineScope()
    var showExitDialog by remember { mutableStateOf(false) }

    // Listen to ask_exit flag
    LaunchedEffect(Unit) {
        while (true) {
            if (prefs.getBoolean("ask_exit", false)) {
                prefs.edit().putBoolean("ask_exit", false).apply()
                showExitDialog = true
            }
            delay(500)
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Master Movie") }) },
        bottomBar = {
            BottomNavigation {
                BottomNavigationItem(selected = selectedTab==0, onClick={ selectedTab=0; prefs.edit().putInt("last_tab",0).apply() }, icon={}, label={Text("Películas")})
                BottomNavigationItem(selected = selectedTab==1, onClick={ selectedTab=1; prefs.edit().putInt("last_tab",1).apply() }, icon={}, label={Text("Series")})
                BottomNavigationItem(selected = selectedTab==2, onClick={ selectedTab=2; prefs.edit().putInt("last_tab",2).apply() }, icon={}, label={Text("Favoritos")})
                BottomNavigationItem(selected = selectedTab==3, onClick={ selectedTab=3; prefs.edit().putInt("last_tab",3).apply() }, icon={}, label={Text("Historial")})
                BottomNavigationItem(selected = selectedTab==4, onClick={ selectedTab=4; prefs.edit().putInt("last_tab",4).apply() }, icon={}, label={Text("Apoya")})
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when(selectedTab) {
                0 -> Text("Películas (página cargada en lotes de 100) - implementar lista")
                1 -> Text("Series")
                2 -> Text("Favoritos")
                3 -> Text("Historial")
                4 -> SupportScreen()
            }
        }
    }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("Confirmar salida") },
            text = { Text("¿Estás seguro que quieres salir de la app?") },
            confirmButton = {
                TextButton(onClick = {
                    (ctx as? ComponentActivity)?.finish()
                }) { Text("Salir") }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
fun SupportScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Text("Apoya al creador", style = MaterialTheme.typography.h6)
        Spacer(Modifier.height(8.dp))
        Text("Si quieres apoyar el proyecto puedes donar por PayPal:")
        Spacer(Modifier.height(8.dp))
        Text("https://paypal.me/WalterAntunez2012", color = MaterialTheme.colors.primary)
    }
}
