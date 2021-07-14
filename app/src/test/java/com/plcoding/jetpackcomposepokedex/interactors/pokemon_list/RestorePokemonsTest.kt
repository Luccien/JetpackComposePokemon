package com.plcoding.jetpackcomposepokedex.interactors.pokemon_list

import com.google.gson.GsonBuilder
import com.plcoding.jetpackcomposepokedex.cache.AppDatabaseFake
import com.plcoding.jetpackcomposepokedex.cache.PokemonDaoFake
import com.plcoding.jetpackcomposepokedex.cache.model.PokedexListEntryEntityMapper
import com.plcoding.jetpackcomposepokedex.domain.model.PokedexListEntryDomainModel
import com.plcoding.jetpackcomposepokedex.interactors.pokemon.GetPokemonListEntries
import com.plcoding.jetpackcomposepokedex.interactors.pokemon.RestorePokemon
import com.plcoding.jetpackcomposepokedex.network.data.MockWebServerResponses
import com.plcoding.jetpackcomposepokedex.network.remote.PokeApi
import com.plcoding.jetpackcomposepokedex.util.Constants
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
import kotlinx.coroutines.flow.toList



class RestorePokemonsTest {
    private val appDatabase = AppDatabaseFake()
    private lateinit var mockWebServer: MockWebServer
    private lateinit var baseUrl: HttpUrl

    // system in test
    private lateinit var restorePokemon: RestorePokemon

    // Dependencies
    private lateinit var getPokemonListEntries: GetPokemonListEntries
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


        getPokemonListEntries = GetPokemonListEntries(
            pokeApi = pokeApi,
            pokedexListEntryDao = pokedexListEntryDao,
            pokedexListEntryEntityMapper = entityMapper
        )

        // instantiate system in test
        restorePokemon = RestorePokemon(
            pokedexListEntryDao = pokedexListEntryDao,
            pokedexListEntryEntityMapper = entityMapper
        )
    }

    /**
     * 1. Get some pokemons from the network and insert into cache
     * 2. Restore and show pokemons are retrieved from cache
     */
    @Test
    fun getPokemonsFromNetwork_restoreFromCache(): Unit = runBlocking {

        // condition the response
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(MockWebServerResponses.pokemonListResponse)
        )

        // confirm the cache is empty to start
        assert(pokedexListEntryDao.getCompletePokedexList().isEmpty())


        val alreadyExistingPokemons = entityMapper.toDomainList(appDatabase.pokemons) // array is empty at start

        val searchResult = getPokemonListEntries.execute(true,
            alreadyExistingPokemons as MutableList<PokedexListEntryDomainModel>,
            Constants.PAGE_SIZE, curPage * Constants.PAGE_SIZE,true ).toList()

        // confirm the cache is no longer empty
        assert(pokedexListEntryDao.getCompletePokedexList().isNotEmpty())

        // run use case
        val flowItems = restorePokemon.execute().toList()

        // first emission should be `loading`
        assert(flowItems[0].loading)

        // Second emission should be the list of recipes
        val pokemons = flowItems[1].data
        assert(pokemons?.size?: 0 > 0)

        // confirm they are actually Recipe objects
        assert(value = pokemons?.get(index = 0) is PokedexListEntryDomainModel)

        assert(!flowItems[1].loading) // loading should be false now


    }


    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown()
    }



}