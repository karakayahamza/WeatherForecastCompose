package com.example.weatherforecastcompose.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
    backgroundColor: Color = MaterialTheme.colorScheme.primary // Default color if not provided
) {
    TopAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = backgroundColor),

        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f))

                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    DotIndicator(
                        pagerState = pagerState, modifier = Modifier.align(Alignment.Center)
                    )
                }

                Box(
                    modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterEnd
                ) {
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
        },
    )
}