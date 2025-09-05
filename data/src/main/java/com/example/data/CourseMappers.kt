package com.example.data

import com.example.data.local.CourseEntity
import com.example.effectivemobile.domain.models.Course

fun CourseDto.toEntity(preservedLike: Boolean = false) = CourseEntity(
    id = id,
    title = title,
    description = description,
    price = price,
    rate = rate,
    startDate = startDate,
    publishDate = publishDate,
    imageUrl = imageUrl,
    hasLike = preservedLike
)

fun CourseEntity.toDomain() = Course(
    id = id,
    title = title,
    description = description,
    price = price,
    rate = rate,
    startDate = startDate,
    hasLike = hasLike,
    publishDate = publishDate,
    imageUrl = imageUrl
)
