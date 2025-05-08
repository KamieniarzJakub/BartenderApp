package com.example.bartenderjetpack.ui

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.bartenderjetpack.R
import com.example.bartenderjetpack.model.Drink
import com.example.bartenderjetpack.utils.getPhoneNumberFromUri
import com.example.bartenderjetpack.utils.sendSms
import kotlinx.coroutines.delay

@Composable
fun DrinkDetails(item: Drink) {
    val context = LocalContext.current

    // Launcher do wyboru kontaktu
    val contactLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickContact()
    ) { uri ->
        uri?.let {
            val phone = getPhoneNumberFromUri(context, uri)
            if (phone != null) {
                sendSms(context, phone, "Składniki drinka ${item.name}: ${item.ingredients}")
            } else {
                Toast.makeText(context, "Nie udało się pobrać numeru", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Launcher do żądania uprawnień
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.values.all { it }
        if (allGranted) {
            contactLauncher.launch(null)
        } else {
            Toast.makeText(context, "Potrzebne uprawnienia do SMS i kontaktów", Toast.LENGTH_SHORT).show()
        }
    }

    val hasSmsPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED
    val hasContactPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                if (hasSmsPermission && hasContactPermission) {
                    contactLauncher.launch(null)
                } else {
                    permissionLauncher.launch(arrayOf(
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.READ_CONTACTS
                    ))
                }
            }) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Wyślij SMS")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.icon),
                contentDescription = item.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            Text(
                text = item.name,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
            Text(
                text = "Składniki:\n${item.ingredients}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Sposób przygotowania:\n${item.recipe}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Timer()
        }
    }
}


@Composable
fun Timer() {
    var totalTime by rememberSaveable { mutableStateOf(60) }
    var timeLeft by rememberSaveable { mutableStateOf(totalTime) }
    var isRunning by rememberSaveable { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    // uruchamianie minutnika
    LaunchedEffect(isRunning) {
        if (isRunning) {
            while (isRunning && timeLeft > 0) {
                delay(1000)
                timeLeft--
            }
            isRunning = false
        }
    }

    // główny widok z przewijaniem w pionie
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Minutnik: ${timeLeft}s",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Suwak ustawiania czasu (blokowany jeśli minutnik działa)
        Slider(
            value = totalTime.toFloat(),
            onValueChange = {
                if (!isRunning) {
                    totalTime = it.toInt()
                    timeLeft = totalTime
                }
            },
            valueRange = 1f..300f,
            steps = 29,
            enabled = !isRunning,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )

        Text("Ustaw czas: ${totalTime}s", modifier = Modifier.padding(8.dp))

        // Przycisk Start / Stop / Reset
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(onClick = { isRunning = true }) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Start")
            }
            IconButton(onClick = { isRunning = false }) {
                Icon(Icons.Default.Clear, contentDescription = "Stop")
            }
            IconButton(onClick = {
                isRunning = false
                timeLeft = totalTime
            }) {
                Icon(Icons.Default.Refresh, contentDescription = "Przerwij")
            }
        }
    }
}
