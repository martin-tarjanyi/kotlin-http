package org.example

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*

val client by lazy {
    HttpClient(CIO) {
        install(JsonFeature) {
            serializer = JacksonSerializer(
                ObjectMapper()
                    .registerModule(KotlinModule(nullIsSameAsDefault = true))
                    .enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
            )
        }
    }
}
