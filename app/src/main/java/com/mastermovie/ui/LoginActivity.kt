package com.mastermovie.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.mastermovie.data.ApiClient
import com.mastermovie.data.LoginRequest
import kotlinx.coroutines.launch
import android.widget.Toast
import android.content.Context
import android.content.SharedPreferences

class LoginActivity : ComponentActivity() {
    private val prefs: SharedPreferences by lazy { getSharedPreferences("mm_prefs", Context.MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginScreen(onLogin = { user, pass ->
                lifecycleScope.launch {
                    performLogin(user, pass)
                }
            })
        }
    }

    private suspend fun performLogin(user: String, pass: String) {
        val api = ApiClient.create()
        val resp = try { api.login(LoginRequest(user, pass)) } catch (e: Exception) { null }
        if (resp != null && resp.isSuccessful) {
            val body = resp.body()
            body?.token?.let { token ->
                prefs.edit().putString("token", token).apply()
                body.expires?.let { prefs.edit().putString("expires", it).apply() }
                // Show banner message once when adding account
                prefs.edit().putBoolean("banner_shown", true).apply()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } ?: run {
                runOnUiThread { Toast.makeText(this, "Login: respuesta inválida", Toast.LENGTH_LONG).show() }
            }
        } else {
            runOnUiThread { Toast.makeText(this, "Error autenticando", Toast.LENGTH_LONG).show() }
        }
    }
}

@Composable
fun LoginScreen(onLogin:(String,String)->Unit) {
    var user by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.Center) {
        Text("APP CREADA POR WALTER, FUNDADOR DEL GRUPO CODIGO MASTER", style = MaterialTheme.typography.subtitle1)
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(value = user, onValueChange = { user = it }, label = { Text("Usuario") })
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = pass, onValueChange = { pass = it }, label = { Text("Contraseña") })
        Spacer(Modifier.height(12.dp))
        Button(onClick = { onLogin(user, pass) }) { Text("Entrar") }
    }
}
