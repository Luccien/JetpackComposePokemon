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

/*
 Avoiding Crashes:
 A crash which occures when user is loading content: a pokemon for the first time but he does not have a internetconnection.
 Within a try and catch blog whitin a flow (GetPokemon.kt )  throw Exeption() -> which leads to the flow beeing cancelled and in
 this case the app beeing crashed
  Further info about it: https://medium.com/@chibatching/avoiding-to-crash-caused-by-misunderstanding-kotlin-coroutine-scope-b38ff5cbef20
    SOLUTION/FIX --> Here just emit(DataState.error("message")) will work to inform the user what is wrong // throw Exception("message") causing the crash is not not necessary
 */
class GetPokemon (
    private val pokemonDao: PokemonDao,
    private val entityMapper: PokemonEntityMapper,
    private val pokeApi: PokeApi,
    private val pokemonDtoMapper: PokemonDtoMapper,
        ) {


    fun execute(
        pokemonName: String,
        isNetworkAvailable: Boolean
    ): Flow<DataState<PokemonDomainModel>> = flow {

        try {
            emit(DataState.loading())

            var pokemon = getPokemonFromCache(pokemonName = pokemonName)

            if(pokemon != null){
                emit(DataState.success(pokemon))
            }
            else {
                if (isNetworkAvailable) {
                    val networkPokemon = getPokemonFromNetwork(pokemonName) // dto -> domain


                    // insert into cache
                    pokemonDao.insertPokemon(
                        // map domain -> entity
                        entityMapper.mapFromDomainModel(networkPokemon)
                    )
                }



                // 1 get from cache // trying to get it again from cache (as in beginning of this try block ) ...
                // 2 even if   isNetworkAvailable=false (will be called twice) --> reason is to keep it consistent
                // 3 ( single source of truth (SSOT) )--> same call does not matter what happend before and how data was retrieved(1.network or 2. cache or 3.both sources doesnot work and there is no data)
                pokemon = getPokemonFromCache(pokemonName = pokemonName)
                /// ---

                // emit and finish
                if (pokemon != null) {
                    emit(DataState.success(pokemon))
                }
                else {

                    //( will capture when networking is off as in catch blog and also when local database is not working properly)
                   emit(DataState.error<PokemonDomainModel>("Unable to get Pokemon from the cache/internet."))

                    // DO NOT USE HERE // Exeptions if executed here will stop the flow and crash the app: checkout by turning internetconnection off
                    //  throw Exception("Unable to get Pokemon from the cache.")

                }
            }


        }

     catch (e: Exception)
    {
        emit(DataState.error<PokemonDomainModel>(e.message ?: "Unknown Error")) // (will most likely tell that networking is switched off)
        //throw Exception("Unable to get Pokemon: network not available") // DO NOT USE HERE // Exeptions if executed here will stop the flow and crash the app: checkout by turning internetconnection off
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

