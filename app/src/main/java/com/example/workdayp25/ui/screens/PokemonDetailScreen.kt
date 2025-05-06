package com.example.workdayp25.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import coil.compose.AsyncImage
import com.example.workdayp25.data.model.PokemonDetails
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.example.workdayp25.R
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.workdayp25.data.model.Types
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import com.example.workdayp25.data.model.Sprites
import com.example.workdayp25.ui.viewmodel.PokemonViewModel

@Composable
fun PokemonDetailScreen(navController: NavHostController, viewModel: PokemonViewModel) {

    val pokemonDetails by viewModel.pokemonDetails.collectAsState()

    // Handle back press to clear details and go back
    BackHandler {
        navController.popBackStack()
        viewModel.clearPokemonDetails()
    }

    // Screen set up
    Column( modifier = Modifier.fillMaxSize()) {
        when {
            pokemonDetails != null -> {
                pokemonDetails?.let {
                    PokemonImage( name = it.name,
                        url = it.sprites.other.home.front_default)
                    PokemonAboutSection(pokemonDetails)
                }
            }
            else ->{
                // Error screen would go here, but since we are clearing,it shows on back track. quick fix
            }
        }
    }

}

@Composable
fun PokemonImage(name: String?, url: String?) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Gray)
            .height(270.dp)
    ) {
        AsyncImage(
            model = url,
            contentDescription = "Image for $name",
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun PokemonAboutSection(pokemonDetails: PokemonDetails?) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding( horizontal = 16.dp )
        .verticalScroll( rememberScrollState() )) {

        pokemonDetails?.let { details ->
            Text(text = details.name, color = Color.White, fontSize = 40.sp, fontWeight = FontWeight.Bold)

            DividerLine()

            DescriptionCell(description = stringResource(id = R.string.id),
                value = details.id)
            DescriptionCell(description = stringResource(id = R.string.height),
                value = stringResource(R.string.heightValue ,details.height))
            DescriptionCell(description = stringResource(id = R.string.weight),
                value =  stringResource(R.string.weightValue ,details.weight))
            TypesCell( types = details.types)
            details.stats.let { stats ->
                stats.forEach{ stat ->
                    DescriptionCell(description = stat.stat.name, value =  stat.base_stat) }
            }
            Text(text = stringResource(id = R.string.sprites), color = Color.White, fontSize = 40.sp, fontWeight = FontWeight.Bold)

            DividerLine()

            SpritesCell(details.name, details.sprites)
        }

    }
}

@Composable
fun SpritesCell(name: String, sprites: Sprites) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        listOf(
            sprites.front_default,
            sprites.back_default,
            sprites.front_shiny,
            sprites.back_shiny
        ).forEach { imageUrl ->
            AsyncImage(
                model = imageUrl,
                contentDescription = "Image for $name",
                modifier = Modifier.size(90.dp)
            )
        }
    }
}

@Composable
fun DividerLine() {
    HorizontalDivider(
        modifier = Modifier
            .fillMaxWidth()
            .paddingFromBaseline( bottom = 8.dp ),
        thickness = 2.dp,
        color = Color.White
    )
}

@Composable
fun DescriptionCell(description: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(text = description, color = Color.White, fontSize = 22.sp)
        Text(text = value,  color = Color.White, fontSize = 22.sp)
    }
}

@Composable
fun TypesCell(types: List<Types>) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(text = stringResource(id = R.string.types), color = Color.White, fontSize = 22.sp)

        Spacer(modifier = Modifier.weight(1f))

        types.forEach { type ->
            Text(
                text = " ${type.type.name}",
                color = Color.White,
                fontSize = 22.sp,
            )
        }
    }
}