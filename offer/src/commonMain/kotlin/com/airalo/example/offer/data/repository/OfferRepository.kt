package com.airalo.example.offer.data.repository

import com.airalo.example.offer.api.client.CountriesApi
import com.airalo.example.offer.api.model.CountryDTO
import com.airalo.example.offer.domain.entity.Country
import com.airalo.example.offer.domain.entity.CountryFlagUri
import com.airalo.example.offer.domain.entity.Id
import com.airalo.example.offer.domain.entity.Offer
import com.airalo.example.offer.domain.repository.OfferRepositoryContract

internal class OfferRepository(
    private val countryApi: CountriesApi,
) : OfferRepositoryContract {
    private fun List<CountryDTO>.toEntity(): List<Country> =
        map { dto ->
            Country(
                id = Id(dto.id),
                flag = CountryFlagUri(dto.image.url),
                name = dto.title,
            )
        }

    override suspend fun listPopularCountries(): List<Country> {
        return try {
            countryApi.countries(type = CountriesApi.TypeCountries.popular).body().toEntity()
        } catch (_: Throwable) {
            emptyList()
        }
    }

    override suspend fun fetchOffersForCountry(id: Id): List<Offer> {
        TODO("Not yet implemented")
    }
}
