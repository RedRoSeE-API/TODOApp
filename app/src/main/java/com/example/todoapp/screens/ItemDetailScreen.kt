package com.example.todoapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.todoapp.entity.Item
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ItemViewScreen(item: Item?, navController: NavController) {
    if (item == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Go back"
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "View TODO",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = item.title,
            onValueChange = { },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            readOnly = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = item.description.ifEmpty { "No description" },
            onValueChange = { },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            maxLines = 5,
            readOnly = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = item.dueDate?.let {
                SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault()).format(it)
            } ?: "Not set",
            onValueChange = { },
            label = { Text("Due Date") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            readOnly = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Created: ${item.createdOn?.let {
                SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(it)
            } ?: "Unknown"}",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.outline
        )
    }
}