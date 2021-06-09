package com.plcoding.jetpackcomposepokedex.cache.model

import com.plcoding.jetpackcomposepokedex.domain.model.PokedexListEntryDomainModel
import com.plcoding.jetpackcomposepokedex.domain.util.DomainMapperToImpl

class PokedexListEntryEntityMapper : DomainMapperToImpl<PokedexListEntryEntity, PokedexListEntryDomainModel> {
        override fun mapToDomainModel(model: PokedexListEntryEntity): PokedexListEntryDomainModel {
            return PokedexListEntryDomainModel(
                pokemonName = model.pokemonName,
                imageUrl = model.imageUrl,
                number = model.number
            )
        }

        override fun mapFromDomainModel(domainModel: PokedexListEntryDomainModel): PokedexListEntryEntity {
            return PokedexListEntryEntity(
                pokemonName = domainModel.pokemonName,
                imageUrl = domainModel.imageUrl,
                number = domainModel.number

            )
        }

        fun toDomainList(initial: List<PokedexListEntryEntity>): List<PokedexListEntryDomainModel>{
            return initial.map { mapToDomainModel(it) }
        }

        fun toEntityList(initial: List<PokedexListEntryDomainModel>): List<PokedexListEntryEntity>{
            return initial.map { mapFromDomainModel(it) }
        }
}