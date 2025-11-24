package com.example.todoapp.nav

sealed class Screen(
    val route: String,
) {
    object ItemScreen : Screen("item_main_screen")

    object ItemViewScreen : Screen("item_detail_screen")

    object ItemEditScreen : Screen("item_edit_screen")
}
