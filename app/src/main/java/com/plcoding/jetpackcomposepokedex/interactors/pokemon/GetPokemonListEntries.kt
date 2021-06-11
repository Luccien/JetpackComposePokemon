package com.plcoding.jetpackcomposepokedex.interactors.pokemon


import com.plcoding.jetpackcomposepokedex.domain.data.DataState
import com.plcoding.jetpackcomposepokedex.domain.model.PokedexListEntryDomainModel
import com.plcoding.jetpackcomposepokedex.domain.model.PokemonDomainModel
import com.plcoding.jetpackcomposepokedex.network.models.PokedexListEntryDto
import com.plcoding.jetpackcomposepokedex.network.models.PokedexListEntryDtoMapper
import com.plcoding.jetpackcomposepokedex.network.models.PokemonDtoMapper
import com.plcoding.jetpackcomposepokedex.network.remote.PokeApi
import com.plcoding.jetpackcomposepokedex.network.remote.responsestoplevel.PokemonListResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*


class GetPokemonListEntries(
    private val pokeApi: PokeApi)
{

    fun execute(
        limit: Int,
        offset: Int
    ): Flow<DataState<List<PokedexListEntryDomainModel>>> = flow {

        try {
            emit(DataState.loading())

            // just to show loading, cache is fast
            delay(1000)



            val networkPokemonListResponse:PokemonListResponse = getPokemonListResponseFromNetwork(limit,offset)

            val pokedexEntries: List<PokedexListEntryDomainModel> = networkPokemonListResponse.results.mapIndexed { index, entry ->
                val number = if(entry.url.endsWith("/")) {
                    entry.url.dropLast(1).takeLastWhile { it.isDigit() }
                } else {
                    entry.url.takeLastWhile { it.isDigit() }
                }
                val url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${number}.png"
                PokedexListEntryDomainModel(entry.name.capitalize(Locale.ROOT), url, number.toInt())
            }


            emit(DataState.success(pokedexEntries))




        }

        catch (e: Exception)
        {
            emit(DataState.error<List<PokedexListEntryDomainModel>>(e.message ?: "Unknown Error"))
        }
    }






    private suspend fun getPokemonListResponseFromNetwork(limit: Int,offset: Int): PokemonListResponse {
        return pokeApi.getPokemonList(limit,offset)
    }




}