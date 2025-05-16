package com.airalo.example.offer.data.client.di

import com.airalo.example.offer.data.client.HttpClientConfigurator
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.module

private const val RAW_CLIENT = "RAW_CLIENT"

@OptIn(ExperimentalSerializationApi::class)
fun resolveClientKoin() =
    module {
        factory(named(RAW_CLIENT)) {
            HttpClient(CIO) {
                install(Logging) {
                    logger = Logger.SIMPLE
                    level = LogLevel.ALL
                }
            }
        }

        single {
            Json {
                allowTrailingComma = true
                ignoreUnknownKeys = true
                isLenient = true
            }
        }

        single {
            HttpClientConfigurator(json = get()).configure(get(named(RAW_CLIENT)))
        }

        single(qualifier = named("BaseUrl")) {
            "https://www.airalo.com/api/v2/"
        }
    }
