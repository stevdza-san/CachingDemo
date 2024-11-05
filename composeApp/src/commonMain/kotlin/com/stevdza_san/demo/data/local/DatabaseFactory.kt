package com.stevdza_san.demo.data.local

import app.cash.sqldelight.db.SqlDriver
import com.stevdza_san.PostDatabase
import com.stevdza_san.demo.domain.Post

interface DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}

class LocalDatabase(
    databaseDriverFactory: DatabaseDriverFactory
) {
    private val database = PostDatabase(
        databaseDriverFactory.createDriver()
    )
    private val query = database.postDatabaseQueries

    fun readAllPosts(): List<Post> {
        println("INFO: Reading the cached data from the local database...")
        return query.readAllPosts()
            .executeAsList()
            .map {
                Post(
                    userId = it.userId.toInt(),
                    id = it.id.toInt(),
                    thumbnail = it.thumbnail,
                    title = it.title,
                    body = it.body
                )
            }
    }

    fun insertAllPosts(posts: List<Post>) {
        println("INFO: Caching the data from the network...")
        query.transaction {
            posts.forEach { post ->
                query.insertPost(
                    userId = post.userId.toLong(),
                    thumbnail = post.thumbnail,
                    id = post.id.toLong(),
                    title = post.title,
                    body = post.body
                )
            }
        }
    }

    fun removeAllPosts() {
        query.removeAllPosts()
    }
}