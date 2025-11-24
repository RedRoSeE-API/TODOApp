package com.example.todoapp.dialog

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.todoapp.event.ItemEvent
import com.example.todoapp.shared.ItemState
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun AddItemDialog(
    state: ItemState,
    onEvent: (ItemEvent) -> Unit,
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    state.dueDate?.let { calendar.time = it }

    val timePickerDialog =
        remember {
            TimePickerDialog(
                context,
                { _, hour, minute ->
                    calendar.set(Calendar.HOUR_OF_DAY, hour)
                    calendar.set(Calendar.MINUTE, minute)
                    onEvent(ItemEvent.SetDueDate(calendar.time))
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true,
            )
        }

    val datePickerDialog =
        remember {
            DatePickerDialog(
                context,
                { _, year, month, day ->
                    calendar.set(year, month, day)
                    onEvent(ItemEvent.SetDueDate(calendar.time))
                    timePickerDialog.show()
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
            )
        }

    Dialog(
        onDismissRequest = { onEvent(ItemEvent.HideDialog) },
    ) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            OutlinedTextField(
                value = state.title,
                onValueChange = { onEvent(ItemEvent.SetTitle(it)) },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = state.description,
                onValueChange = { onEvent(ItemEvent.SetDescription(it)) },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5,
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = { datePickerDialog.show() },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text =
                        state.dueDate?.let {
                            SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(it)
                        } ?: "Select due date",
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TextButton(onClick = { onEvent(ItemEvent.HideDialog) }) {
                    Text("Cancel")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { onEvent(ItemEvent.SaveItem) },
                    enabled = state.title.isNotBlank(),
                ) {
                    Text("Save")
                }
            }
        }
    }
}
