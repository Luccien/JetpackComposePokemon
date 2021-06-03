package com.plcoding.jetpackcomposepokedex.cache.model

import com.plcoding.jetpackcomposepokedex.domain.model.PokemonDomainModel
import com.plcoding.jetpackcomposepokedex.domain.util.DomainMapperToImpl

class PokemonEntityMapper : DomainMapperToImpl<PokemonEntity, PokemonDomainModel> {
    override fun mapToDomainModel(model: PokemonEntity): PokemonDomainModel {
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

    override fun mapFromDomainModel(domainModel: PokemonDomainModel): PokemonEntity {
        return PokemonEntity(
            id = domainModel.id,
            name = domainModel.name,
            height = domainModel.height,
            sprites = domainModel.sprites,
            stats = domainModel.stats,
            weight = domainModel.weight,
            types = domainModel.types

        )
    }

    fun toDomainList(initial: List<PokemonEntity>): List<PokemonDomainModel>{
        return initial.map { mapToDomainModel(it) }
    }

    fun toEntityList(initial: List<PokemonDomainModel>): List<PokemonEntity>{
        return initial.map { mapFromDomainModel(it) }
    }


}