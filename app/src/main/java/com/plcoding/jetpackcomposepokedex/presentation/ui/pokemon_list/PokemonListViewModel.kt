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
import com.plcoding.jetpackcomposepokedex.interactors.pokemon.RestorePokemon
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


const val STATE_KEY_PAGE = "pokedex.state.page.key"
const val STATE_KEY_QUERY = "pokedex.state.query.key"
//const val STATE_KEY_LIST_POSITION = "pokedex.state.query.list_position"


@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val getPokemonListEntries: GetPokemonListEntries,
    private val restorePokemon: RestorePokemon,
    private val connectivityManager: ConnectivityManager,
    private val savedStateHandle: SavedStateHandle,

    ) : ViewModel() {

    private var curPage = 0

    // pokemonList is used in  searchPokemonList(query: String)      and in loadPokemonPaginated() // a searchQuery will just query already loaded pokemonListEntries
    var pokemonList = mutableStateOf<List<PokedexListEntryDomainModel>>(listOf())
   private var cachedPokemonList = listOf<PokedexListEntryDomainModel>()


    val query = mutableStateOf("")
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false) // check its not used -> not switched to true


    // cached  means cached in viewmodel as long as this viewmdel lives // cachedPokemonlist will be used to display an empty search

    private var isSearchStarting = true
    var isSearching = mutableStateOf(false)

    val dialogQueue = DialogQueue()

    // maybe add list scroll position later for restoring after process death
    //var pokemonListScrollPosition = 0


    init {
        savedStateHandle.get<Int>(STATE_KEY_PAGE)?.let { p ->
            setPage(p)
        }
        /* savedStateHandle.get<Int>(STATE_KEY_LIST_POSITION)?.let { p ->
            setListScrollPosition(p)
        }*/
        savedStateHandle.get<String>(STATE_KEY_QUERY)?.let { q ->
            setQuery(q)
        }

        if(curPage != 0 || query.value !="" ){  //(|| pokemonListScrollPosition != 0) // maybe add scrollposition later
                onTriggerEvent(PokemonListEvent.RestoreStateEvent)
        }
        else{
            onTriggerEvent(PokemonListEvent.NextPageEvent,true)
        }
    }


/* // MAYBE ADD LATER
    fun onChangePokemonScrollPosition(position: Int){
        setListScrollPosition(position = position)
    }
    private fun setListScrollPosition(position: Int){
        pokemonListScrollPosition = position
        savedStateHandle.set(STATE_KEY_LIST_POSITION, position)
    }
*/
    private fun incrementPage(){
        setPage(curPage + 1)
    }

    private fun setPage(page: Int){
        this.curPage = page
        savedStateHandle.set(STATE_KEY_PAGE, page)
    }

    fun onQueryChanged(query: String){
        setQuery(query)
    }

    private fun setQuery(query: String){
        this.query.value = query
        savedStateHandle.set(STATE_KEY_QUERY, query)

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
                        restoreState()
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




    private fun restoreState(){
        restorePokemon.execute().onEach{ dataState ->

            isLoading.value = dataState.loading

            dataState.data?.let { data ->
                incrementPage()
                loadError.value = ""
                isLoading.value = false
                pokemonList.value = data
                // trigger a search event ->if app crashed with a not empty search query
                if(query.value != "") {
                    onTriggerEvent(PokemonListEvent.NewSearchEvent)
                }
            }

            dataState.error?.let { error ->
                loadError.value = error
                isLoading.value = false
                dialogQueue.appendErrorMessage("An Error Occurred", error)
            }

        }.launchIn(viewModelScope)
    }





    private fun nextPage(onAppStart:Boolean = false){
        getPokemonListEntries.execute(onAppStart,pokemonList.value,PAGE_SIZE, curPage * PAGE_SIZE,connectivityManager.isNetworkAvailable.value).onEach{ dataState ->

            isLoading.value = dataState.loading

            dataState.data?.let { data ->

                incrementPage()
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