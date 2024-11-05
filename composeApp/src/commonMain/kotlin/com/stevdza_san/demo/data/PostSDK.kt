package com.stevdza_san.demo.data

import com.russhwolf.settings.Settings
import com.stevdza_san.demo.data.local.LocalDatabase
import com.stevdza_san.demo.data.remote.PostApi
import com.stevdza_san.demo.domain.Post
import com.stevdza_san.demo.domain.RequestState
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration.Companion.hours

const val FRESH_DATA_KEY = "freshDataTimestamp"

class PostSDK(
    private val api: PostApi,
    private val database: LocalDatabase,
    private val settings: Settings
) {
    @Throws(Exception::class)
    suspend fun getAllPosts(): RequestState<List<Post>> {
        return try {
            val cachedPosts = database.readAllPosts()
            if (cachedPosts.isEmpty()) {
                settings.putLong(
                    FRESH_DATA_KEY,
                    Clock.System.now().toEpochMilliseconds()
                )
                RequestState.Success(
                    api.fetchAllPosts().also {
                        database.removeAllPosts()
                        database.insertAllPosts(it)
                    }
                )
            } else {
                if (isDataStale()) {
                    settings.putLong(
                        FRESH_DATA_KEY,
                        Clock.System.now().toEpochMilliseconds()
                    )
                    RequestState.Success(
                        api.fetchAllPosts().also {
                            database.removeAllPosts()
                            database.insertAllPosts(it)
                        }
                    )
                } else RequestState.Success(cachedPosts)
            }
        } catch (e: Exception) {
            RequestState.Error(e.message.toString())
        }
    }

    private fun isDataStale(): Boolean {
        val savedTimestamp = Instant.fromEpochMilliseconds(
            settings.getLong(FRESH_DATA_KEY, defaultValue = 0L)
        )
        val currentTimestamp = Clock.System.now()
        val difference =
            if (savedTimestamp > currentTimestamp) savedTimestamp - currentTimestamp
            else currentTimestamp - savedTimestamp
        return difference >= 24.hours
    }
}