package com.plcoding.jetpackcomposepokedex.network.models

import com.plcoding.jetpackcomposepokedex.cache.model.PokedexListEntryEntity
import com.plcoding.jetpackcomposepokedex.domain.model.PokedexListEntryDomainModel
import com.plcoding.jetpackcomposepokedex.domain.util.DomainMapperTo
import com.plcoding.jetpackcomposepokedex.domain.util.DomainMapperToImpl

class PokedexListEntryDtoMapper {
}



class PokedexListEntryEntityMapper : DomainMapperTo<PokedexListEntryDto, PokedexListEntryDomainModel> {

    override fun mapToDomainModel(model: PokedexListEntryDto): PokedexListEntryDomainModel {
        return PokedexListEntryDomainModel(
            pokemonName = model.pokemonName,
            imageUrl = model.imageUrl,
            number = model.number
        )
    }


    fun toDomainList(initial: List<PokedexListEntryDto>): List<PokedexListEntryDomainModel>{
        return initial.map { mapToDomainModel(it) }
    }


}