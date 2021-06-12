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
    private val pokemonDao: PokemonDao,
    private val entityMapper: PokemonEntityMapper,
    private val pokeApi: PokeApi,
    private val pokemonDtoMapper: PokemonDtoMapper,
        ) {


    fun execute(
        pokemonName: String
    ): Flow<DataState<PokemonDomainModel>> = flow {

        try {
            emit(DataState.loading())


            //delay(1000)


            var pokemon = getPokemonFromCache(pokemonName = pokemonName)

            if(pokemon != null){
                emit(DataState.success(pokemon))
            }
            else{
                val networkPokemon = getPokemonFromNetwork(pokemonName) // dto -> domain


                // insert into cache
                pokemonDao.insertPokemon(
                    // map domain -> entity
                    entityMapper.mapFromDomainModel(networkPokemon)
                )

            }




            // get from cache
            pokemon = getPokemonFromCache(pokemonName = pokemonName)

            // emit and finish
            if(pokemon != null){
                emit(DataState.success(pokemon))
            }
            else{
                throw Exception("Unable to get Pokemon from the cache.")
            }



        }

     catch (e: Exception)
    {
        emit(DataState.error<PokemonDomainModel>(e.message ?: "Unknown Error"))
    }
}


    private suspend fun getPokemonFromCache(pokemonName: String): PokemonDomainModel? {
        return pokemonDao.getPokemonByName(pokemonName)?.let {  pokemonEntity ->
            entityMapper.mapToDomainModel(pokemonEntity)
        }
    }


    private suspend fun getPokemonFromNetwork(pokemonName: String): PokemonDomainModel {
        return pokemonDtoMapper.mapToDomainModel(pokeApi.getPokemonInfo(pokemonName))
    }

}

