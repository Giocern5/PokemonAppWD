package com.example.workdayp25.ui.screens.navigation

sealed class Screen(val route: String) {
    object PokemonList : Screen("pokemonListScreen")
    object PokemonDetail : Screen("pokemonDetailScreen")
}