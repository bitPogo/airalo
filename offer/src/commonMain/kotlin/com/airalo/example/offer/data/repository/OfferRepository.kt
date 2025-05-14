package com.airalo.example.offer.data.repository

import com.airalo.example.offer.api.client.CountriesApi
import com.airalo.example.offer.api.model.CountryDTO
import com.airalo.example.offer.domain.entity.Country
import com.airalo.example.offer.domain.entity.Flag
import com.airalo.example.offer.domain.entity.Id
import com.airalo.example.offer.domain.repository.OfferRepositoryContract

internal class OfferRepository(
    private val countryApi: CountriesApi,
) : OfferRepositoryContract {
    private fun List<CountryDTO>.toEntity(): List<Country> =
        map { dto ->
            Country(
                id = Id(dto.id),
                flag = Flag(dto.image.url),
                name = dto.title,
            )
        }

    override suspend fun listPopularCountries(): List<Country> {
        return try {
            countryApi.countries(type = CountriesApi.TypeCountries.popular).body().toEntity()
        } catch (e: Throwable) {
            println(e)
            emptyList()
        }
    }
}
