package com.plcoding.jetpackcomposepokedex.network.remote.responses


import com.google.gson.annotations.SerializedName

data class GenerationV(
    @SerializedName("black-white")
    val blackWhite: BlackWhite
)