package com.plcoding.jetpackcomposepokedex.presentation.ui.pokemon_list

sealed class PokemonListEvent {

    object NewSearchEvent : PokemonListEvent()

    object NextPageEvent : PokemonListEvent()

    // restore after process death
    object RestoreStateEvent: PokemonListEvent()
}