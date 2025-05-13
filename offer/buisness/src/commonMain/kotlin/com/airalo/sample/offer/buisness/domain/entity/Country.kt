package com.airalo.sample.offer.buisness.entity

import com.airalo.sample.offer.buisness.domain.entity.Id

@JvmInline
value class Flag(val url: String)

data class Country(
    val id: Id,
    val flag: Flag,
    val name: String,
)
