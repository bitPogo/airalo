package com.airalo.example.offer.domain.entity

@JvmInline
value class Flag(val url: String)

data class Country(
    val id: Id,
    val flag: Flag,
    val name: String,
)
