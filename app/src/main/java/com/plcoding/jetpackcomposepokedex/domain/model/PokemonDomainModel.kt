package com.plcoding.jetpackcomposepokedex.domain.model

import com.plcoding.jetpackcomposepokedex.network.remote.responses.*

data class PokemonDomainModel(
    val id: Int,
    val name: String,
    val height: Int,
    val sprites: Sprites,
    val stats: List<Stat>,
    val weight: Int,
    val types: List<Type>
)
