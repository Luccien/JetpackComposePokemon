package com.plcoding.jetpackcomposepokedex.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemonList")
data class PokedexListEntryEntity(

    // Value from API
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "pokemonName")
    val pokemonName: String,

    // Value from API
    @ColumnInfo(name = "imageUrl")
    val imageUrl: String,

    // Value from API
    @ColumnInfo(name = "number")
    val number: Int
)
