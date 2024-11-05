package com.stevdza_san.demo.domain

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Post(
    val userId: Int,
    @Transient
    val thumbnail: String = "thumbnail.png",
    val id: Int,
    val title: String,
    val body: String
)
