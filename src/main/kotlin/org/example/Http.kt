package org.example

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.example.model.Todo

fun main() = runBlocking {
    val client = createHttpClient()

    val todos = client.get<List<Todo>>("https://jsonplaceholder.typicode.com/todos/")

    todos
        .map { async { client.get<Todo>("https://jsonplaceholder.typicode.com/todos/${it.id}") } }
        .awaitAll()
        .forEach { println(it) }
}

private fun createHttpClient() = HttpClient(CIO) {
    install(JsonFeature) {
        serializer = JacksonSerializer(
            ObjectMapper()
                .registerModule(KotlinModule(nullIsSameAsDefault = true))
                .enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
        )
    }
}