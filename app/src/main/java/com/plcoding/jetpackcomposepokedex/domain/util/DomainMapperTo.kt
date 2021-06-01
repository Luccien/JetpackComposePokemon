package com.plcoding.jetpackcomposepokedex.domain.util

// has only the to mapper
interface DomainMapperTo <T, DomainModel>{

    fun mapToDomainModel(model: T): DomainModel

}