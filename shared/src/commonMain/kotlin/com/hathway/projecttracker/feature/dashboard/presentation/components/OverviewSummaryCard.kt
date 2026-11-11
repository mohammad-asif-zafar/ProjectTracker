package com.hathway.projecttracker.feature.dashboard.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.HourglassEmpty
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hathway.projecttracker.core.ui.theme.ProTrackTheme
import com.hathway.projecttracker.core.ui.theme.SuccessGreen
import com.hathway.projecttracker.domain.model.ProjectProgress

@Composable
fun OverviewSummaryCard(
    progress: ProjectProgress,
    modifier: Modifier = Modifier
) {
    val total = progress.totalProjects
    val completedPercentage = if (total > 0) "${(progress.completedProjects * 100) / total}%" else "0%"
    val inProgressPercentage = if (total > 0) "${(progress.inProgressProjects * 100) / total}%" else "0%"
    val pendingPercentage = if (total > 0) "${(progress.pendingProjects * 100) / total}%" else "0%"

    // 1. Define the internal target state for animation initialization
    var targetProgress by remember { mutableFloatStateOf(0f) }

    // 2. Trigger the animation upon entering the Composition Lifecycle
    LaunchedEffect(progress.overallCompletion) {
        targetProgress = (progress.overallCompletion.coerceIn(0, 100)) / 100f
    }

    // 3. Create an animated float value that follows a smooth 1000ms easing curve
    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(durationMillis = 1000),
        label = "CircularProgressAnimation"
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Side: Google Material Standard Sized Animated Circular Progress Indicator
            Box(
                modifier = Modifier.size(88.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier.fillMaxSize(),
                    color = SuccessGreen,
                    trackColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                    strokeWidth = 8.dp
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    AnimatedContent(
                        targetState = progress.overallCompletion,
                        transitionSpec = { androidx.compose.animation.fadeIn() togetherWith androidx.compose.animation.fadeOut() },
                        label = "TextCompletionAnimation"
                    ) { completionValue ->
                        Text(
                            text = "$completionValue%",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            ),
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                    Spacer(modifier = Modifier.height(1.dp))
                    Text(
                        text = "Overall\nProgress",
                        style = MaterialTheme.typography.bodySmall.copy(
                            lineHeight = 12.sp,
                            fontSize = 10.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Right Side: Breakdown Rows with non-wrapping percentage badges
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                StatusBreakdownRow(
                    icon = Icons.Rounded.CheckCircle,
                    iconColor = SuccessGreen,
                    countLabel = "${progress.completedProjects} Done",
                    percentageValue = completedPercentage,
                    badgeColor = SuccessGreen
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 6.dp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f)
                )

                StatusBreakdownRow(
                    icon = Icons.Rounded.HourglassEmpty,
                    iconColor = MaterialTheme.colorScheme.primary,
                    countLabel = "${progress.inProgressProjects} In Progress",
                    percentageValue = inProgressPercentage,
                    badgeColor = MaterialTheme.colorScheme.primary
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 6.dp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f)
                )

                StatusBreakdownRow(
                    icon = Icons.Rounded.RadioButtonUnchecked,
                    iconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    countLabel = "${progress.pendingProjects} Pending",
                    percentageValue = pendingPercentage,
                    badgeColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun StatusBreakdownRow(
    icon: ImageVector,
    iconColor: Color,
    countLabel: String,
    percentageValue: String,
    badgeColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = countLabel,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                softWrap = false
            )
        }

        Surface(
            shape = MaterialTheme.shapes.small,
            color = badgeColor.copy(alpha = 0.10f)
        ) {
            Text(
                text = percentageValue,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                color = badgeColor,
                maxLines = 1,
                softWrap = false,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
            )
        }
    }
}
// --- Previews ---

private val mockProgress = ProjectProgress(
    totalProjects = 5,
    completedProjects = 2,
    inProgressProjects = 3,
    pendingProjects = 0,
    totalSubtasks = 10,
    completedSubtasks = 4,
    overallCompletion = 36
)

@Preview(name = "Vector Light Mode", showBackground = true)
@Composable
fun OverviewSummaryCardVectorLightPreview() {
    ProTrackTheme(darkTheme = false) {
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.padding(16.dp)
        ) {
            OverviewSummaryCard(progress = mockProgress)
        }
    }
}

@Preview(name = "Vector Dark Mode", showBackground = true)
@Composable
fun OverviewSummaryCardVectorDarkPreview() {
    ProTrackTheme(darkTheme = true) {
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.padding(16.dp)
        ) {
            OverviewSummaryCard(progress = mockProgress)
        }
    }
}
