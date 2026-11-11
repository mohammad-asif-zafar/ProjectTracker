package com.hathway.projecttracker.core.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hathway.projecttracker.core.ui.theme.ProTrackTheme
import org.jetbrains.compose.resources.painterResource
import projecttracker.shared.generated.resources.*

/**
 * A highly scalable Material Icon registry that is fully cross-platform safe.
 * Includes explicit additions for global brand and store vectors.
 */
val AvailableMaterialIcons: List<ImageVector> = listOf(
    Icons.Rounded.Folder,
    Icons.Rounded.Code,
    Icons.Rounded.LaptopMac,
    Icons.Rounded.Smartphone,
    Icons.Rounded.Brush,
    Icons.Rounded.Palette,
    Icons.Rounded.Build,
    Icons.Rounded.Settings,
    Icons.Rounded.Analytics,
    Icons.Rounded.PieChart,
    Icons.Rounded.Groups,
    Icons.Rounded.Assignment,
    Icons.Rounded.Cloud,
    Icons.Rounded.Lock,
    Icons.Rounded.Speed,
    Icons.Rounded.AutoAwesome,
    Icons.Rounded.Home,
    Icons.Rounded.Star,
    Icons.Rounded.Favorite,
    Icons.Rounded.Work,

    // Core Utilities
    Icons.Rounded.Search,
    Icons.Rounded.Add,
    Icons.Rounded.CheckCircle,
    Icons.Rounded.Close,
    Icons.Rounded.Notifications,
    Icons.Rounded.Info,
    Icons.Rounded.Help,

    // Brand & App Store Extensions
    Icons.Rounded.Shop,          // Standard Material vector representation for Play Store / Android Market
    Icons.Rounded.Approval,      // Standard Material vector representation for iOS / App Store
    Icons.Rounded.Web,           // Representation for Web apps / Deployment targets
    Icons.Rounded.Games          // Representation for Games / Play Console listings
)

val AvailableBrandIconKeys: List<String> = listOf(
    "googleplay",
    "appstore",
    "apple",
    "tablet"
)

/**
 * Optimized Map for static O(1) lookups on both Android and iOS targets.
 */
private val IconMap: Map<String, ImageVector> by lazy {
    AvailableMaterialIcons.associateBy { vector ->
        vector.name.substringAfter(".").lowercase()
    }
}

/**
 * Automatically parses any image vector item back to its text lookup key for clean JSON serializations.
 */
fun iconVectorToString(vector: ImageVector): String {
    val cleanKey = vector.name.substringAfter(".").lowercase()
    return if (IconMap.containsKey(cleanKey)) cleanKey else "folder"
}

/**
 * Dynamically evaluates incoming lookup text arguments to resolve the targeted Material vector profile.
 */
fun getIconVectorOrNull(iconStr: String): ImageVector? {
    val cleanInput = iconStr.lowercase().trim()
    return when (cleanInput) {
        "laptop" -> Icons.Rounded.LaptopMac
        "laptopmac" -> Icons.Rounded.LaptopMac
        "phone" -> Icons.Rounded.Smartphone
        "smartphone" -> Icons.Rounded.Smartphone
        "ai" -> Icons.Rounded.AutoAwesome
        "autoawesome" -> Icons.Rounded.AutoAwesome
        else -> IconMap[cleanInput]
    }
}

/**
 * Enhanced lookup function supporting both Material Vector tokens
 * and custom branding resource assets.
 */
@Composable
fun ProjectIconView(iconStr: String, modifier: Modifier = Modifier) {
    val cleanInput = iconStr.lowercase().trim()

    when (cleanInput) {
        "googleplay", "playstore", "shop" -> {
            Image(
                painter = painterResource(Res.drawable.ic_google_play),
                contentDescription = "Google Play Icon",
                modifier = modifier
            )
        }
        "appstore", "ios_store" -> {
            Image(
                painter = painterResource(Res.drawable.ic_app_store),
                contentDescription = "App Store Icon",
                modifier = modifier
            )
        }
        "apple", "ios" -> {
            Icon(
                painter = painterResource(Res.drawable.ic_apple_logo),
                contentDescription = "Apple Brand Logo",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = modifier
            )
        }
        "tablet", "tablet_android" -> {
            Icon(
                imageVector = Icons.Rounded.TabletAndroid,
                contentDescription = "Tablet Icon",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = modifier
            )
        }
        else -> {
            val vector = getIconVectorOrNull(cleanInput)
            if (vector != null) {
                Icon(
                    imageVector = vector,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = modifier
                )
            } else {
                Box(contentAlignment = Alignment.Center, modifier = modifier) {
                    Text(
                        text = iconStr.ifBlank { "📁" },
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            }
        }
    }
}

/**
 * Dedicated standalone layout utility for explicit task items
 */
@Composable
fun TaskBrandIcon(iconKey: String, modifier: Modifier = Modifier) {
    val cleanKey = iconKey.lowercase().trim()
    when (cleanKey) {
        "googleplay", "playstore", "shop" -> {
            Image(
                painter = painterResource(Res.drawable.ic_google_play),
                contentDescription = "Google Play Icon",
                modifier = modifier
            )
        }
        "appstore", "ios_store" -> {
            Image(
                painter = painterResource(Res.drawable.ic_app_store),
                contentDescription = "App Store Icon",
                modifier = modifier
            )
        }
        "apple", "ios" -> {
            Icon(
                painter = painterResource(Res.drawable.ic_apple_logo),
                contentDescription = "Apple Brand Logo",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = modifier
            )
        }
        "tablet", "tablet_android" -> {
            Icon(
                imageVector = Icons.Rounded.TabletAndroid,
                contentDescription = "Tablet Icon",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = modifier
            )
        }
        else -> {
            Icon(
                imageVector = Icons.Rounded.Assignment,
                contentDescription = "Task Icon",
                tint = MaterialTheme.colorScheme.primary,
                modifier = modifier
            )
        }
    }
}

/**
 * Intelligent helper to map subtask titles to their respective brand or vector icon keys.
 */
fun getSubtaskIconKey(title: String): String {
    val t = title.lowercase()
    return when {
        t.contains("google") || t.contains("play") -> "googleplay"
        t.contains("app store") || t.contains("appstore") -> "appstore"
        t.contains("ios") || t.contains("apple") -> "apple"
        t.contains("tablet") -> "tablet"
        else -> "assignment"
    }
}

// --- Previews ---

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun BuiltInIconShowcase() {
    Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Platform Store Icons Preview",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val targetedPlatforms = listOf("googleplay", "appstore", "apple", "tablet")
            targetedPlatforms.forEach { platform ->
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                            shape = MaterialTheme.shapes.medium
                        )
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    ProjectIconView(iconStr = platform, modifier = Modifier.size(32.dp))
                }
            }
        }
    }
}

@Preview(name = "Store Icons Light View", showBackground = true)
@Composable
fun StoreIconsLightPreview() {
    ProTrackTheme(darkTheme = false) {
        Surface(color = MaterialTheme.colorScheme.background) {
            BuiltInIconShowcase()
        }
    }
}

@Preview(name = "Store Icons Dark View", showBackground = true)
@Composable
fun StoreIconsDarkPreview() {
    ProTrackTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colorScheme.background) {
            BuiltInIconShowcase()
        }
    }
}
