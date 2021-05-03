package org.example

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*

fun createClient(customize: HttpClientConfig<CIOEngineConfig>.() -> Unit = {}) =
    HttpClient(CIO) {
        install(JsonFeature) {
            serializer = JacksonSerializer(
                ObjectMapper()
                    .registerModule(KotlinModule(nullIsSameAsDefault = true))
                    .enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
                    .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            )
        }
        customize()
    }
