package com.plcoding.jetpackcomposepokedex.network.remote

import com.plcoding.jetpackcomposepokedex.network.remote.responsestoplevel.PokemonDto
import com.plcoding.jetpackcomposepokedex.network.remote.responsestoplevel.PokemonListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApi {

    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): PokemonListResponse

    @GET("pokemon/{name}")
    suspend fun getPokemonInfo(
        @Path("name") name: String
    ): PokemonDto
}