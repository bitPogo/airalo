package com.airalo.example.offer.domain.entity

import com.airalo.example.offer.api.model.ImageDTO
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName

data class Offer(
    val operator: Operator,
    val price: Price,
    val validity: Validity,
    val volume: Volume,
)
