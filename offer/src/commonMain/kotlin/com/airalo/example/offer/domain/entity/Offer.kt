package com.airalo.example.offer.domain.entity

data class Offer(
    val operator: Operator,
    val price: Float,
    val validity: Validity,
    val volume: Volume,
)
