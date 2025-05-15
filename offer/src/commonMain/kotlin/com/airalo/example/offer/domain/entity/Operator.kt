package com.airalo.example.offer.domain.entity

@JvmInline
value class OperatorLogoUrl(val url: String)

data class Operator(
    val name: String,
    val logo: OperatorLogoUrl,
)
