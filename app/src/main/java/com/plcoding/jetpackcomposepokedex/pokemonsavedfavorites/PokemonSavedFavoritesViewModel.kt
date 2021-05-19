package com.plcoding.jetpackcomposepokedex.pokemonsavedfavorites

import androidx.lifecycle.ViewModel
import com.plcoding.jetpackcomposepokedex.data.remote.responses.Pokemon
import com.plcoding.jetpackcomposepokedex.repository.PokemonRepository
import com.plcoding.jetpackcomposepokedex.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject



    @HiltViewModel
    class PokemonSavedFavoritesViewModel @Inject constructor(
        private val repository: PokemonRepository
    ) : ViewModel() {

        suspend fun getPokemonInfo(pokemonName: String): Resource<Pokemon> {
            return repository.getPokemonInfo(pokemonName)
        }
    }
