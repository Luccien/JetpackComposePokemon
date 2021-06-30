package com.plcoding.jetpackcomposepokedex.interactors.pokemon

import com.plcoding.jetpackcomposepokedex.cache.PokedexListEntryDao
import com.plcoding.jetpackcomposepokedex.cache.model.PokedexListEntryEntityMapper
import com.plcoding.jetpackcomposepokedex.domain.data.DataState
import com.plcoding.jetpackcomposepokedex.domain.model.PokedexListEntryDomainModel
import com.plcoding.jetpackcomposepokedex.network.remote.responsestoplevel.PokemonListResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.*

/**
 * Restore a list of pokemons after process death.
 */
class RestorePokemon (
   private val pokedexListEntryEntityMapper: PokedexListEntryEntityMapper,
    private val pokedexListEntryDao: PokedexListEntryDao
)
{

    fun execute(
    ): Flow<DataState<List<PokedexListEntryDomainModel>>> = flow {

        try {
            emit(DataState.loading())

            var pokedexEntries:List<PokedexListEntryDomainModel> = listOf<PokedexListEntryDomainModel>()

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









}