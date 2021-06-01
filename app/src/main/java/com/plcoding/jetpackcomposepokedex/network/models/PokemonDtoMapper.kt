package com.plcoding.jetpackcomposepokedex.network.models

import com.plcoding.jetpackcomposepokedex.domain.model.PokemonDomainModel
import com.plcoding.jetpackcomposepokedex.domain.util.DomainMapperTo
import com.plcoding.jetpackcomposepokedex.domain.util.DomainMapperToImpl
import com.plcoding.jetpackcomposepokedex.network.remote.responses.Pokemon


class PokemonDtoMapper  : DomainMapperTo<Pokemon, PokemonDomainModel> {


    override fun mapToDomainModel(model: Pokemon): PokemonDomainModel {
        return PokemonDomainModel(
            id = model.id,
            name = model.name,
            height = model.height,
            sprites = model.sprites,
            stats = model.stats,
            weight = model.weight
        )
    }

    fun toDomainList(initial: List<Pokemon>): List<PokemonDomainModel>{
        return initial.map { mapToDomainModel(it) }
    }

    /*
    override fun mapFromDomainModel(domainModel: PokemonDomainModel): Pokemon {
        return Pokemon(
            id = domainModel.id,
            name = domainModel.name,
            height = domainModel.height,
            sprites = domainModel.sprites,
            stats = domainModel.stats,
            weight = domainModel.weight
        )
    }*/
}