package com.example

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.engine.AlizaLiveStreamEngine
import com.example.engine.FamilyManagementModule
import com.example.engine.MentalFrequencyMonitor
import com.example.engine.TerraManoOSPrimeLauncher
import com.example.engine.VidakManoHomeIntegration
import com.example.engine.VidakTabadalaEngine
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    createNotificationChannel(this)
    setContent {
      MyApplicationTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
          TerraManoInterface(modifier = Modifier.padding(innerPadding))
        }
      }
    }
  }

  private fun createNotificationChannel(context: Context) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
          val name = "TerraManoNotifications"
          val descriptionText = "Terra Mano OS System Notifications"
          val importance = NotificationManager.IMPORTANCE_DEFAULT
          val channel = NotificationChannel("TERRA_MANO_CHANNEL", name, importance).apply {
              description = descriptionText
          }
          val notificationManager: NotificationManager =
              context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
          notificationManager.createNotificationChannel(channel)
      }
  }
}

@Composable
fun TerraManoInterface(modifier: Modifier = Modifier) {
  var status by remember { mutableStateOf("BOOTING_SYSTEM...") }
  var mentalFreq by remember { mutableStateOf(0.0) }
  val context = LocalContext.current
  val engine = remember { VidakTabadalaEngine() }
  val launcher = remember { TerraManoOSPrimeLauncher(engine) }
  val alizaEngine = remember { AlizaLiveStreamEngine() }
  val homeIntegration = remember { VidakManoHomeIntegration() }
  val familyModule = remember { FamilyManagementModule() }
  val frequencyMonitor = remember { MentalFrequencyMonitor() }

  LaunchedEffect(Unit) {
      val success = launcher.powerOnSequence()
      status = if (success) "ONLINE_OPTIMIZED" else "BOOT_FAILED"
      if (success) {
          showNotification(context)
      }
  }

  LaunchedEffect(Unit) {
      frequencyMonitor.frequencyFlow.collect { freq ->
          mentalFreq = freq
      }
  }

  Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
      Text(text = "Terra Mano OS State: $status", style = MaterialTheme.typography.headlineMedium)
      Text(text = "Mentális Frekvencia: %.2f Hz".format(mentalFreq), style = MaterialTheme.typography.bodyLarge)
      
      Spacer(modifier = Modifier.height(16.dp))
      
      Text(text = "Családi AI Rendszer:", style = MaterialTheme.typography.titleMedium)
      LazyColumn(modifier = Modifier.weight(1f)) {
          items(familyModule.getFamilyTasks()) { task ->
              Text(text = "- $task", style = MaterialTheme.typography.bodyMedium)
          }
      }
  }
}

fun showNotification(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return
        }
    }
    
    val builder = NotificationCompat.Builder(context, "TERRA_MANO_CHANNEL")
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        .setContentTitle("Terra Mano OS")
        .setContentText("System is now ONLINE_OPTIMIZED")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    with(NotificationManagerCompat.from(context)) {
        notify(1, builder.build())
    }
}
