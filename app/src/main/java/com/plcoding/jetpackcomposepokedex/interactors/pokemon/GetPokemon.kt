package com.plcoding.jetpackcomposepokedex.interactors.pokemon

import com.plcoding.jetpackcomposepokedex.cache.PokemonDao
import com.plcoding.jetpackcomposepokedex.cache.model.PokemonEntityMapper
import com.plcoding.jetpackcomposepokedex.domain.data.DataState
import com.plcoding.jetpackcomposepokedex.domain.model.PokemonDomainModel
import com.plcoding.jetpackcomposepokedex.network.models.PokemonDtoMapper
import com.plcoding.jetpackcomposepokedex.network.remote.PokeApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetPokemon (
   // private val pokemonDao: PokemonDao,
    //private val entityMapper: PokemonEntityMapper,
    private val pokeApi: PokeApi,
    private val pokemonDtoMapper: PokemonDtoMapper,
        ) {


    fun execute(
        pokemonName: String
    ): Flow<DataState<PokemonDomainModel>> = flow {

        try {
            emit(DataState.loading())

            // just to show loading, cache is fast
            delay(1000)


            val networkPokemon = getPokemonFromNetwork(pokemonName) // dto -> domain
            // TODO rename in pokemoninfo
            var pokemon = networkPokemon

            // emit and finish
            if(pokemon != null){
                emit(DataState.success(pokemon))
            }
            else{
                throw Exception("Unable to get recipe from the cache.")
            }
        }

     catch (e: Exception)
    {
        emit(DataState.error<PokemonDomainModel>(e.message ?: "Unknown Error"))
    }
}


    private suspend fun getPokemonFromNetwork(pokemonName: String): PokemonDomainModel {
        return pokemonDtoMapper.mapToDomainModel(pokeApi.getPokemonInfo(pokemonName))
    }

}

