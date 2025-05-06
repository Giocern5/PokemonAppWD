package com.example.workdayp25.data.repository

import androidx.paging.PagingData
import com.example.workdayp25.data.model.PokemonDetails
import com.example.workdayp25.data.model.PokemonQuery
import kotlinx.coroutines.flow.Flow
import com.example.workdayp25.data.model.Result

interface PokemonRepository {
    fun getPokemonList(): Flow<PagingData<PokemonQuery>>
    suspend fun getPokemonDetails(name: String): Result<PokemonDetails>
}