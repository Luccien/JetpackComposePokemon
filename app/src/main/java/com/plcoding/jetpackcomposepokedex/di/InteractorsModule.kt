package com.plcoding.jetpackcomposepokedex.di

import com.plcoding.jetpackcomposepokedex.cache.PokemonDao
import com.plcoding.jetpackcomposepokedex.cache.model.PokemonEntityMapper
import com.plcoding.jetpackcomposepokedex.interactors.pokemon.GetPokemon
import com.plcoding.jetpackcomposepokedex.network.models.PokemonDtoMapper
import com.plcoding.jetpackcomposepokedex.network.remote.PokeApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object InteractorsModule {

    @ViewModelScoped
    @Provides
    fun provideGetPokemon(
        //pokemonDao: PokemonDao,
        //pokemonEntityMapper: PokemonEntityMapper,
        pokeApi: PokeApi,
        pokemonDtoMapper: PokemonDtoMapper,
    ): GetPokemon {
        return GetPokemon(
            //pokemonDao = pokemonDao,
            //entityMapper = pokemonEntityMapper,
            pokeApi = pokeApi,
            pokemonDtoMapper = pokemonDtoMapper,
        )
    }
}