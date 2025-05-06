package com.example.workdayp25.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.workdayp25.ui.viewmodel.PokemonViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.workdayp25.data.model.PokemonQuery
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.workdayp25.R
import com.example.workdayp25.ui.components.ErrorMessage
import com.example.workdayp25.ui.components.LoadingIndicator
import com.example.workdayp25.ui.screens.navigation.Screen

@Composable
fun PokemonListScreen(navController: NavHostController, viewModel: PokemonViewModel) {

    val pokemonList = viewModel.pokemonFeedPagingFlow.collectAsLazyPagingItems()
    val refreshState = rememberSwipeRefreshState(isRefreshing = false)
    val pokemonDetails by viewModel.pokemonDetails.collectAsState()
    val context = LocalContext.current

    // Navigating to details page on either search input or item click
    LaunchedEffect(pokemonDetails) {
        if (pokemonDetails != null) {
            navController.navigate(Screen.PokemonDetail.route)
        }
    }

    // Search errors
    LaunchedEffect(Unit) {
        viewModel.errorMessage.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    SwipeRefresh(
        state = refreshState,
        onRefresh = { pokemonList.refresh() },
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Screen set up
        Column(modifier = Modifier.fillMaxSize()) {
            SearchBar(viewModel)
            PokemonList(pokemonList, viewModel)
        }
    }
}

@Composable
fun SearchBar(viewModel: PokemonViewModel) {
    var searchBarText by remember { mutableStateOf("") }

    Row(horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(8.dp)) {

        OutlinedTextField( value = searchBarText,
            onValueChange = { text -> searchBarText = text },
            modifier = Modifier
                .weight(1f)
                .background(Color.Gray))
        Spacer( modifier = Modifier.width(8.dp) )
        Button( onClick = {
            if(searchBarText.isNotEmpty() ) {
                viewModel.setPokemonDetails(searchBarText)
            }
        },
            colors = ButtonDefaults.buttonColors(Color.Gray),
            modifier = Modifier.height(45.dp)) {
            Text( text = "Search", color = Color.White )
        }
    }
}

@Composable
fun PokemonList(pokemonList: LazyPagingItems<PokemonQuery>, viewModel: PokemonViewModel) {

    when (pokemonList.loadState.refresh) {
        is LoadState.Error -> {
            ErrorMessage( onRetry = { pokemonList.retry() } )
        }
        is LoadState.Loading -> {
            LoadingIndicator()
        }
        is LoadState.NotLoading -> {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize()
            ) {
                items(pokemonList.itemCount) { index ->
                    pokemonList[index]?.let {
                        PokemonItem(it, viewModel)
                    }
                }

                if (pokemonList.loadState.append is LoadState.Loading) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        LoadingIndicator(modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun PokemonItem(pokemon: PokemonQuery, viewModel: PokemonViewModel) {

    Column(
        modifier = Modifier
            .padding(8.dp)
            .background(Color.Gray)
            .clickable {
                viewModel.setPokemonDetails(pokemon.name)
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.poke_ball),
            contentDescription = pokemon.name,
            modifier = Modifier.size(170.dp)
        )
        Text(
            text = pokemon.name,
            modifier = Modifier.padding(vertical = 8.dp),
            fontSize = 20.sp,
            color = Color.White,
            maxLines = 2,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
    }

}
