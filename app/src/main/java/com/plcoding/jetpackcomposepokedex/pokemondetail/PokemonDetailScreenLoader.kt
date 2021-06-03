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
    navController: NavController

)
{
    Box (
        modifier = Modifier.fillMaxSize()
    ){
        if(true){
            PokemonDetailScreen(
                dominantColor = dominantColor,
                pokemonName = pokemonName?.toLowerCase(Locale.ROOT) ?: "",
                navController = navController
            )
        }
        /*
        if (loading && recipe == null) {
            LoadingRecipeShimmer(imageHeight = IMAGE_HEIGHT.dp)
        }
        else if(!loading && recipe == null && onLoad){
            TODO("Show Invalid Recipe")
        }
        else {
            recipe?.let {RecipeView(recipe = it) }
        }*/
    }

}