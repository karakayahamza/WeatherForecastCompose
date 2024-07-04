package com.example.weatherforecastcompose.view.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainTopAppBar(
    pagerState: PagerState,
    drawerState: DrawerState,
    scope: CoroutineScope,
    backgroundColor: Color = MaterialTheme.colorScheme.primary
) {
    var showDialog by remember { mutableStateOf(false) }

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = backgroundColor),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { showDialog = true }) {
                    Icon(Icons.Default.Info, contentDescription = "Info")
                }

                AnimatedDotIndicator(pagerState = pagerState)

                val iconState = remember { mutableStateOf(false) }

                AnimatedNavDrawerMenuButton(isOpen = drawerState.isOpen, onToggle = {
                    iconState.value = !iconState.value
                    scope.launch {
                        if (drawerState.isOpen) {

                            drawerState.close()
                        } else {
                            drawerState.open()
                        }
                    }
                })
            }
        },
    )

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Rüzgar Yönleri") },
            text = {
                Column {
                    Text("K: Kuzey")
                    Text("KD: Kuzeydoğu")
                    Text("D: Doğu")
                    Text("GD: Güneydoğu")
                    Text("G: Güney")
                    Text("GB: Güneybatı")
                    Text("B: Batı")
                    Text("KB: Kuzeybatı")
                }
            },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Tamam")
                }
            }
        )
    }
}

