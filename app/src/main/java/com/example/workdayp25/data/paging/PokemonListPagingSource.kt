package com.example.workdayp25.data.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.workdayp25.data.model.PokemonQuery
import com.example.workdayp25.network.PokemonService
import javax.inject.Inject
import okio.IOException

class PokemonListPagingSource @Inject constructor (
    private val service: PokemonService
): PagingSource<Int, PokemonQuery>() {

    companion object {
        const val TAG = "PokemonListPagingSource"
    }

    // bug with double load on swipe to refresh
    override fun getRefreshKey(state: PagingState<Int, PokemonQuery>): Int? {
        return null // Always restart at offset 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PokemonQuery> {
        return try {
            val offset = params.key ?: 0
            val pageSize = params.loadSize

            val response = service.getPokemonList(limit = pageSize, offset = offset)

            if (response.isSuccessful) {
                val pokemon = response.body()?.results.orEmpty()
                Log.e("Paging Source", "Loaded ${pokemon.size} items at offset $offset")

                LoadResult.Page(
                    data = pokemon,
                    prevKey = if (offset <= 0) null else maxOf(0, offset - pageSize),
                    nextKey = if (pokemon.size < pageSize) null else offset + pageSize
                )
            } else {
                Log.e(TAG, "Error body: ${response.errorBody()}")
                LoadResult.Error(Exception("API error: ${response.message()}"))
            }

        } catch (e: IOException) {
            Log.e(TAG,  e.message.toString())
            LoadResult.Error(e)
        }
    }
}