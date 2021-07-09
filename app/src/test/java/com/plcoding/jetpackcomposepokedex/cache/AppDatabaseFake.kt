package com.plcoding.jetpackcomposepokedex.cache

import com.plcoding.jetpackcomposepokedex.cache.model.PokemonEntity

class AppDatabaseFake {
    // fake for pokemon table in local db
    val pokemons = mutableListOf<PokemonEntity>()

}