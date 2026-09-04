package com.mastermovie.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.MediaItem
import androidx.compose.ui.platform.setContent
import androidx.compose.material.*
import androidx.compose.runtime.*
import android.content.Context
import com.mastermovie.db.AppDatabase
import com.mastermovie.db.HistoryItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class PlayerActivity : ComponentActivity() {
    private var player: SimpleExoPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Params: mediaUrl, mediaId, title
        val mediaUrl = intent.getStringExtra("mediaUrl") ?: ""
        val mediaId = intent.getStringExtra("mediaId") ?: ""
        val title = intent.getStringExtra("title") ?: ""
        setContent {
            PlayerScreen(mediaUrl = mediaUrl, mediaId = mediaId, title = title, context = this)
        }
    }

    override fun onStop() {
        super.onStop()
        player?.release()
        player = null
    }
}

@Composable
fun PlayerScreen(mediaUrl:String, mediaId:String, title:String, context: Context) {
    var isPlaying by remember { mutableStateOf(false) }
    val prefs = context.getSharedPreferences("mm_prefs", Context.MODE_PRIVATE)
    var quality by remember { mutableStateOf(prefs.getString("quality","low") ?: "low") }
    Column {
        Text(title, style = MaterialTheme.typography.h6)
        // Simple buttons to simulate playback - in real app embed PlayerView
        Row {
            Button(onClick = {
                // Start playback using ExoPlayer - abbreviated here
                // Save history with lastPosition 0 for start
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val db = AppDatabase.getInstance(context)
                        db.mediaDao().upsertHistory(HistoryItem(mediaId, title, "", 0, 0, System.currentTimeMillis()))
                    } catch (e: Exception) {}
                }
                isPlaying = true
            }) { Text("Reproducir ($quality)") }
            Spacer(Modifier.width(8.dp))
            Button(onClick = {
                // Toggle quality and save
                quality = when(quality) { "low"->"medium"; "medium"->"high"; else->"low" }
                prefs.edit().putString("quality", quality).apply()
            }) { Text("Cambiar calidad") }
        }
    }
}
