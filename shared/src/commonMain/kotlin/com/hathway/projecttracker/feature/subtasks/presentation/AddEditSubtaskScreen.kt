package com.hathway.projecttracker.feature.subtasks.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hathway.projecttracker.core.ui.components.AvailableBrandIconKeys
import com.hathway.projecttracker.core.ui.components.AvailableMaterialIcons
import com.hathway.projecttracker.core.ui.components.ProjectIconView
import com.hathway.projecttracker.core.ui.components.iconVectorToString
import com.hathway.projecttracker.core.ui.theme.ProTrackTheme
import com.hathway.projecttracker.core.ui.theme.SuccessGreen
import com.hathway.projecttracker.core.ui.theme.rememberBackgroundPainter
import com.hathway.projecttracker.feature.components.ProTrackButton
import com.hathway.projecttracker.feature.components.ProTrackTopAppBar
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun AddEditSubtaskScreen(
    viewModel: AddEditSubtaskViewModel, onBack: () -> Unit, modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.isSaved) {
        if (state.isSaved) {
            onBack()
        }
    }

    AddEditSubtaskContent(
        uiState = state,
        onTitleChanged = viewModel::onTitleChanged,
        onDescriptionChanged = viewModel::onDescriptionChanged,
        onProgressChanged = viewModel::onProgressChanged,
        onNotesChanged = viewModel::onNotesChanged,
        onIconChanged = viewModel::onIconChanged,
        onToggleActivity = viewModel::onToggleActivity,
        onActivityDateChanged = viewModel::onActivityDateChanged,
        onActivityStartTimeChanged = viewModel::onActivityStartTimeChanged,
        onActivityEndTimeChanged = viewModel::onActivityEndTimeChanged,
        onActivityNotesChanged = viewModel::onActivityNotesChanged,
        onSaveClick = viewModel::saveSubtask,
        onBack = onBack,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditSubtaskContent(
    uiState: AddEditSubtaskUiState,
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onProgressChanged: (Int) -> Unit,
    onNotesChanged: (String) -> Unit,
    onIconChanged: (String) -> Unit,
    onToggleActivity: (Boolean) -> Unit,
    onActivityDateChanged: (String) -> Unit,
    onActivityStartTimeChanged: (String) -> Unit,
    onActivityEndTimeChanged: (String) -> Unit,
    onActivityNotesChanged: (String) -> Unit,
    onSaveClick: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showIconPicker by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }

    // Check if the selected date is strictly in the future compared to today
    val isFutureDate = remember(uiState.activityDate) {
        isActivityDateInFuture(uiState.activityDate)
    }

    // Parse start time into minutes from midnight (0..1439)
    val startTimeMinutes = remember(uiState.activityStartTime) {
        parseAmPmTimeToMinutes(uiState.activityStartTime)
    }

    // Compute duration dynamically from selected date, start time, and end time
    val computedDuration = remember(
        uiState.activityDate, uiState.activityStartTime, uiState.activityEndTime
    ) {
        calculateActivityDuration(
            startTime = uiState.activityStartTime,
            endTime = uiState.activityEndTime,
            isFutureDate = isFutureDate,
            fallback = uiState.activityDuration
        )
    }

    Box(
        modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
    ) {
        Image(
            painter = rememberBackgroundPainter(),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Subtle dark tint overlay for readability over design highlights
        Box(
            modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.15f))
        )

        // Top Header & Form Column scrolling together smoothly
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
        ) {
            ProTrackTopAppBar(
                title = if (uiState.subtaskId == null) "New Subtask" else "Edit Subtask",
                onBackClick = onBack
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Form Layout Container
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Subtask Icon Card with Edit Pencil Badge
                Box(
                    modifier = Modifier.size(110.dp).clickable { showIconPicker = true },
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier.size(96.dp).clip(MaterialTheme.shapes.large)
                            .background(MaterialTheme.colorScheme.surface),
                        contentAlignment = Alignment.Center
                    ) {
                        ProjectIconView(
                            iconStr = uiState.icon, modifier = Modifier.size(44.dp)
                        )
                    }

                    IconButton(
                        onClick = { showIconPicker = true },
                        modifier = Modifier.align(Alignment.BottomEnd).size(34.dp)
                            .background(MaterialTheme.colorScheme.surface, shape = CircleShape)
                            .padding(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Icon",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // Form Layout Card matching the dashboard architecture style
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Subtask Title Input Block
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            RequiredLabel(text = "Subtask Title")
                            OutlinedTextField(
                                value = uiState.title,
                                onValueChange = onTitleChanged,
                                placeholder = { Text("e.g. Google Play Store") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = MaterialTheme.shapes.medium,
                                singleLine = true
                            )
                        }

                        // Description Input Block
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = "Description",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            OutlinedTextField(
                                value = uiState.description,
                                onValueChange = onDescriptionChanged,
                                placeholder = { Text("Enter detailed workflow metrics...") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = MaterialTheme.shapes.medium,
                                minLines = 2
                            )
                        }

                        // Interactive Status Slider Component Section
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "Progress: ${uiState.progress}%",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Slider(
                                value = uiState.progress.toFloat(),
                                onValueChange = { onProgressChanged(it.toInt()) },
                                valueRange = 0f..100f,
                                steps = 19,
                                colors = SliderDefaults.colors(
                                    thumbColor = MaterialTheme.colorScheme.primary,
                                    activeTrackColor = MaterialTheme.colorScheme.primary,
                                    inactiveTrackColor = MaterialTheme.colorScheme.primaryContainer
                                )
                            )
                        }
// Activity Schedule Section with Toggle
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.medium,
                            colors = CardDefaults.cardColors(
                                containerColor = if (uiState.hasActivity) {
                                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f)
                                } else {
                                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                                }
                            )
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth().padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.FitnessCenter,
                                            contentDescription = "Activity Schedule",
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Text(
                                            text = "Activity Schedule",
                                            style = MaterialTheme.typography.bodyLarge.copy(
                                                fontWeight = FontWeight.Bold
                                            ),
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                    Switch(
                                        checked = uiState.hasActivity,
                                        onCheckedChange = onToggleActivity,
                                        colors = SwitchDefaults.colors(
                                            checkedThumbColor = Color.White,
                                            checkedTrackColor = MaterialTheme.colorScheme.primary
                                        )
                                    )
                                }

                                if (uiState.hasActivity) {
                                    HorizontalDivider(
                                        color = MaterialTheme.colorScheme.onSurface.copy(
                                            alpha = 0.08f
                                        )
                                    )
// Activity Date
                                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Text(
                                            text = "Activity Date",
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = FontWeight.Bold
                                            ),
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Box(modifier = Modifier.fillMaxWidth()) {
                                            OutlinedTextField(
                                                value = uiState.activityDate,
                                                onValueChange = {},
                                                readOnly = true,
                                                placeholder = { Text("Select Date") },
                                                trailingIcon = {
                                                    Icon(
                                                        imageVector = Icons.Default.DateRange,
                                                        contentDescription = "Select Date"
                                                    )
                                                },
                                                modifier = Modifier.fillMaxWidth(),
                                                shape = MaterialTheme.shapes.medium,
                                                singleLine = true
                                            )
                                            Box(
                                                modifier = Modifier.matchParentSize()
                                                    .clickable { showDatePicker = true })
                                        }
                                    }
// Start Time & End Time Row
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
// Start Time
                                        Column(
                                            modifier = Modifier.weight(1f),
                                            verticalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Text(
                                                text = "Start Time",
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontWeight = FontWeight.Bold
                                                ),
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            Box(modifier = Modifier.fillMaxWidth()) {
                                                OutlinedTextField(
                                                    value = uiState.activityStartTime,
                                                    onValueChange = {},
                                                    readOnly = true,
                                                    placeholder = { Text("00:00") },
                                                    trailingIcon = {
                                                        Icon(
                                                            imageVector = Icons.Default.Schedule,
                                                            contentDescription = "Select Start Time"
                                                        )
                                                    },
                                                    modifier = Modifier.fillMaxWidth(),
                                                    shape = MaterialTheme.shapes.medium,
                                                    singleLine = true
                                                )
                                                Box(
                                                    modifier = Modifier.matchParentSize()
                                                        .clickable { showStartTimePicker = true })
                                            }
                                        }
// End Time
                                        Column(
                                            modifier = Modifier.weight(1f),
                                            verticalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Text(
                                                text = "End Time",
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontWeight = FontWeight.Bold
                                                ),
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            Box(modifier = Modifier.fillMaxWidth()) {
                                                OutlinedTextField(
                                                    value = uiState.activityEndTime,
                                                    onValueChange = {},
                                                    readOnly = true,
                                                    placeholder = { Text("00:00") },
                                                    trailingIcon = {
                                                        Icon(
                                                            imageVector = Icons.Default.Schedule,
                                                            contentDescription = "Select End Time"
                                                        )
                                                    },
                                                    modifier = Modifier.fillMaxWidth(),
                                                    shape = MaterialTheme.shapes.medium,
                                                    singleLine = true
                                                )
                                                Box(
                                                    modifier = Modifier.matchParentSize()
                                                        .clickable { showEndTimePicker = true })
                                            }
                                        }
                                    }

                                    // Duration Box
                                    Surface(
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = MaterialTheme.shapes.medium,
                                        color = SuccessGreen.copy(alpha = 0.12f)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(
                                                horizontal = 14.dp, vertical = 12.dp
                                            ),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Timer,
                                                contentDescription = "Duration",
                                                tint = SuccessGreen,
                                                modifier = Modifier.size(22.dp)
                                            )
                                            Text(
                                                text = buildAnnotatedString {
                                                    withStyle(
                                                        style = SpanStyle(
                                                            fontWeight = FontWeight.Medium,
                                                            fontSize = MaterialTheme.typography.bodyMedium.fontSize
                                                        )
                                                    ) {
                                                        append("Duration: ")
                                                    }
                                                    withStyle(
                                                        style = SpanStyle(
                                                            fontWeight = FontWeight.Bold,
                                                            fontSize = MaterialTheme.typography.titleMedium.fontSize
                                                        )
                                                    ) {
                                                        append(computedDuration)
                                                    }
                                                }, color = SuccessGreen
                                            )
                                        }
                                    }


// Activity Notes
                                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Text(
                                            text = "Activity Notes",
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = FontWeight.Bold
                                            ),
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        OutlinedTextField(
                                            value = uiState.activityNotes,
                                            onValueChange = onActivityNotesChanged,
                                            placeholder = { Text("Chest and triceps workout...") },
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = MaterialTheme.shapes.medium,
                                            minLines = 2
                                        )
                                    }
                                }
                            }
                        }
// Optional Notes Input Block
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "Optional Notes",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            OutlinedTextField(
                                value = uiState.notes,
                                onValueChange = onNotesChanged,
                                placeholder = { Text("Add extra logging contexts here...") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = MaterialTheme.shapes.medium,
                                minLines = 2
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))

                        ProTrackButton(
                            onClick = onSaveClick,
                            text = if (uiState.subtaskId == null) "Create Subtask" else "Save Subtask",
                            modifier = Modifier.fillMaxWidth().height(50.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
          // 1. Built-in Calendar Date Picker Dialog
        if (showDatePicker) {
            val datePickerState = rememberDatePickerState()
            DatePickerDialog(onDismissRequest = { showDatePicker = false }, confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val localDate = kotlin.time.Instant.fromEpochMilliseconds(millis)
                                .toLocalDateTime(TimeZone.currentSystemDefault()).date
                            val monthName =
                                localDate.month.name.lowercase().replaceFirstChar { it.uppercase() }
                            val formattedDate = "${localDate.day} $monthName ${localDate.year}"
                            onActivityDateChanged(formattedDate)
                          // If user switches back to today/past date and existing endTime < startTime, clear endTime
                            val newIsFuture = isActivityDateInFuture(formattedDate)
                            val currentStartMins = parseAmPmTimeToMinutes(uiState.activityStartTime)
                            val currentEndMins = parseAmPmTimeToMinutes(uiState.activityEndTime)
                            if (!newIsFuture && currentStartMins != null && currentEndMins != null && currentEndMins < currentStartMins) {
                                onActivityEndTimeChanged("")
                            }
                        }
                        showDatePicker = false
                    }) {
                    Text("OK")
                }
            }, dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }) {
                DatePicker(state = datePickerState)
            }
        }
         // 2. Built-in Start Time Picker Dialog
        if (showStartTimePicker) {
            TimePickerModal(
                initialHour = startTimeMinutes?.div(60) ?: 18,
                initialMinute = startTimeMinutes?.rem(60) ?: 0,
                onConfirm = { hour, minute ->
                    val formattedTime = formatToAmPm(hour, minute)
                    onActivityStartTimeChanged(formattedTime)
           // If not a future date and current end time is now less than the new start time, clear end time
                    val newStartMins = hour * 60 + minute
                    val currentEndMins = parseAmPmTimeToMinutes(uiState.activityEndTime)
                    if (!isFutureDate && currentEndMins != null && currentEndMins < newStartMins) {
                        onActivityEndTimeChanged("")
                    }
                    showStartTimePicker = false
                },
                onDismiss = { showStartTimePicker = false })
        }
       // 3. Built-in End Time Picker Dialog
        if (showEndTimePicker) {
      // Default initial time to 1 hour after start time (if start time is selected)
            val defaultEndMins = if (startTimeMinutes != null) {
                (startTimeMinutes + 60).coerceAtMost(23 * 60 + 59)
            } else {
                19 * 60
            }
            TimePickerModal(
                initialHour = defaultEndMins / 60,
                initialMinute = defaultEndMins % 60,
       // Enforce minimum time only when Start Time is set AND Date is NOT a future date
                minTotalMinutes = if (!isFutureDate) startTimeMinutes else null,
                onConfirm = { hour, minute ->
                    val formattedTime = formatToAmPm(hour, minute)
                    onActivityEndTimeChanged(formattedTime)
                    showEndTimePicker = false
                },
                onDismiss = { showEndTimePicker = false })
        }
     // Icon Picker Bottom Sheet with Material & Brand Icons
        if (showIconPicker) {
            ModalBottomSheet(
                onDismissRequest = { showIconPicker = false }) {
                Column(
                    modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())
                        .padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Choose Subtask Icon",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Brand & Store Icons",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    AvailableBrandIconKeys.chunked(4).forEach { rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start)
                        ) {
                            rowItems.forEach { iconKey ->
                                val isSelected = uiState.icon.lowercase() == iconKey
                                Surface(
                                    modifier = Modifier.size(52.dp).clip(RoundedCornerShape(14.dp))
                                        .clickable {
                                            onIconChanged(iconKey)
                                            showIconPicker = false
                                        },
                                    color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(
                                        alpha = 0.5f
                                    ),
                                    shape = RoundedCornerShape(14.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        ProjectIconView(
                                            iconStr = iconKey, modifier = Modifier.size(26.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Material Icons",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    AvailableMaterialIcons.chunked(4).forEach { rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start)
                        ) {
                            rowItems.forEach { iconVector ->
                                val iconKey = iconVectorToString(iconVector)
                                val isSelected = uiState.icon.lowercase() == iconKey
                                Surface(
                                    modifier = Modifier.size(52.dp).clip(RoundedCornerShape(14.dp))
                                        .clickable {
                                            onIconChanged(iconKey)
                                            showIconPicker = false
                                        },
                                    color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(
                                        alpha = 0.5f
                                    ),
                                    shape = RoundedCornerShape(14.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = iconVector,
                                            contentDescription = null,
                                            tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.size(26.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerModal(
    initialHour: Int = 18,
    initialMinute: Int = 0,
    minTotalMinutes: Int? = null,
    onConfirm: (hour: Int, minute: Int) -> Unit,
    onDismiss: () -> Unit
) {
    val timePickerState = rememberTimePickerState(
        initialHour = initialHour, initialMinute = initialMinute, is24Hour = false
    )
    val selectedTotalMinutes = timePickerState.hour * 60 + timePickerState.minute
    val isInvalidTime = minTotalMinutes != null && selectedTotalMinutes < minTotalMinutes
    AlertDialog(onDismissRequest = onDismiss, confirmButton = {
        TextButton(
            onClick = { onConfirm(timePickerState.hour, timePickerState.minute) },
            enabled = !isInvalidTime
        ) {
            Text("OK")
        }
    }, dismissButton = {
        TextButton(onClick = onDismiss) {
            Text("Cancel")
        }
    }, text = {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TimePicker(state = timePickerState)
            if (isInvalidTime && minTotalMinutes != null) {
                val minTimeFormatted = formatToAmPm(minTotalMinutes / 60, minTotalMinutes % 60)
                Text(
                    text = "End time cannot be earlier than Start Time ($minTimeFormatted)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    })
}

// --- Date, Time & Duration Helpers ---
private fun formatToAmPm(hour: Int, minute: Int): String {
    val hStr = if (hour == 0) "12" else if (hour > 12) "${hour - 12}" else "$hour"
    val mStr = if (minute < 10) "0$minute" else "$minute"
    val amPm = if (hour >= 12) "PM" else "AM"
    return "$hStr:$mStr $amPm"
}

private fun parseAmPmTimeToMinutes(timeStr: String): Int? {
    if (timeStr.isBlank()) return null
    return try {
        val parts = timeStr.trim().split(" ")
        if (parts.size != 2) return null
        val timeParts = parts[0].split(":")
        if (timeParts.size != 2) return null
        var hour = timeParts[0].toInt()
        val minute = timeParts[1].toInt()
        val amPm = parts[1].uppercase()
        if (amPm == "PM" && hour < 12) hour += 12
        if (amPm == "AM" && hour == 12) hour = 0
        hour * 60 + minute
    } catch (e: Exception) {
        null
    }
}

private fun isActivityDateInFuture(dateStr: String): Boolean {
    if (dateStr.isBlank()) return false
    return try {
        // Expected format: "13 November 2026"
        val parts = dateStr.trim().split(" ")
        if (parts.size != 3) return false
        val year = parts[2].toInt()
        year >= 2026
    } catch (e: Exception) {
        false
    }
}

private fun calculateActivityDuration(
    startTime: String, endTime: String, isFutureDate: Boolean, fallback: String
): String {
    val startMins = parseAmPmTimeToMinutes(startTime) ?: return fallback.ifBlank { "0 mins" }
    val endMins = parseAmPmTimeToMinutes(endTime) ?: return fallback.ifBlank { "0 mins" }
    var diffMins = endMins - startMins
    if (diffMins < 0 && isFutureDate) {
        diffMins += 24 * 60
    } else if (diffMins < 0) {
        return "0 mins"
    }
    val hours = diffMins / 60
    val mins = diffMins % 60
    return when {
        hours > 0 && mins > 0 -> "${hours}h ${mins}m"
        hours > 0 -> "${hours}h"
        else -> "${mins}m"
    }
}

@Composable
private fun RequiredLabel(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "*",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.error
        )
    }
}


// --- Preview Configurations ---
@Preview(name = "Light Mode Subtask Entry", showSystemUi = true)
@Composable
fun AddEditSubtaskScreenLightPreview() {
    val mockState = AddEditSubtaskUiState(
        title = "Google Play Store",
        description = "Production release track deployment listing configuration",
        progress = 65,
        notes = "Pending alpha tester credentials validation",
        icon = "googleplay"
    )
    ProTrackTheme(darkTheme = false) {
        AddEditSubtaskContent(
            uiState = mockState,
            onTitleChanged = {},
            onDescriptionChanged = {},
            onProgressChanged = {},
            onNotesChanged = {},
            onIconChanged = {},
            onToggleActivity = {},
            onActivityDateChanged = {},
            onActivityStartTimeChanged = {},
            onActivityEndTimeChanged = {},
            onActivityNotesChanged = {},
            onSaveClick = {},
            onBack = {})
    }
}

@Preview(name = "Dark Mode Subtask Entry", showSystemUi = true)
@Composable
fun AddEditSubtaskScreenDarkPreview() {
    val mockState = AddEditSubtaskUiState(
        subtaskId = "subtask_1",
        title = "App Store Connect Review",
        progress = 100,
        icon = "appstore",
        hasActivity = true
    )
    ProTrackTheme(darkTheme = true) {
        AddEditSubtaskContent(
            uiState = mockState,
            onTitleChanged = {},
            onDescriptionChanged = {},
            onProgressChanged = {},
            onNotesChanged = {},
            onIconChanged = {},
            onToggleActivity = {},
            onActivityDateChanged = {},
            onActivityStartTimeChanged = {},
            onActivityEndTimeChanged = {},
            onActivityNotesChanged = {},
            onSaveClick = {},
            onBack = {})
    }
}