package com.plcoding.jetpackcomposepokedex.pokemondetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.navigation.NavController
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
    val pokemon = viewModel.pokemon.value


    if (loading && pokemon == null) {
        // todo add loading animation
         //LoadingPokemonShimmer(imageHeight = IMAGE_HEIGHT.dp)
    }
    else if(!loading && pokemon == null && onLoad){
        TODO("Show Invalid Recipe")
    }
    else {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
                PokemonDetailScreen(
                    dominantColor = dominantColor,
                    pokemonDM = pokemon, // TODO RENAME IN POKEMONINFO
                    //pokemonName = pokemonName?.toLowerCase(Locale.ROOT) ?: "",
                    navController = navController
                )
            }

        }


    }

}