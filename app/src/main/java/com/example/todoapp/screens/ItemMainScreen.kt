package com.example.todoapp.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.todoapp.dialog.AddItemDialog
import com.example.todoapp.event.ItemEvent
import com.example.todoapp.nav.Screen
import com.example.todoapp.shared.ItemState
import com.example.todoapp.shared.SortTypes
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun ItemScreen(
    navController: NavController,
    state: ItemState,
    onEvent: (ItemEvent) -> Unit,
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                onEvent(ItemEvent.ShowDialog)
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add TODO")
            }
        },
    ) { padding ->
        if (state.isAddingItem) {
            AddItemDialog(
                state = state,
                onEvent = onEvent,
            )
        }

        LazyColumn(
            contentPadding = padding,
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxSize().horizontalScroll(rememberScrollState()),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    SortTypes.entries.forEach { sortType ->
                        Row(
                            modifier =
                                Modifier.clickable {
                                    onEvent(ItemEvent.SortItems(sortType))
                                },
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            RadioButton(
                                selected = state.sortType == sortType,
                                onClick = {
                                    onEvent(ItemEvent.SortItems(sortType))
                                },
                            )
                            Text(text = sortType.name)
                        }
                    }
                }
            }

            items(state.items) { item ->
                Row(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Column(
                        modifier =
                            Modifier.weight(1f).clickable {
                                navController.navigate(Screen.ItemViewScreen.route + "/${item.id}")
                            },
                    ) {
                        Text(
                            text = item.title,
                            fontSize = 20.sp,
                        )
                        Text(
                            text =
                                if (item.description.length >
                                    10
                                ) {
                                    "${item.description.take(10)}..."
                                } else {
                                    item.description
                                },
                            fontSize = 12.sp,
                        )
                        Text(
                            text =
                                item.dueDate?.let {
                                    SimpleDateFormat("MMM dd, yyyy HH:mm:ss", Locale.getDefault()).format(it)
                                } ?: "",
                            fontSize = 12.sp,
                        )
                        Text(
                            text =
                                item.createdOn?.let {
                                    SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(it)
                                } ?: "",
                            fontSize = 12.sp,
                        )
                    }
                    IconButton(onClick = { navController.navigate(Screen.ItemEditScreen.route + "/${item.id}") }) {
                        Icon(
                            imageVector = Icons.Rounded.Edit,
                            contentDescription = "Edit TODO",
                        )
                    }
                    IconButton(onClick = {
                        onEvent(ItemEvent.DeleteItem(item))
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete TODO",
                        )
                    }
                }
            }
        }
    }
}
