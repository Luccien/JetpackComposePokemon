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


/*
 Avoiding Crashes:  (SAME AS in GetPokemon)
 A crash which occures when user is loading content: a pokemon for the first time but he does not have a internetconnection.
 Within a try and catch blog whitin a flow (GetPokemon.kt )  throw Exeption() -> which leads to the flow beeing cancelled and in
 this case the app beeing crashed
  Further info about it: https://medium.com/@chibatching/avoiding-to-crash-caused-by-misunderstanding-kotlin-coroutine-scope-b38ff5cbef20
    SOLUTION/FIX --> Here just emit(DataState.error("message")) will work to inform the user what is wrong // throw Exception("message") causing the crash is not not necessary
 */


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
        offset: Int,
        isNetworkAvailable: Boolean
    ): Flow<DataState<List<PokedexListEntryDomainModel>>> = flow {

        try {
            emit(DataState.loading())


            var pokedexEntries:List<PokedexListEntryDomainModel> = listOf<PokedexListEntryDomainModel>()

            // onAppStart is only true   when this is called  from the viewModel Init -
            // only on app start it will be checked if something is in the db - all other calls will be when pages data from the db has ended - so it will continue loading and saving
            if(onAppStart){
                pokedexEntries = getPokedexEntriesFromCache()
            }


            // pokedexEntries will either be null when  1)) onAppStart = false 2)) or database is empty (which only happens when app is started or dbschema change for the very first time)
            if(pokedexEntries.isNotEmpty()){
                emit(DataState.success(pokedexEntries))
            }
            else {
                if (isNetworkAvailable) {  // start network

                    val networkPokemonListResponse: PokemonListResponse =
                        getPokemonListResponseFromNetwork(limit, offset)

                    val networkPokedexEntries: List<PokedexListEntryDomainModel> =
                        networkPokemonListResponse.results.mapIndexed { index, entry ->
                            val number = if (entry.url.endsWith("/")) {
                                entry.url.dropLast(1).takeLastWhile { it.isDigit() }
                            } else {
                                entry.url.takeLastWhile { it.isDigit() }
                            }
                            val url =
                                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${number}.png"
                            PokedexListEntryDomainModel(
                                entry.name.capitalize(Locale.ROOT),
                                url,
                                number.toInt()
                            )
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
                ///////// end network


                // 1 get from cache // trying to get it again from cache (as in beginning of this try block ) ...
                // 2 even if   isNetworkAvailable=false (will be called twice) --> reason is to keep it consistent
                // 3 ( single source of truth (SSOT) )--> same call does not matter what happend before and how data was retrieved(1.network or 2. cache or 3.both sources doesnot work and there is no data)
                pokedexEntries = getPokedexEntriesFromCache()

                // emit and finish
                if(pokedexEntries.isNotEmpty()){
                //if(pokedexEntries != null){
                    emit(DataState.success(pokedexEntries))
                }
                else{
                    //( will capture when networking is off as in catch blog and also when local database is not working properly)
                    emit(DataState.error<List<PokedexListEntryDomainModel>>("Unable to get Pokemon List from the cache/internet."))

                    // DO NOT USE HERE // Exeptions if executed here will stop the flow and crash the app: checkout by turning internetconnection off
                    //  throw Exception("Unable to get pokemonList from the cache.")
                    //throw Exception("Unable to get pokemonList from the cache.")
                }


            }


        }

        catch (e: Exception)
        {
            emit(DataState.error<List<PokedexListEntryDomainModel>>(e.message ?: "Unknown Error - loading list"))// (will most likely tell that networking is switched off)
            //throw Exception("Unable to get Pokemon: network not available") // DO NOT USE HERE // Exeptions if executed here will stop the flow and crash the app: checkout by turning internetconnection off
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