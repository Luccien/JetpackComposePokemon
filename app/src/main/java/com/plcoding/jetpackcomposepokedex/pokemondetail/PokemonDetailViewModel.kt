package com.plcoding.jetpackcomposepokedex.pokemondetail

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.jetpackcomposepokedex.domain.model.PokemonDomainModel
import com.plcoding.jetpackcomposepokedex.interactors.pokemon.GetPokemon
import com.plcoding.jetpackcomposepokedex.network.remote.responses.Pokemon
import com.plcoding.jetpackcomposepokedex.repository.PokemonRepository
import com.plcoding.jetpackcomposepokedex.util.Constants.TAG
import com.plcoding.jetpackcomposepokedex.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val getPokemon: GetPokemon,
    private val repository: PokemonRepository
) : ViewModel() {


    val pokemon: MutableState<PokemonDomainModel?> = mutableStateOf(null)

    val loading = mutableStateOf(false)

    val onLoad: MutableState<Boolean> = mutableStateOf(false)

    // TODO remove repository here
    suspend fun getPokemonInfo(pokemonName: String): Resource<Pokemon> {
        return repository.getPokemonInfo(pokemonName)
    }


    fun onTriggerEvent(pokemonName: String) {
        viewModelScope.launch {
            getPokemon(pokemonName)
        }
    }

     private fun getPokemon(pokemonName: String){

            getPokemon.execute(pokemonName).onEach{ dataState ->

                loading.value = dataState.loading


                dataState.data?.let { data ->
                    pokemon.value = data
                }

                dataState.error?.let { error ->
                    Log.e(TAG, "getPokemon: ${error}")
                }

            }.launchIn(viewModelScope)
    }


}