package com.example.workdayp25.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.workdayp25.data.model.PokemonDetails
import com.example.workdayp25.data.model.PokemonQuery
import com.example.workdayp25.data.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.workdayp25.data.model.Result
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

@HiltViewModel
class PokemonViewModel @Inject constructor( private val repo: PokemonRepository ) : ViewModel() {

    companion object {
        const val TAG = "PokemonViewModel"
    }

    val pokemonFeedPagingFlow: Flow<PagingData<PokemonQuery>> =
        repo.getPokemonList()
            .cachedIn(viewModelScope)


    private val _pokemonDetails = MutableStateFlow<PokemonDetails?>(null)
    val pokemonDetails = _pokemonDetails.asStateFlow()

    private val _errorMessage = MutableSharedFlow<String>() // emits error messages
    val errorMessage = _errorMessage.asSharedFlow()

    fun setPokemonDetails(name: String) {
        viewModelScope.launch (Dispatchers.IO) {
            when (val result = repo.getPokemonDetails(name)) {
                is Result.Success -> {
                    Log.e(TAG, result.data.toString())
                    _pokemonDetails.value = result.data
                }
                is Result.Error -> {
                    _errorMessage.emit(result.exception.message.toString())
                    Log.e(TAG, result.exception.message.toString())
                }
            }
        }
    }

    fun clearPokemonDetails() {
        _pokemonDetails.value = null
    }

}
