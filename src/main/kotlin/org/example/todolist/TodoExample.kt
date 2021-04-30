package org.example.todolist

import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.coroutineScope
import org.example.asyncAll
import org.example.createClient

val todoClient by lazy {
    createClient {
        defaultRequest {
            url {
                protocol = URLProtocol.HTTPS
                host = "jsonplaceholder.typicode.com"
            }
        }
    }
}

suspend fun main() = coroutineScope {
    val todos = todoClient.get<List<Todo>>("/todos/")

    todos
        .asyncAll { todoClient.get<Todo>("/todos/${it.id}") }
        .forEach { println(it) }
}

data class Todo(
    val completed: Boolean,
    val id: Int,
    val title: String,
    val userId: Int,
)
