package com.plcoding.jetpackcomposepokedex.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.plcoding.jetpackcomposepokedex.network.remote.responses.Sprites
import com.plcoding.jetpackcomposepokedex.network.remote.responses.Stat
import com.plcoding.jetpackcomposepokedex.network.remote.responses.Type

@Entity(tableName = "pokemons")
data class PokemonEntity(

    // Value from API

    @ColumnInfo(name = "id")
    val id: Int,

    // Value from API
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "name")
    val name: String,

    // Value from API
    @ColumnInfo(name = "height")
    val height: Int,

    // Value from API
    @ColumnInfo(name = "sprites")
    val sprites: Sprites,

    // Value from API
    @ColumnInfo(name = "stats")
    val stats: List<Stat>,

    // Value from API
    @ColumnInfo(name = "weight")
    val weight: Int,

    // Value from API
    @ColumnInfo(name = "types")
    val types: List<Type>
)
