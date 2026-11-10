package com.hathway.projecttracker.feature.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hathway.projecttracker.core.ui.theme.ProTrackTheme

@Composable
fun ProTrackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String? = null,
    iconSymbol: String? = null, // Cross-platform symbol/emoji (e.g. "+" or "🔍")
    iconContentDescription: String? = null
) {
    val paddingValues = when {
        text != null && iconSymbol != null -> PaddingValues(start = 12.dp, top = 8.dp, end = 16.dp, bottom = 8.dp)
        iconSymbol != null -> PaddingValues(horizontal = 8.dp, vertical = 8.dp)
        else -> PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    }

    Button(
        onClick = onClick,
        modifier = modifier,
        shape = MaterialTheme.shapes.extraLarge,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        contentPadding = paddingValues
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (iconSymbol != null) {
                val iconModifier = if (iconContentDescription != null) {
                    Modifier.semantics { contentDescription = iconContentDescription }
                } else {
                    Modifier
                }
                Text(
                    text = iconSymbol,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = iconModifier
                )
            }

            if (iconSymbol != null && text != null) {
                Spacer(modifier = Modifier.width(6.dp))
            }

            if (text != null) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Composable
private fun ButtonShowcase() {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Variant 1: Icon Symbol + Text
        ProTrackButton(
            onClick = {},
            text = "Project",
            iconSymbol = "+",
            iconContentDescription = "Add project icon"
        )

        // Variant 2: Text Only
        ProTrackButton(
            onClick = {},
            text = "Submit"
        )

        // Variant 3: Icon Only
        ProTrackButton(
            onClick = {},
            iconSymbol = "🔍",
            iconContentDescription = "Search icon"
        )
    }
}

@Preview(name = "Light Mode Button Showcase", showBackground = true)
@Composable
fun ProTrackButtonLightPreview() {
    ProTrackTheme(darkTheme = false) {
        Surface(color = MaterialTheme.colorScheme.background) {
            ButtonShowcase()
        }
    }
}

@Preview(name = "Dark Mode Button Showcase", showBackground = true)
@Composable
fun ProTrackButtonDarkPreview() {
    ProTrackTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colorScheme.background) {
            ButtonShowcase()
        }
    }
}
