package com.airalo.example.offer.domain.entity

data class Offer(
    val location: String,
    val operator: Operator,
    val price: Price,
    val validity: Validity,
    val volume: Volume,
)
