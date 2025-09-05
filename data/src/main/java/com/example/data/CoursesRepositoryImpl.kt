package com.example.data

import android.util.Log
import com.example.data.local.CourseEntity
import com.example.data.local.CoursesDao
import com.example.effectivemobile.domain.models.Course
import com.example.effectivemobile.domain.models.SortType
import com.example.effectivemobile.domain.repository.CoursesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.*

class CoursesRepositoryImpl(
    private val api: CoursesApi,
    private val dao: CoursesDao,
    private val json: Json,
    private val url: String =
        "https://drive.usercontent.google.com/u/0/uc?id=15arTK7XT2b7Yv4BJsmDctA4Hg-BbS8-q&export=download"
) : CoursesRepository {

    /** Мгновенный показ из БД; сортировка выбирается на уровне запроса DAO */
    override fun observeCourses(): Flow<List<Course>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    /** Тянем сеть, мёрджим флаги закладок, UPSERT в БД */
    override suspend fun refresh() = withContext(Dispatchers.IO) {
        val body = api.getCoursesRaw(url).string()
        Log.d("CoursesRepo", "HTTP head: ${body.take(200)}")

        val root: JsonElement = try { json.parseToJsonElement(body) }
        catch (e: SerializationException) { throw IllegalStateException("Bad JSON: ${body.take(200)}", e) }

        val dtos: List<CourseDto> = when {
            root is JsonObject && "courses" in root ->
                json.decodeFromJsonElement(CoursesResponseDto.serializer(), root).courses
            root is JsonArray ->
                json.decodeFromJsonElement(ListSerializer(CourseDto.serializer()), root)
            root is JsonObject -> {
                val arr = root.entries.firstOrNull { it.value is JsonArray }?.value
                    ?: throw IllegalStateException("No array in JSON")
                json.decodeFromJsonElement(ListSerializer(CourseDto.serializer()), arr)
            }
            else -> emptyList()
        }

        // Сохраняем текущее состояние лайков, чтобы не потерять при апдейте
        val likes = dao.snapshotLikes().associate { it.id to it.hasLike }

        val entities: List<CourseEntity> = dtos.map { dto ->
            val preserved = likes[dto.id] ?: false
            dto.toEntity(preservedLike = preserved)
        }

        dao.upsertAll(entities)
    }

    override suspend fun updateBookmark(id: Long, hasLike: Boolean) {
        withContext(Dispatchers.IO) { dao.updateBookmark(id, hasLike) }
    }
}
