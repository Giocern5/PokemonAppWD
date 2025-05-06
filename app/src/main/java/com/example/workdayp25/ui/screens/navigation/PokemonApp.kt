package com.example.workdayp25.ui.screens.navigation

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.composable
import com.example.workdayp25.ui.screens.PokemonDetailScreen
import com.example.workdayp25.ui.screens.PokemonListScreen
import com.example.workdayp25.ui.viewmodel.PokemonViewModel

@Composable
fun PokemonApp() {
    val navController = rememberNavController()
    // Used for shared viewmodel
    val viewModel =  hiltViewModel<PokemonViewModel>()

    NavHost(navController = navController, startDestination = Screen.PokemonList.route,
        modifier = Modifier.background(Color.DarkGray)
    ) {
        composable(route = Screen.PokemonList.route) {
            PokemonListScreen(navController, viewModel)
        }
        composable(route = Screen.PokemonDetail.route) {
            PokemonDetailScreen(navController, viewModel)
        }
    }
}