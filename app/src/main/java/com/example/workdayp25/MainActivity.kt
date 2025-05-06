package com.example.workdayp25

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.workdayp25.ui.screens.navigation.PokemonApp
import com.example.workdayp25.ui.theme.WorkdayP25Theme
import dagger.hilt.android.AndroidEntryPoint

// == using paging instead of manual implemention
// fix data mapping
// fix paging
// error still showing

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WorkdayP25Theme {
                PokemonApp()
            }
        }
    }
}
