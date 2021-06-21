package com.plcoding.jetpackcomposepokedex.presentation.ui.pokemon_detail

sealed class PokemonEvent {
    data class GetPokemonEvent(
        val name: String
    ): PokemonEvent()
}