package com.plcoding.jetpackcomposepokedex.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.plcoding.jetpackcomposepokedex.cache.model.PokedexListEntryEntity


@Dao
interface PokedexListEntryDao {

    @Insert
    suspend fun insertPokemonListEntry(pokedexListEntry: PokedexListEntryEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPokemonList(pokedexList: List<PokedexListEntryEntity>): LongArray


    @Query("SELECT * FROM pokemonList")
    suspend fun getCompletePokedexList(): List<PokedexListEntryEntity>

}