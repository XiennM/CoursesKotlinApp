package com.example.data

import com.example.effectivemobile.domain.models.Course

fun CourseDto.toDomain(): Course = Course(
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