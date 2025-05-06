package com.example.workdayp25.data.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import coil.request.Tags
import com.example.workdayp25.data.model.PokemonDetails
import com.example.workdayp25.data.model.PokemonQuery
import com.example.workdayp25.data.paging.PokemonListPagingSource
import com.example.workdayp25.network.PokemonService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import java.io.IOException
import com.example.workdayp25.data.model.Result

private const val pageSize = 4 // pageSize * 3 = results shown

class PokemonRepositoryImpl @Inject constructor(private val service: PokemonService) : PokemonRepository {

    companion object {
        const val TAG =  "PokemonRepositoryImpl"
    }

    override fun getPokemonList(): Flow<PagingData<PokemonQuery>> {
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = { PokemonListPagingSource(service) }
        ).flow
    }

    override suspend fun getPokemonDetails(name: String): Result<PokemonDetails> {
        return try {
            val response = service.getPokemonDetails(name.lowercase())
            if (response.isSuccessful) {
                val details = response.body()
                if (details != null) {
                    Log.e(TAG, "Fetching Details")
                    Result.Success(details)
                } else {
                    Result.Error(Exception("Empty response"))
                }
            } else {
                Result.Error(Exception("Enter Valid Pokemon!"))
            }
        } catch (e: IOException) {
            Log.e(TAG, e.message.toString())
            Result.Error(e)
        }
    }
}