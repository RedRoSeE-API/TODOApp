package com.example.todoapp.nav

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.todoapp.dao.ItemDao
import com.example.todoapp.event.ItemEvent
import com.example.todoapp.screens.ItemDetailScreen
import com.example.todoapp.screens.ItemEditScreen
import com.example.todoapp.screens.ItemScreen
import com.example.todoapp.shared.ItemState

@Composable
fun Navigation(
    state: ItemState,
    onEvent: (ItemEvent) -> Unit,
    dao: ItemDao,
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.ItemScreen.route,
        modifier =
            Modifier
                .fillMaxSize()
                .systemBarsPadding(),
    ) {
        composable(route = Screen.ItemScreen.route) {
            ItemScreen(
                navController = navController,
                state = state,
                onEvent = onEvent,
            )
        }
        composable(
            route = Screen.ItemViewScreen.route + "/{itemId}",
            arguments = listOf(navArgument("itemId") { type = NavType.IntType }),
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getInt("itemId") ?: return@composable
            val item by dao.getItemById(itemId).collectAsState(initial = null)

            ItemDetailScreen(item = item, navController = navController)
        }

        composable(
            route = Screen.ItemEditScreen.route + "/{itemId}",
            arguments = listOf(navArgument("itemId") { type = NavType.IntType }),
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getInt("itemId") ?: return@composable
            val item by dao.getItemById(itemId).collectAsState(initial = null)

            ItemEditScreen(
                item = item,
                navController = navController,
                onSave = { editedTodo ->
                    onEvent(ItemEvent.UpdateItem(editedTodo))
                },
            )
        }
    }
}
