package com.plcoding.jetpackcomposepokedex.interactors.pokemon


import com.plcoding.jetpackcomposepokedex.domain.data.DataState
import com.plcoding.jetpackcomposepokedex.domain.model.PokedexListEntryDomainModel
import com.plcoding.jetpackcomposepokedex.domain.model.PokemonDomainModel
import com.plcoding.jetpackcomposepokedex.network.models.PokedexListEntryDtoMapper
import com.plcoding.jetpackcomposepokedex.network.models.PokemonDtoMapper
import com.plcoding.jetpackcomposepokedex.network.remote.PokeApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow



/// TODO overNetwork umbennen :: aufruf beim erstenmal mit parameter // firstCall=true // ansonsten false -->
class GetPokemonListEntriesOverNetwork(
    private val pokedexListEntryDtoMapper: PokedexListEntryDtoMapper,
    private val pokeApi: PokeApi)
{

    fun execute(
        pokemonName: String
    ): Flow<DataState<PokemonDomainModel>> = flow {

        try {
            emit(DataState.loading())

            // just to show loading, cache is fast
            delay(1000)


            // var pokemon = getPokemonFromCache(pokemonName = pokemonName)

            //if(pokemon != null){
            //  emit(DataState.success(pokemon))
            //}
            //else{
            //JJJJJJJJJJJJJJJJJJJ val networkPokemon = getPokemonFromNetwork(pokemonName) // dto -> domain

            // testing delete later!!!
            // JJJJJJJJJJJJJJJ  emit(DataState.success(networkPokemon))
            // TESTING !!

            /*
                // insert into cache
                pokemonDao.insertPokemon(
                    // map domain -> entity
                    entityMapper.mapFromDomainModel(networkPokemon)
                )

                 */

            // }


            // get from cache
            //pokemon = getPokemonFromCache(pokemonName = pokemonName)

            // emit and finish
            /*
            if(pokemon != null){
                emit(DataState.success(pokemon))
            }


            else{
                throw Exception("Unable to get recipe from the cache.")
            }
*/


        }

        catch (e: Exception)
        {
            emit(DataState.error<PokemonDomainModel>(e.message ?: "Unknown Error"))
        }
    }

    private suspend fun getPokemonFromNetwork(limit: Int,offset: Int){ ///jjjjjjjjjjjjjjj : PokedexListEntryDomainModel {
       //---JJJJJJJJJJJJJJJJJJ return pokedexListEntryDtoMapper.mapToDomainModel(pokeApi.getPokemonList(limit,offset))
    }




}