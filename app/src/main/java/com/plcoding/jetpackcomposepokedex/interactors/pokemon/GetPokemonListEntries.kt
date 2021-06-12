package com.plcoding.jetpackcomposepokedex.interactors.pokemon


import com.plcoding.jetpackcomposepokedex.cache.PokedexListEntryDao
import com.plcoding.jetpackcomposepokedex.cache.model.PokedexListEntryEntityMapper
import com.plcoding.jetpackcomposepokedex.domain.data.DataState
import com.plcoding.jetpackcomposepokedex.domain.model.PokedexListEntryDomainModel
import com.plcoding.jetpackcomposepokedex.domain.model.PokemonDomainModel
import com.plcoding.jetpackcomposepokedex.network.remote.PokeApi
import com.plcoding.jetpackcomposepokedex.network.remote.responses.Stat
import com.plcoding.jetpackcomposepokedex.network.remote.responsestoplevel.PokemonListResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*


class GetPokemonListEntries(
    private val pokeApi: PokeApi,
    private val pokedexListEntryEntityMapper:PokedexListEntryEntityMapper,
    private val pokedexListEntryDao:PokedexListEntryDao
    )
{





    fun execute(
        onAppStart:Boolean = false,
        pokemonList:List<PokedexListEntryDomainModel>,
        limit: Int,
        offset: Int
    ): Flow<DataState<List<PokedexListEntryDomainModel>>> = flow {

        try {
            emit(DataState.loading())

            //delay(1000)

            var cachePokedexEntries:List<PokedexListEntryDomainModel> = listOf<PokedexListEntryDomainModel>()

            // onAppStart is only true   when this is called  from the viewModel Init -
            // only on app start it will be checked if something is in the db - all other calls will be when pages data from the db has ended - so it will continue loading and saving
            if(onAppStart){
                cachePokedexEntries = getPokedexEntriesFromCache()
            }


            // cachePokedexEntries will either be null when  1)) onAppStart = false 2)) or database is empty (which only happens when app is started or dbschema change for the very first time)
            if(cachePokedexEntries.isNotEmpty()){
                emit(DataState.success(cachePokedexEntries))
            }
            else {
                val networkPokemonListResponse:PokemonListResponse = getPokemonListResponseFromNetwork(limit,offset)

                val networkPokedexEntries: List<PokedexListEntryDomainModel> = networkPokemonListResponse.results.mapIndexed { index, entry ->
                    val number = if(entry.url.endsWith("/")) {
                        entry.url.dropLast(1).takeLastWhile { it.isDigit() }
                    } else {
                        entry.url.takeLastWhile { it.isDigit() }
                    }
                    val url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${number}.png"
                    PokedexListEntryDomainModel(entry.name.capitalize(Locale.ROOT), url, number.toInt())
                }



                // as it loads only paginated (e.g. pokemon from 10-20)  but for saving it needs the whole list  (pokemonList comes from viewmodel)
                // instead one could do an add operation on the database (than pokemonList would not be needed here)
                val completeList = pokemonList + networkPokedexEntries

                // insert into cache
                pokedexListEntryDao.insertPokemonList(
                    // map domain -> entity
                    pokedexListEntryEntityMapper.toEntityList(completeList)
                )

            }

            val pokedexEntries = getPokedexEntriesFromCache()

            // emit and finish
            if(pokedexEntries != null){
                emit(DataState.success(pokedexEntries))
            }
            else{
                throw Exception("Unable to get pokemonList from the cache.")
            }
        }

        catch (e: Exception)
        {
            emit(DataState.error<List<PokedexListEntryDomainModel>>(e.message ?: "Unknown Error"))
        }
    }



    private suspend fun getPokedexEntriesFromCache():List<PokedexListEntryDomainModel> {
        return pokedexListEntryDao.getCompletePokedexList()?.let { entriesEntity ->
            pokedexListEntryEntityMapper.toDomainList(entriesEntity)
        }
    }


    private suspend fun getPokemonListResponseFromNetwork(limit: Int,offset: Int): PokemonListResponse {
        return pokeApi.getPokemonList(limit,offset)
    }




}