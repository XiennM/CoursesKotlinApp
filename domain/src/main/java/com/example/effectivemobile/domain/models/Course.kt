package com.example.effectivemobile.domain.models


data class Course(
    val id: Long,
    val title: String,
    val description: String,
    val price: String,
    val rate: String,
    val startDate: String,
    val hasLike: Boolean,
    val publishDate: String,
    val imageUrl: String? = null
)