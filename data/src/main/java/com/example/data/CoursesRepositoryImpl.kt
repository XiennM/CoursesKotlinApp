package com.example.data

import android.util.Log
import com.example.effectivemobile.domain.models.Course
import com.example.effectivemobile.domain.repository.CoursesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject


class CoursesRepositoryImpl(
    private val api: CoursesApi,
    private val url: String =
        "https://drive.usercontent.google.com/u/0/uc?id=15arTK7XT2b7Yv4BJsmDctA4Hg-BbS8-q&export=download",
    private val json: Json
) : CoursesRepository {

    override suspend fun getCourses(): List<Course> = withContext(Dispatchers.IO) {
        val body = api.getCoursesRaw(url).string()
        val head = body.take(300)
        Log.d("CoursesRepo", "HTTP head:\n$head")

        val trimmed = body.trimStart()
        if (trimmed.startsWith("<!DOCTYPE", true) || trimmed.startsWith("<html", true)) {
            throw IllegalStateException("HTML вместо JSON: проверьте публичность/квоту по ссылке Google Drive.")
        }

        val root: JsonElement = try {
            json.parseToJsonElement(body)
        } catch (e: SerializationException) {
            throw IllegalStateException("Невалидный JSON. Начало ответа: $head", e)
        }

        val dtos: List<CourseDto> = when {
            // кейс 1: { "courses": [ ... ] }
            root is JsonObject && "courses" in root -> {
                try {
                    json.decodeFromJsonElement(CoursesResponseDto.serializer(), root).courses
                } catch (e: SerializationException) {
                    throw IllegalStateException("Не удалось декодировать поле 'courses'. Начало: $head", e)
                }
            }
            // кейс 2: чистый массив [ ... ]
            root is JsonArray -> {
                try {
                    json.decodeFromJsonElement(ListSerializer(CourseDto.serializer()), root)
                } catch (e: SerializationException) {
                    throw IllegalStateException("Не удалось декодировать массив. Начало: $head", e)
                }
            }
            // кейс 3: объект с другим ключом (например, { "data": [ ... ] })
            root is JsonObject -> {
                val firstArrayEntry = root.entries.firstOrNull { it.value is JsonArray }
                if (firstArrayEntry != null) {
                    Log.w("CoursesRepo", "Используем массив из ключа '${firstArrayEntry.key}'")
                    json.decodeFromJsonElement(ListSerializer(CourseDto.serializer()), firstArrayEntry.value)
                } else {
                    throw IllegalStateException("JSON без массива курсов. Начало: $head")
                }
            }
            else -> throw IllegalStateException("Неизвестная структура JSON. Начало: $head")
        }

        dtos.map { it.toDomain() }
    }
}