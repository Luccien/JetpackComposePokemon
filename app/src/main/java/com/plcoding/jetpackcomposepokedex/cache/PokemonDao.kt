package com.plcoding.jetpackcomposepokedex.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.plcoding.jetpackcomposepokedex.cache.model.PokemonEntity

@Dao
interface PokemonDao {

    @Insert
    suspend fun insertPokemon(pokemon: PokemonEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRecipes(pokemon: List<PokemonEntity>): LongArray

    @Query("SELECT * FROM pokemons WHERE name = :name")
    suspend fun getRecipeById(id: Int): PokemonEntity?


    // TODO names instead of ids
    @Query("DELETE FROM pokemons WHERE id IN (:ids)")
    suspend fun deleteRecipes(ids: List<Int>): Int

    @Query("DELETE FROM pokemons")
    suspend fun deleteAllRecipes()

    @Query("DELETE FROM pokemons WHERE name = :name")
    suspend fun deleteRecipe(primaryKey: Int): Int

    // TODO    add querry for searching


}