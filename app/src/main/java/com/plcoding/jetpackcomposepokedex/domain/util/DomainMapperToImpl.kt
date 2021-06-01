package com.plcoding.jetpackcomposepokedex.domain.util

// has to and from mapper // implements the to mapper
interface DomainMapperToImpl<T, DomainModel> : DomainMapperTo<T, DomainModel> {

    fun mapFromDomainModel(domainModel: DomainModel): T
}