package com.plcoding.jetpackcomposepokedex.cache.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.plcoding.jetpackcomposepokedex.cache.PokemonDao
import com.plcoding.jetpackcomposepokedex.cache.model.PokemonEntity

@Database(entities = [PokemonEntity::class ], version = 18)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase(){

    abstract fun pokemonDao(): PokemonDao

    companion object{
        val DATABASE_NAME: String = "pokemon_db"
    }

}

