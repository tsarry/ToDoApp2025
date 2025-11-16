package com.example.todoapp2025

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.todoapp2025.data.ProfileRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalContext

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                ProfileScreen(
                    onProfileSaved = { name ->
                        lifecycleScope.launch {
                            ProfileRepository.saveProfileName(this@ProfileActivity, name)
                            startActivity(Intent(this@ProfileActivity, MainActivity::class.java))
                            finish()
                        }
                    },
                    onProfileDeleted = {
                        lifecycleScope.launch {
                            ProfileRepository.deleteProfile(this@ProfileActivity)
                            recreate() // restart activity to show empty profile
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ProfileScreen(
    onProfileSaved: (String) -> Unit,
    onProfileDeleted: () -> Unit
) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var loaded by remember { mutableStateOf(false) }

    // Load existing profile name from DataStore once
    LaunchedEffect(Unit) {
        val storedName = ProfileRepository.getProfileName(context).first()
        if (!storedName.isNullOrBlank()) {
            name = storedName
        }
        loaded = true
    }

    if (!loaded) {
        // Optional: show loading indicator while fetching
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Welcome! Enter your name:",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = { onProfileSaved(name) },
            enabled = name.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save & Continue")
        }
        Spacer(Modifier.height(12.dp))
        OutlinedButton(
            onClick = onProfileDeleted,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Delete Profile")
        }
    }
}
