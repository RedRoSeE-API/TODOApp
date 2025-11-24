package com.example.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.todoapp.nav.Navigation
import com.example.todoapp.theme.MyApplicationTheme
import com.example.todoapp.viewmodel.ItemMainViewModel

class MainActivity : ComponentActivity() {
    val db by lazy {
        Room
            .databaseBuilder(
                applicationContext,
                ItemDatabase::class.java,
                "items.db",
            ).fallbackToDestructiveMigration()
            .build()
    }

    private val viewModel by viewModels<ItemMainViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T = ItemMainViewModel(db.dao) as T
            }
        },
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        WindowInsetsControllerCompat(window, window.decorView).apply {
            hide(WindowInsetsCompat.Type.navigationBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme(window = window) {
                val state = viewModel.state.collectAsState()
                Navigation(
                    state = state.value,
                    onEvent = viewModel::onEvent,
                    dao = db.dao,
                )
            }
        }
    }
}
