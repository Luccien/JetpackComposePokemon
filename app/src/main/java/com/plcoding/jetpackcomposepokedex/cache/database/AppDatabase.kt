package com.plcoding.jetpackcomposepokedex.cache.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.plcoding.jetpackcomposepokedex.cache.PokedexListEntryDao
import com.plcoding.jetpackcomposepokedex.cache.PokemonDao
import com.plcoding.jetpackcomposepokedex.cache.model.PokedexListEntryEntity
import com.plcoding.jetpackcomposepokedex.cache.model.PokemonEntity

@Database(entities = [PokemonEntity::class,PokedexListEntryEntity::class ], version = 37)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase(){

    abstract fun pokemonDao(): PokemonDao

    abstract fun pokedexListEntryDao(): PokedexListEntryDao

    companion object{
        val DATABASE_NAME: String = "pokemon_db"
    }

}

