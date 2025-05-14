package com.airalo.example.offer.domain.entity

@JvmInline
value class CountryFlagUri(val url: String)

data class Country(
    val id: Id,
    val flag: CountryFlagUri,
    val name: String,
)
