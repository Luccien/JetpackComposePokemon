package com.plcoding.jetpackcomposepokedex.cache

import com.plcoding.jetpackcomposepokedex.cache.model.PokemonEntity

class PokemonDaoFake (
    private val appDatabaseFake: AppDatabaseFake
        ): PokemonDao {
    override suspend fun insertPokemon(pokemon: PokemonEntity): Long {
        appDatabaseFake.pokemons.add(pokemon)
        return 1 // return success
    }

    override suspend fun insertPokemons(pokemon: List<PokemonEntity>): LongArray {
        appDatabaseFake.pokemons.addAll(pokemon)
        return longArrayOf(1) // return success
    }

    override suspend fun getPokemonByName(name: String): PokemonEntity? {
        return appDatabaseFake.pokemons.find { it.name == name }
    }

    override suspend fun deletePokemons(ids: List<Int>): Int {
        appDatabaseFake.pokemons.removeIf { it.id in ids }
        return 1 // return success
    }

    override suspend fun deleteAllPokemons() {
        appDatabaseFake.pokemons.clear()
    }

}