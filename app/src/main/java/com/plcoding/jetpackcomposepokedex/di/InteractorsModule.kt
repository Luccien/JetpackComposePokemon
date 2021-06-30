package com.plcoding.jetpackcomposepokedex.di

import com.plcoding.jetpackcomposepokedex.cache.PokedexListEntryDao
import com.plcoding.jetpackcomposepokedex.cache.PokemonDao
import com.plcoding.jetpackcomposepokedex.cache.database.AppDatabase
import com.plcoding.jetpackcomposepokedex.cache.model.PokedexListEntryEntityMapper
import com.plcoding.jetpackcomposepokedex.cache.model.PokemonEntityMapper
import com.plcoding.jetpackcomposepokedex.interactors.pokemon.GetPokemon
import com.plcoding.jetpackcomposepokedex.interactors.pokemon.GetPokemonListEntries
import com.plcoding.jetpackcomposepokedex.interactors.pokemon.RestorePokemon
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
        pokemonDao: PokemonDao,
        pokemonEntityMapper: PokemonEntityMapper,
        pokeApi: PokeApi,
        pokemonDtoMapper: PokemonDtoMapper,
    ): GetPokemon {
        return GetPokemon(
            pokemonDao = pokemonDao,
            entityMapper = pokemonEntityMapper,
            pokeApi = pokeApi,
            pokemonDtoMapper = pokemonDtoMapper,
        )
    }


    @ViewModelScoped
    @Provides
    fun provideGetPokemonListEntries(
        pokeApi: PokeApi,
        pokedexListEntryEntityMapper: PokedexListEntryEntityMapper,
        pokedexListEntryDao: PokedexListEntryDao
    ): GetPokemonListEntries {
        return GetPokemonListEntries(
            pokeApi = pokeApi,
            pokedexListEntryEntityMapper =pokedexListEntryEntityMapper,
            pokedexListEntryDao = pokedexListEntryDao
        )
    }

    @ViewModelScoped
    @Provides
    fun provideRestorePokemon(
        pokedexListEntryEntityMapper: PokedexListEntryEntityMapper,
        pokedexListEntryDao: PokedexListEntryDao
    ): RestorePokemon {
        return RestorePokemon(
            pokedexListEntryEntityMapper =pokedexListEntryEntityMapper,
            pokedexListEntryDao = pokedexListEntryDao
        )
    }




}