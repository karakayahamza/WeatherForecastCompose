package com.example.weatherforecastcompose.view.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AnimatedDotIndicator(
    pagerState: PagerState, modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier, verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pagerState.pageCount) { pageIndex ->
            val dotRotation by animateFloatAsState(
                targetValue = if (pageIndex == pagerState.currentPage) 360f else 0f,
                animationSpec = spring(
                    dampingRatio = 0.8f, stiffness = Spring.StiffnessLow
                ),
                label = ""
            )

            val dotScale by animateFloatAsState(
                targetValue = if (pageIndex == pagerState.currentPage) 1.2f else 1f,
                animationSpec = spring(
                    dampingRatio = 0.8f, stiffness = Spring.StiffnessLow
                ),
                label = ""
            )

            val dotColor by animateColorAsState(
                targetValue = if (pageIndex == pagerState.currentPage) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary,
                animationSpec = spring(
                    dampingRatio = 0.8f, stiffness = Spring.StiffnessLow
                ),
                label = ""
            )

            Box(
                modifier = Modifier
                    .size(10.dp)
                    .graphicsLayer(
                        scaleX = dotScale, scaleY = dotScale, rotationZ = dotRotation
                    )
                    .clip(CircleShape)
                    .background(dotColor)
            )
            Spacer(modifier = Modifier.size(10.dp))
        }
    }
}
