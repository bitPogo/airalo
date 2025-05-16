package com.airalo.example.offer.data.repository

import com.airalo.example.offer.api.client.CountriesApi
import com.airalo.example.offer.api.client.OffersInCountryApi
import com.airalo.example.offer.api.model.CountryDTO
import com.airalo.example.offer.api.model.OfferDTO
import com.airalo.example.offer.domain.entity.Country
import com.airalo.example.offer.domain.entity.CountryFlagUri
import com.airalo.example.offer.domain.entity.Id
import com.airalo.example.offer.domain.entity.Offer
import com.airalo.example.offer.domain.entity.Operator
import com.airalo.example.offer.domain.entity.OperatorLogoUrl
import com.airalo.example.offer.domain.entity.Price
import com.airalo.example.offer.domain.entity.Validity
import com.airalo.example.offer.domain.entity.Volume
import com.airalo.example.offer.domain.repository.OfferRepositoryContract
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class OfferRepository(
    private val countryApi: CountriesApi,
    private val offerApi: OffersInCountryApi,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : OfferRepositoryContract {
    private fun List<CountryDTO>.toCountry(): List<Country> =
        map { dto ->
            Country(
                id = Id(dto.id),
                flag = CountryFlagUri(dto.image.url),
                name = dto.title,
            )
        }

    override suspend fun listPopularCountries(): List<Country> {
        return withContext(ioDispatcher) {
            try {
                countryApi.countries(type = CountriesApi.TypeCountries.popular).body().toCountry()
            } catch (_: Throwable) {
                emptyList()
            }
        }
    }

    private fun OfferDTO.toOffer(): List<Offer> {
        return this.packages.map { dto ->
            Offer(
                operator = Operator(
                    name = dto.operator.title,
                    logo = OperatorLogoUrl(dto.operator.image.url),
                ),
                price = Price(dto.price),
                validity = Validity(dto.validity),
                volume = Volume(dto.data),
                location = this.title,
            )
        }
    }

    override suspend fun fetchOffersForCountry(id: Id): List<Offer> {
        return withContext(ioDispatcher) {
            try {
                offerApi.countryOffers(id = id.id).body().toOffer()
            } catch (_: Throwable) {
                emptyList()
            }
        }
    }
}
