package org.example

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.jackson.*

fun createClient(customize: HttpClientConfig<CIOEngineConfig>.() -> Unit = {}) =
    HttpClient(CIO) {
        install(ContentNegotiation) {
            jackson {
                registerModule(
                    KotlinModule.Builder()
                        .configure(KotlinFeature.NullIsSameAsDefault, enabled = true)
                        .build()
                )
                registerModule(JavaTimeModule())
                enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
                disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE)
            }
        }
        customize()
    }
