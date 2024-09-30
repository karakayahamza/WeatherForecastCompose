package com.example.weatherforecastcompose.presentation.compenents

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainTopAppBar(
    pagerState: PagerState,
    drawerState: DrawerState,
    backgroundColor: Color = MaterialTheme.colorScheme.onPrimary,
    currentPlace: Pair<Double, Double>?
) {
    val scope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = backgroundColor),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { showDialog = true }) {
                    Icon(Icons.Default.Info, contentDescription = "Info")
                }

                Box(
                    modifier = Modifier
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    AnimatedDotIndicator(
                        pagerState = pagerState,
                        currentPlace = currentPlace,
                    )
                }

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
        }
    )

    CustomAlertDialog(
        showDialog = showDialog,
        onDismissRequest = { showDialog = false },
        dialogTitle = "Rüzgar Yönleri",
        dialogText = "K: Kuzey\nKD: Kuzeydoğu\nD: Doğu\nGD: Güneydoğu\nG: Güney\nGB: Güneybatı\nB: Batı\nKB: Kuzeybatı",
        icon = Icons.Default.Info
    )
}