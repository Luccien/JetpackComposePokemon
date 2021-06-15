package com.plcoding.jetpackcomposepokedex.di

import androidx.room.Room
import com.plcoding.jetpackcomposepokedex.presentation.PokedexApplication
import com.plcoding.jetpackcomposepokedex.cache.PokedexListEntryDao
import com.plcoding.jetpackcomposepokedex.cache.PokemonDao
import com.plcoding.jetpackcomposepokedex.cache.database.AppDatabase
import com.plcoding.jetpackcomposepokedex.cache.model.PokedexListEntryEntityMapper
import com.plcoding.jetpackcomposepokedex.cache.model.PokemonEntityMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CacheModule {



    @Singleton
    @Provides
    fun provideDb(app: PokedexApplication): AppDatabase {
        return Room
            .databaseBuilder(app, AppDatabase::class.java, AppDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun providePokemonDao(db: AppDatabase): PokemonDao {
        return db.pokemonDao()
    }

    @Singleton
    @Provides
    fun provideCachePokemonMapper(): PokemonEntityMapper {
        return PokemonEntityMapper()
    }

    @Singleton
    @Provides
    fun providePokedexListEntryDao(db: AppDatabase): PokedexListEntryDao {
        return db.pokedexListEntryDao()
    }

    @Singleton
    @Provides
    fun provideCachePokedexListEntryMapper(): PokedexListEntryEntityMapper {
        return PokedexListEntryEntityMapper()
    }


}