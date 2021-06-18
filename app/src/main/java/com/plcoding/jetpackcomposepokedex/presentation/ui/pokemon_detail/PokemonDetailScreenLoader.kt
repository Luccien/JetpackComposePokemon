package com.plcoding.jetpackcomposepokedex.presentation.ui.pokemon_detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.navigation.NavController
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import com.plcoding.jetpackcomposepokedex.presentation.components.NothingHere

@Composable
fun PokemonDetailScreenLoader(
    dominantColor: Color,
    pokemonName: String,
    navController: NavController,
    viewModel: PokemonDetailViewModel = hiltNavGraphViewModel()

){


if (pokemonName == null){
    NothingHere()

}else {

    // fire a one-off event to get the pokemon from api
    val onLoad = viewModel.onLoad.value
    if (!onLoad) {
        viewModel.onLoad.value = true
        viewModel.onTriggerEvent(pokemonName)
    }

    val loading = viewModel.loading.value
    val pokemonDM = viewModel.pokemon.value

    if (loading && pokemonDM == null) {
         CircularProgressIndicator(
            color = MaterialTheme.colors.primary,
            modifier = Modifier
        )

    } else if (!loading && pokemonDM == null && onLoad) { //  can happen when network is not available or local database broken/unavailable
        NothingHere()

    } else if(pokemonDM != null){
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            PokemonDetailScreen(
                dominantColor = dominantColor,
                pokemonDM = pokemonDM,
                navController = navController
            )
        }
    }



    }

}