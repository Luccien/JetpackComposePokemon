package com.plcoding.jetpackcomposepokedex.cache

import com.plcoding.jetpackcomposepokedex.cache.model.PokedexListEntryEntity

class PokemonDaoFake (
    private val appDatabaseFake: AppDatabaseFake
        ): PokedexListEntryDao {
    override suspend fun insertPokemonListEntry(pokedexListEntry: PokedexListEntryEntity): Long {
        appDatabaseFake.pokemons.add(pokedexListEntry)
        return 1 // return success
    }

    override suspend fun insertPokemonList(pokedexList: List<PokedexListEntryEntity>): LongArray {
        appDatabaseFake.pokemons.addAll(pokedexList)
        return longArrayOf(1) // return success
    }

    override suspend fun getCompletePokedexList(): List<PokedexListEntryEntity> {
        return appDatabaseFake.pokemons
    }

}