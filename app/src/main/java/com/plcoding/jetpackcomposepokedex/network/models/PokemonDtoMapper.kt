package com.plcoding.jetpackcomposepokedex.network.models

import com.plcoding.jetpackcomposepokedex.domain.model.PokemonDomainModel
import com.plcoding.jetpackcomposepokedex.domain.util.DomainMapperTo
import com.plcoding.jetpackcomposepokedex.network.remote.responsestoplevel.PokemonDto


class PokemonDtoMapper  : DomainMapperTo<PokemonDto, PokemonDomainModel> {


    override fun mapToDomainModel(model: PokemonDto): PokemonDomainModel {
        return PokemonDomainModel(
            id = model.id,
            name = model.name,
            height = model.height,
            sprites = model.sprites,
            stats = model.stats,
            weight = model.weight,
            types = model.types
        )
    }

    fun toDomainList(initial: List<PokemonDto>): List<PokemonDomainModel>{
        return initial.map { mapToDomainModel(it) }
    }


}