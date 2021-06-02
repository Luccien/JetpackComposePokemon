package com.plcoding.jetpackcomposepokedex.interactors.pokemon

import com.plcoding.jetpackcomposepokedex.cache.PokemonDao
import com.plcoding.jetpackcomposepokedex.cache.model.PokemonEntityMapper
import com.plcoding.jetpackcomposepokedex.network.models.PokemonDtoMapper
import com.plcoding.jetpackcomposepokedex.network.remote.PokeApi

class GetPokemon (
    private val pokemonDao: PokemonDao,
    private val entityMapper: PokemonEntityMapper,
    private val pokemonService: PokeApi,
    private val pokemonDtoMapper: PokemonDtoMapper,
        ) {
}