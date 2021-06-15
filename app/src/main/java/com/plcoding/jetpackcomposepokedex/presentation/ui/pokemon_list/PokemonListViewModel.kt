package com.plcoding.jetpackcomposepokedex.presentation.ui.pokemon_list

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.plcoding.jetpackcomposepokedex.domain.model.PokedexListEntryDomainModel
import com.plcoding.jetpackcomposepokedex.interactors.pokemon.GetPokemonListEntries
import com.plcoding.jetpackcomposepokedex.util.Constants
import com.plcoding.jetpackcomposepokedex.util.Constants.PAGE_SIZE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val getPokemonListEntries: GetPokemonListEntries
) : ViewModel() {

    private var curPage = 0

    // pokemonList is used in  searchPokemonList(query: String)      and in loadPokemonPaginated() // a searchQuery will just query already loaded pokemonListEntries
    var pokemonList = mutableStateOf<List<PokedexListEntryDomainModel>>(listOf())

    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)

    private var cachedPokemonList = listOf<PokedexListEntryDomainModel>()
    private var isSearchStarting = true
    var isSearching = mutableStateOf(false)

    init {
        loadPokemonPaginated(true)
    }

    fun searchPokemonList(query: String) {
        val listToSearch = if(isSearchStarting) {
            pokemonList.value
        } else {
            cachedPokemonList
        }
        viewModelScope.launch(Dispatchers.Default) {
            if(query.isEmpty()) {
                pokemonList.value = cachedPokemonList
                isSearching.value = false
                isSearchStarting = true
                return@launch
            }
            val results = listToSearch.filter {
                it.pokemonName.contains(query.trim(), ignoreCase = true) ||
                        it.number.toString() == query.trim()
            }
            if(isSearchStarting) {
                cachedPokemonList = pokemonList.value
                isSearchStarting = false
            }
            pokemonList.value = results
            isSearching.value = true
        }
    }







    fun loadPokemonPaginated(onAppStart:Boolean = false) {
        onTriggerEvent(onAppStart)
    }
    fun onTriggerEvent(onAppStart:Boolean = false) {
        viewModelScope.launch {
            getPokemonListEntries(onAppStart)
        }
    }

    private fun getPokemonListEntries(onAppStart:Boolean = false){
       // isLoading.value = true // old loading state
        getPokemonListEntries.execute(onAppStart,pokemonList.value,PAGE_SIZE, curPage * PAGE_SIZE).onEach{ dataState ->

            isLoading.value = dataState.loading // new loading state

            dataState.data?.let { data ->

                curPage++
                loadError.value = ""
                isLoading.value = false

                // changed --> list will now be retrieved from db as complete list  // old version was to add only newly retrieved data  //pokemonList.value += data
                pokemonList.value = data
            }

            dataState.error?.let { error ->
                Log.e(Constants.TAG, "getPokemon: ${error}")
                loadError.value = error
                isLoading.value = false
            }

        }.launchIn(viewModelScope)
    }





    fun calcDominantColor(drawable: Drawable, onFinish: (Color) -> Unit) {
        val bmp = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)

        Palette.from(bmp).generate { palette ->
            palette?.dominantSwatch?.rgb?.let { colorValue ->
                onFinish(Color(colorValue))
            }
        }
    }



}