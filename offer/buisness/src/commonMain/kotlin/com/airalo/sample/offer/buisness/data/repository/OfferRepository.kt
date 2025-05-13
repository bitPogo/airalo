package com.airalo.sample.offer.buisness.data.repository

import com.airalo.sample.offer.buisness.domain.repository.OfferRepositoryContract
import com.airalo.sample.offer.buisness.api.client.CountriesApi
import com.airalo.sample.offer.buisness.api.model.CountryDTO
import com.airalo.sample.offer.buisness.domain.entity.Country
import com.airalo.sample.offer.buisness.domain.entity.Id
import com.airalo.sample.offer.buisness.domain.entity.Flag

internal class OfferRepository(
    private val countryApi: CountriesApi
) : OfferRepositoryContract {
    private fun List<CountryDTO>.toEntity(): List<Country> = map { dto ->
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
