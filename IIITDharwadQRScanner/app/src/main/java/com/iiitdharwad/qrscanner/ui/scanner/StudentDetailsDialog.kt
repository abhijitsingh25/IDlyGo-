package com.iiitdharwad.qrscanner.ui.scanner

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.iiitdharwad.qrscanner.data.ScannerMode
import com.iiitdharwad.qrscanner.data.Student
import com.iiitdharwad.qrscanner.ui.components.ActionButton
import com.iiitdharwad.qrscanner.utils.DateTimeUtils

@Composable
fun StudentDetailsDialog(
    student: Student,
    scannerMode: ScannerMode,
    isLoading: Boolean,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    Dialog(onDismissRequest = { if (!isLoading) onCancel() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Student Details",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Student information
                StudentInfoRow(label = "Name", value = student.name)
                Spacer(modifier = Modifier.height(12.dp))
                StudentInfoRow(label = "Registration No", value = student.regNo)
                Spacer(modifier = Modifier.height(12.dp))
                StudentInfoRow(label = "Time", value = DateTimeUtils.getCurrentFormattedDateTime())
                Spacer(modifier = Modifier.height(12.dp))
                StudentInfoRow(label = "Action", value = scannerMode.getDisplayName())

                Spacer(modifier = Modifier.height(32.dp))

                if (isLoading) {
                    CircularProgressIndicator()
                } else {
                    // Action buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedButton(
                            onClick = onCancel,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancel")
                        }

                        ActionButton(
                            text = "Confirm ${scannerMode.getDisplayName()}",
                            onClick = onConfirm,
                            modifier = Modifier.weight(1f),
                            containerColor = if (scannerMode == ScannerMode.CHECK_IN) {
                                MaterialTheme.colorScheme.secondary
                            } else {
                                MaterialTheme.colorScheme.tertiary
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StudentInfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}