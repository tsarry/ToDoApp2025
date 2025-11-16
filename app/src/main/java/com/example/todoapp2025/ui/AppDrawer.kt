package com.example.todoapp2025.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDrawer(
    drawerState: DrawerState,
    scope: CoroutineScope,
    currentScreen: String,
    onNavigateMain: () -> Unit,
    onNavigateCat: () -> Unit,
    onNavigateProfile: () -> Unit,
    content: @Composable (Modifier) -> Unit
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = "Navigate",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )

                NavigationDrawerItem(
                    label = { Text("Main") },
                    selected = currentScreen == "Main",
                    onClick = {
                        scope.launch { drawerState.close() }
                        onNavigateMain()
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                NavigationDrawerItem(
                    label = { Text("Cat") },
                    selected = currentScreen == "Cat",
                    onClick = {
                        scope.launch { drawerState.close() }
                        onNavigateCat()
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                NavigationDrawerItem(
                    label = { Text("Profile") },
                    selected = currentScreen == "Profile",
                    onClick = {
                        scope.launch { drawerState.close() }
                        onNavigateProfile()
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }
        }
    ) {
        // Pass in a Modifier to the content so you can use it safely
        content(Modifier)
    }
}
