package com.plcoding.jetpackcomposepokedex.interactors.pokemon_list

import com.google.gson.GsonBuilder
import com.plcoding.jetpackcomposepokedex.cache.AppDatabaseFake
import com.plcoding.jetpackcomposepokedex.cache.PokemonDaoFake
import com.plcoding.jetpackcomposepokedex.cache.model.PokedexListEntryEntityMapper
import com.plcoding.jetpackcomposepokedex.domain.model.PokedexListEntryDomainModel
import com.plcoding.jetpackcomposepokedex.interactors.pokemon.GetPokemonListEntries
import com.plcoding.jetpackcomposepokedex.network.remote.PokeApi
import com.plcoding.jetpackcomposepokedex.util.Constants
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

class GetPokemonListEntriesTest {

    private val appDatabase = AppDatabaseFake()
    private lateinit var mockWebServer: MockWebServer
    private lateinit var baseUrl: HttpUrl


    // system in test
    private lateinit var getPokemonListEntries: GetPokemonListEntries

    // Dependencies
    private lateinit var pokeApi: PokeApi
    private lateinit var pokedexListEntryDao: PokemonDaoFake
    private val entityMapper = PokedexListEntryEntityMapper()
    private val curPage=1



    @BeforeEach
    fun setup(){
        mockWebServer = MockWebServer()
        mockWebServer.start()
        baseUrl = mockWebServer.url("/api/v2/")
        pokeApi = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(PokeApi::class.java)
        pokedexListEntryDao = PokemonDaoFake(appDatabaseFake = appDatabase)

        // instantiate system in test
        getPokemonListEntries = GetPokemonListEntries(
            pokeApi = pokeApi,
            pokedexListEntryDao = pokedexListEntryDao,
            pokedexListEntryEntityMapper = entityMapper
        )


    }


    /**
     * Simulate a bad request
     */
    @Test
    fun getPokemonsFromNetwork_emitHttpError(): Unit = runBlocking {

        // condition the response
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST)
                .setBody("{}")
        )

        val alreadyExistingPokemons = entityMapper.toDomainList(appDatabase.pokemons) // array is empty at start

        val flowItems = getPokemonListEntries.execute(true,
            alreadyExistingPokemons as MutableList<PokedexListEntryDomainModel>,
            Constants.PAGE_SIZE, curPage * Constants.PAGE_SIZE,true ).toList()
            .toList()

        // first emission should be `loading`
        assert(flowItems[0].loading)

        // Second emission should be the exception
        val error = flowItems[1].error
        assert(error != null)

        assert(!flowItems[1].loading) // loading should be false now
    }



    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown()
    }


}