package com.plcoding.jetpackcomposepokedex.cache

import com.plcoding.jetpackcomposepokedex.cache.model.PokedexListEntryEntity
import com.plcoding.jetpackcomposepokedex.domain.model.PokedexListEntryDomainModel

class AppDatabaseFake {
    // fake for pokemon table in local db
    val pokemons = mutableListOf<PokedexListEntryEntity>()

}