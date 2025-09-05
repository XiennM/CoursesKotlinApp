package com.example.data

import android.util.Log
import com.example.data.local.CoursesDao
import com.example.effectivemobile.domain.models.Course
import com.example.effectivemobile.domain.repository.CoursesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.*

class CoursesRepositoryImpl(
    private val api: CoursesApi,
    private val dao: CoursesDao,
    private val json: Json,
    private val url: String =
        "https://drive.usercontent.google.com/u/0/uc?id=15arTK7XT2b7Yv4BJsmDctA4Hg-BbS8-q&export=download"
) : CoursesRepository {

    override fun observeCourses(): Flow<List<Course>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override suspend fun refresh() = withContext(Dispatchers.IO) {
        try {
            val resp = api.getCoursesRaw(url)
            if (!resp.isSuccessful) {
                Log.e("CoursesRepo", "HTTP error: ${resp.code()} ${resp.message()}")
                return@withContext
            }

            val body = resp.body()?.string().orEmpty()
            Log.d("CoursesRepo", "HTTP head: ${body.take(200)}")

            if (body.isBlank()) {
                Log.e("CoursesRepo", "Empty response body")
                return@withContext
            }

            val firstChar = body.firstOrNull { !it.isWhitespace() }
            if (firstChar != '{' && firstChar != '[') {
                Log.e("CoursesRepo", "Not JSON, looks like HTML/text: ${body.take(80)}")
                return@withContext
            }

            val root = json.parseToJsonElement(body)

            val dtos: List<CourseDto> = try {
                when {
                    root is JsonObject && "courses" in root ->
                        json.decodeFromJsonElement(CoursesResponseDto.serializer(), root).courses
                    root is JsonArray ->
                        json.decodeFromJsonElement(ListSerializer(CourseDto.serializer()), root)
                    root is JsonObject -> {
                        val arr = root.entries.firstOrNull { it.value is JsonArray }?.value
                            ?: JsonArray(emptyList())
                        json.decodeFromJsonElement(ListSerializer(CourseDto.serializer()), arr)
                    }
                    else -> emptyList()
                }
            } catch (e: Exception) {
                Log.e("CoursesRepo", "Parse failed", e)
                emptyList()
            }

            Log.d("CoursesRepo", "parsed ${dtos.size} items")

            val likes = dao.snapshotLikes().associate { it.id to it.hasLike }
            val entities = dtos.map { dto ->
                val preserved = likes[dto.id] ?: false
                dto.toEntity(preservedLike = preserved)
            }

            dao.upsertAll(entities)
            Log.d("CoursesRepo", "db count after upsert: ${dao.count()}")

        } catch (t: Throwable) {
            Log.e("CoursesRepo", "refresh failed", t)
        }
    }


    override suspend fun updateBookmark(id: Long, hasLike: Boolean) {
        withContext(Dispatchers.IO) { dao.updateBookmark(id, hasLike) }
    }
}
