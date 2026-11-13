package com.hathway.projecttracker.core.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
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
    Icons.Rounded.Shop,
    Icons.Rounded.Approval,
    Icons.Rounded.Web,
    Icons.Rounded.Games
)

val AvailableBrandIconKeys: List<String> = listOf(
    "googleplay",
    "appstore",
    "apple",
    "android",
    "github",
    "figma",
    "firebase",
    "youtube",
    "slack",
    "jira",
    "notion",
    "docker",
    "stripe",
    "aws",
    "fitness",
    "kotlin",
    "swift",
    "react",
    "python",
    "gitlab",
    "bitbucket",
    "vscode",
    "branch",
    "merge",
    "commit"
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
        "android" -> {
            Image(
                painter = painterResource(Res.drawable.ic_android),
                contentDescription = "Android Icon",
                modifier = modifier
            )
        }
        "github", "git" -> {
            Icon(
                painter = painterResource(Res.drawable.ic_github),
                contentDescription = "GitHub Icon",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = modifier
            )
        }
        "figma", "design" -> {
            Image(
                painter = painterResource(Res.drawable.ic_figma),
                contentDescription = "Figma Icon",
                modifier = modifier
            )
        }
        "firebase" -> {
            Image(
                painter = painterResource(Res.drawable.ic_firebase),
                contentDescription = "Firebase Icon",
                modifier = modifier
            )
        }
        "youtube", "video" -> {
            Image(
                painter = painterResource(Res.drawable.ic_youtube),
                contentDescription = "YouTube Icon",
                modifier = modifier
            )
        }
        "slack", "chat" -> {
            Image(
                painter = painterResource(Res.drawable.ic_slack),
                contentDescription = "Slack Icon",
                modifier = modifier
            )
        }
        "jira", "atlassian" -> {
            Image(
                painter = painterResource(Res.drawable.ic_jira),
                contentDescription = "Jira Icon",
                modifier = modifier
            )
        }
        "notion", "docs" -> {
            Image(
                painter = painterResource(Res.drawable.ic_notion),
                contentDescription = "Notion Icon",
                modifier = modifier
            )
        }
        "docker", "container" -> {
            Image(
                painter = painterResource(Res.drawable.ic_docker),
                contentDescription = "Docker Icon",
                modifier = modifier
            )
        }
        "stripe", "payment" -> {
            Image(
                painter = painterResource(Res.drawable.ic_stripe),
                contentDescription = "Stripe Icon",
                modifier = modifier
            )
        }
        "aws", "cloud" -> {
            Image(
                painter = painterResource(Res.drawable.ic_aws),
                contentDescription = "AWS Icon",
                modifier = modifier
            )
        }
        "fitness", "workout", "gym" -> {
            Image(
                painter = painterResource(Res.drawable.ic_fitness),
                contentDescription = "Fitness Icon",
                modifier = modifier
            )
        }
        "kotlin", "kt" -> {
            Image(
                painter = painterResource(Res.drawable.ic_kotlin),
                contentDescription = "Kotlin Icon",
                modifier = modifier
            )
        }
        "swift" -> {
            Image(
                painter = painterResource(Res.drawable.ic_swift),
                contentDescription = "Swift Icon",
                modifier = modifier
            )
        }
        "react", "reactjs", "react_native" -> {
            Image(
                painter = painterResource(Res.drawable.ic_react),
                contentDescription = "React Icon",
                modifier = modifier
            )
        }
        "python", "py" -> {
            Image(
                painter = painterResource(Res.drawable.ic_python),
                contentDescription = "Python Icon",
                modifier = modifier
            )
        }
        "gitlab" -> {
            Image(
                painter = painterResource(Res.drawable.ic_gitlab),
                contentDescription = "GitLab Icon",
                modifier = modifier
            )
        }
        "bitbucket" -> {
            Image(
                painter = painterResource(Res.drawable.ic_bitbucket),
                contentDescription = "Bitbucket Icon",
                modifier = modifier
            )
        }
        "vscode", "code", "editor" -> {
            Image(
                painter = painterResource(Res.drawable.ic_vscode),
                contentDescription = "VS Code Icon",
                modifier = modifier
            )
        }
        "branch", "git_branch" -> {
            Image(
                painter = painterResource(Res.drawable.ic_git_branch),
                contentDescription = "Git Branch Icon",
                modifier = modifier
            )
        }
        "merge", "pull_request", "pr" -> {
            Image(
                painter = painterResource(Res.drawable.ic_git_merge),
                contentDescription = "Git Merge Icon",
                modifier = modifier
            )
        }
        "commit", "git_commit" -> {
            Image(
                painter = painterResource(Res.drawable.ic_git_commit),
                contentDescription = "Git Commit Icon",
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
        "android" -> {
            Image(
                painter = painterResource(Res.drawable.ic_android),
                contentDescription = "Android Icon",
                modifier = modifier
            )
        }
        "github", "git" -> {
            Icon(
                painter = painterResource(Res.drawable.ic_github),
                contentDescription = "GitHub Icon",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = modifier
            )
        }
        "figma", "design" -> {
            Image(
                painter = painterResource(Res.drawable.ic_figma),
                contentDescription = "Figma Icon",
                modifier = modifier
            )
        }
        "firebase" -> {
            Image(
                painter = painterResource(Res.drawable.ic_firebase),
                contentDescription = "Firebase Icon",
                modifier = modifier
            )
        }
        "youtube", "video" -> {
            Image(
                painter = painterResource(Res.drawable.ic_youtube),
                contentDescription = "YouTube Icon",
                modifier = modifier
            )
        }
        "slack", "chat" -> {
            Image(
                painter = painterResource(Res.drawable.ic_slack),
                contentDescription = "Slack Icon",
                modifier = modifier
            )
        }
        "jira", "atlassian" -> {
            Image(
                painter = painterResource(Res.drawable.ic_jira),
                contentDescription = "Jira Icon",
                modifier = modifier
            )
        }
        "notion", "docs" -> {
            Image(
                painter = painterResource(Res.drawable.ic_notion),
                contentDescription = "Notion Icon",
                modifier = modifier
            )
        }
        "docker", "container" -> {
            Image(
                painter = painterResource(Res.drawable.ic_docker),
                contentDescription = "Docker Icon",
                modifier = modifier
            )
        }
        "stripe", "payment" -> {
            Image(
                painter = painterResource(Res.drawable.ic_stripe),
                contentDescription = "Stripe Icon",
                modifier = modifier
            )
        }
        "aws", "cloud" -> {
            Image(
                painter = painterResource(Res.drawable.ic_aws),
                contentDescription = "AWS Icon",
                modifier = modifier
            )
        }
        "fitness", "workout", "gym" -> {
            Image(
                painter = painterResource(Res.drawable.ic_fitness),
                contentDescription = "Fitness Icon",
                modifier = modifier
            )
        }
        "kotlin", "kt" -> {
            Image(
                painter = painterResource(Res.drawable.ic_kotlin),
                contentDescription = "Kotlin Icon",
                modifier = modifier
            )
        }
        "swift" -> {
            Image(
                painter = painterResource(Res.drawable.ic_swift),
                contentDescription = "Swift Icon",
                modifier = modifier
            )
        }
        "react", "reactjs", "react_native" -> {
            Image(
                painter = painterResource(Res.drawable.ic_react),
                contentDescription = "React Icon",
                modifier = modifier
            )
        }
        "python", "py" -> {
            Image(
                painter = painterResource(Res.drawable.ic_python),
                contentDescription = "Python Icon",
                modifier = modifier
            )
        }
        "gitlab" -> {
            Image(
                painter = painterResource(Res.drawable.ic_gitlab),
                contentDescription = "GitLab Icon",
                modifier = modifier
            )
        }
        "bitbucket" -> {
            Image(
                painter = painterResource(Res.drawable.ic_bitbucket),
                contentDescription = "Bitbucket Icon",
                modifier = modifier
            )
        }
        "vscode", "code", "editor" -> {
            Image(
                painter = painterResource(Res.drawable.ic_vscode),
                contentDescription = "VS Code Icon",
                modifier = modifier
            )
        }
        "branch", "git_branch" -> {
            Image(
                painter = painterResource(Res.drawable.ic_git_branch),
                contentDescription = "Git Branch Icon",
                modifier = modifier
            )
        }
        "merge", "pull_request", "pr" -> {
            Image(
                painter = painterResource(Res.drawable.ic_git_merge),
                contentDescription = "Git Merge Icon",
                modifier = modifier
            )
        }
        "commit", "git_commit" -> {
            Image(
                painter = painterResource(Res.drawable.ic_git_commit),
                contentDescription = "Git Commit Icon",
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
        t.contains("android") -> "android"
        t.contains("github") || t.contains("git") -> "github"
        t.contains("figma") -> "figma"
        t.contains("firebase") -> "firebase"
        t.contains("youtube") -> "youtube"
        t.contains("slack") -> "slack"
        t.contains("jira") -> "jira"
        t.contains("notion") -> "notion"
        t.contains("docker") -> "docker"
        t.contains("stripe") -> "stripe"
        t.contains("aws") -> "aws"
        t.contains("gym") || t.contains("workout") || t.contains("fitness") -> "fitness"
        t.contains("kotlin") || t.contains("kt") -> "kotlin"
        t.contains("swift") -> "swift"
        t.contains("react") -> "react"
        t.contains("python") -> "python"
        t.contains("gitlab") -> "gitlab"
        t.contains("bitbucket") -> "bitbucket"
        t.contains("code") || t.contains("vscode") -> "vscode"
        t.contains("branch") -> "branch"
        t.contains("merge") || t.contains("pr") -> "merge"
        t.contains("commit") -> "commit"
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
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "Brand Icons Showcase",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AvailableBrandIconKeys.forEach { brandKey ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
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
                        ProjectIconView(iconStr = brandKey, modifier = Modifier.size(32.dp))
                    }
                    Text(
                        text = brandKey,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Normal Material Icons Showcase",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AvailableMaterialIcons.take(16).forEach { vector ->
                val key = iconVectorToString(vector)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
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
                        Icon(
                            imageVector = vector,
                            contentDescription = key,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Text(
                        text = key,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Preview(name = "Brand & Normal Icons Light", showBackground = true)
@Composable
fun IconsLightPreview() {
    ProTrackTheme(darkTheme = false) {
        Surface(color = MaterialTheme.colorScheme.background) {
            BuiltInIconShowcase()
        }
    }
}

@Preview(name = "Brand & Normal Icons Dark", showBackground = true)
@Composable
fun IconsDarkPreview() {
    ProTrackTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colorScheme.background) {
            BuiltInIconShowcase()
        }
    }
}
