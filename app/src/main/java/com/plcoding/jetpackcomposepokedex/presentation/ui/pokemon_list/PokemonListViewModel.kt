package com.plcoding.jetpackcomposepokedex.presentation.ui.pokemon_list

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.plcoding.jetpackcomposepokedex.domain.model.PokedexListEntryDomainModel
import com.plcoding.jetpackcomposepokedex.interactors.pokemon.GetPokemonListEntries
import com.plcoding.jetpackcomposepokedex.presentation.ui.util.DialogQueue
import com.plcoding.jetpackcomposepokedex.presentation.util.ConnectivityManager
import com.plcoding.jetpackcomposepokedex.util.Constants
import com.plcoding.jetpackcomposepokedex.util.Constants.PAGE_SIZE
import com.plcoding.jetpackcomposepokedex.util.Constants.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject




@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val getPokemonListEntries: GetPokemonListEntries,
    private val connectivityManager: ConnectivityManager,
    private val savedStateHandle: SavedStateHandle,

    ) : ViewModel() {

    private var curPage = 0

    // pokemonList is used in  searchPokemonList(query: String)      and in loadPokemonPaginated() // a searchQuery will just query already loaded pokemonListEntries
    var pokemonList = mutableStateOf<List<PokedexListEntryDomainModel>>(listOf())
    val query = mutableStateOf("")
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false) // TODO check its not used -> not switched to true


    // cached  means cached in viewmodel as long as this viewmdel lives // cachedPokemonlist will be used to display an empty search
    private var cachedPokemonList = listOf<PokedexListEntryDomainModel>()
    private var isSearchStarting = true
    var isSearching = mutableStateOf(false)

    val dialogQueue = DialogQueue()

    init {
        onTriggerEvent(PokemonListEvent.NextPageEvent,true)
    }


    fun onQueryChanged(query: String){
        setQuery(query)
    }

    private fun setQuery(query: String){
        this.query.value = query
    }

    fun searchPokemonList() {
        Log.d(TAG, "newPokemonList: query: ${query.value}")
        var quer = query.value
        val listToSearch = if(isSearchStarting) {
            pokemonList.value
        } else {
            cachedPokemonList
        }
        viewModelScope.launch(Dispatchers.Default) {
            if(quer.isEmpty()) {
                pokemonList.value = cachedPokemonList
                isSearching.value = false
                isSearchStarting = true
                return@launch
            }
            val results = listToSearch.filter {
                it.pokemonName.contains(quer.trim(), ignoreCase = true) ||
                        it.number.toString() == quer.trim()
            }
            if(isSearchStarting) {
                cachedPokemonList = pokemonList.value
                isSearchStarting = false
            }
            pokemonList.value = results
            isSearching.value = true
        }
    }




    fun onTriggerEvent(event: PokemonListEvent,onAppStart:Boolean = false){
        viewModelScope.launch {
            try {
                when(event){
                    is PokemonListEvent.NewSearchEvent -> {
                        searchPokemonList()
                    }
                    is PokemonListEvent.NextPageEvent -> {
                        nextPage(onAppStart)
                    }
                    is PokemonListEvent.RestoreStateEvent -> {
                        //TODO //restoreState()
                    }
                }
            }catch (e: Exception){
                Log.e(TAG, "launchJob: Exception: ${e}, ${e.cause}")
                e.printStackTrace()
            }
            finally {
                Log.d(TAG, "launchJob: finally called.")
            }
        }
    }



    private fun nextPage(onAppStart:Boolean = false){
        getPokemonListEntries.execute(onAppStart,pokemonList.value,PAGE_SIZE, curPage * PAGE_SIZE,connectivityManager.isNetworkAvailable.value).onEach{ dataState ->


            isLoading.value = dataState.loading

            dataState.data?.let { data ->

                curPage++
                loadError.value = ""
                isLoading.value = false

                pokemonList.value = data
            }

            dataState.error?.let { error ->
                Log.e(Constants.TAG, "getPokemon: ${error}")
                loadError.value = error
                isLoading.value = false
                dialogQueue.appendErrorMessage("An Error Occurred", error)
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