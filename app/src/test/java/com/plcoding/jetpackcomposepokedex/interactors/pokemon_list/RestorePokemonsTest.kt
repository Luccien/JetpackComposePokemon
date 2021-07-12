package com.plcoding.jetpackcomposepokedex.interactors.pokemon_list

import com.google.gson.GsonBuilder
import com.plcoding.jetpackcomposepokedex.cache.AppDatabaseFake
import com.plcoding.jetpackcomposepokedex.cache.PokemonDaoFake
import com.plcoding.jetpackcomposepokedex.cache.model.PokemonEntityMapper
import com.plcoding.jetpackcomposepokedex.interactors.pokemon.GetPokemonListEntries
import com.plcoding.jetpackcomposepokedex.interactors.pokemon.RestorePokemon
import com.plcoding.jetpackcomposepokedex.network.models.PokemonDtoMapper
import com.plcoding.jetpackcomposepokedex.network.remote.PokeApi
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.BeforeEach
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RestorePokemonsTest {
    private val appDatabase = AppDatabaseFake()
    private lateinit var mockWebServer: MockWebServer
    private lateinit var baseUrl: HttpUrl
    private val DUMMY_QUERY = "This doesn't matter" // can be anything

    // system in test
    private lateinit var restorePokemon: RestorePokemon

    // Dependencies
    private lateinit var getPokemonListEntries: GetPokemonListEntries
    private lateinit var pokeApi: PokeApi
    private lateinit var pokemonDao: PokemonDaoFake
    private val entityMapper = PokemonEntityMapper()
    private val dtoMapper = PokemonDtoMapper()





}