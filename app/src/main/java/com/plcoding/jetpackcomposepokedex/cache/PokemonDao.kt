package com.plcoding.jetpackcomposepokedex.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.plcoding.jetpackcomposepokedex.cache.model.PokemonEntity

@Dao
interface PokemonDao {
    // pokemon name is unique and in api the name is used to get the detail of the pokemon

    /*
    @Insert
    suspend fun insertPokemon(pokemon: PokemonEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPokemons(pokemon: List<PokemonEntity>): LongArray

    @Query("SELECT * FROM pokemons WHERE name = :name")
    suspend fun getPokemonByName(name: String): PokemonEntity?

    @Query("DELETE FROM pokemons WHERE name = :name")
    suspend fun deletePokemon(name: String): String





    @Query("DELETE FROM pokemons WHERE id IN (:ids)")
    suspend fun deletePokemons(ids: List<Int>): Int

    @Query("DELETE FROM pokemons")
    suspend fun deleteAllPokemons()
*/


}