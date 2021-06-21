package com.plcoding.jetpackcomposepokedex.presentation.ui.pokemon_detail

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.jetpackcomposepokedex.domain.model.PokemonDomainModel
import com.plcoding.jetpackcomposepokedex.interactors.pokemon.GetPokemon
import com.plcoding.jetpackcomposepokedex.network.remote.responses.Sprites
import com.plcoding.jetpackcomposepokedex.presentation.ui.util.DialogQueue
import com.plcoding.jetpackcomposepokedex.presentation.util.ConnectivityManager
import com.plcoding.jetpackcomposepokedex.util.Constants.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val getPokemon: GetPokemon,
    private val connectivityManager: ConnectivityManager
) : ViewModel() {


    val pokemon: MutableState<PokemonDomainModel?> = mutableStateOf(null)

    val loading = mutableStateOf(false)

    val onLoad: MutableState<Boolean> = mutableStateOf(false)

    val dialogQueue = DialogQueue()

    fun onTriggerEvent(event:PokemonEvent){//pokemonName: String) {
        viewModelScope.launch {
             ///
            try {
                when(event){
                    is PokemonEvent.GetPokemonEvent -> {
                        if(pokemon.value == null){
                            getPokemon(event.name)
                        }
                    }
                }
            }catch (e: Exception){
                Log.e(TAG, "launchJob: Exception: ${e}, ${e.cause}")
                e.printStackTrace()
            }
            ///
        }
    }

     private fun getPokemon(pokemonName: String){
            getPokemon.execute(pokemonName,connectivityManager.isNetworkAvailable.value).onEach{ dataState ->

                loading.value = dataState.loading

                dataState.data?.let { data ->
                    pokemon.value = data
                }

                dataState.error?.let { error ->
                    Log.e(TAG, "getPokemon: ${error}")
                    dialogQueue.appendErrorMessage("An Error Occurred", error)

                }

            }.launchIn(viewModelScope)
    }


}