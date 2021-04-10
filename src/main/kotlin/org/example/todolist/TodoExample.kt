package org.example.todolist

import io.ktor.client.request.*
import kotlinx.coroutines.coroutineScope
import org.example.asyncAll
import org.example.client

suspend fun main() = coroutineScope {
    val todos = client.get<List<Todo>>("https://jsonplaceholder.typicode.com/todos/")

    todos
        .asyncAll { client.get<Todo>("https://jsonplaceholder.typicode.com/todos/${it.id}") }
        .forEach { println(it) }
}

data class Todo(
    val completed: Boolean,
    val id: Int,
    val title: String,
    val userId: Int,
)
