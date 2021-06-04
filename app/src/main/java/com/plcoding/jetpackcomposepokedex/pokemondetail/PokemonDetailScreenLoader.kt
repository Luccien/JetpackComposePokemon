package com.plcoding.jetpackcomposepokedex.pokemondetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.navigation.NavController
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import java.util.*

@Composable
fun PokemonDetailScreenLoader(
    dominantColor: Color,
    pokemonName: String,
    navController: NavController,
    viewModel: PokemonDetailViewModel = hiltNavGraphViewModel()

){


if (pokemonName == null){
  // TODO show invalid pokemon
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
    }
    else if(!loading && pokemonDM == null && onLoad){
        TODO("Show Invalid Recipe")
    }
    else {
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