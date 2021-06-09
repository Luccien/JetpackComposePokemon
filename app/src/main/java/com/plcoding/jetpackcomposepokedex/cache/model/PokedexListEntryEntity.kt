package com.plcoding.jetpackcomposepokedex.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "pokemonList")
data class PokedexListEntryEntity(

    // Value from API
    @ColumnInfo(name = "pokemonName")
    val pokemonName: String,

    // Value from API
    @ColumnInfo(name = "imageUrl")
    val imageUrl: String,

    // Value from API
    @ColumnInfo(name = "number")
    val number: Int
)
