package com.plcoding.jetpackcomposepokedex.network.remote.responsestoplevel

import com.plcoding.jetpackcomposepokedex.network.remote.responses.Result


data class PokemonListResponse(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: List<Result>
)